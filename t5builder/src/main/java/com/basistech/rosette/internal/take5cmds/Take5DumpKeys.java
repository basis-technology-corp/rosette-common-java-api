/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010-2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5cmds;

import com.basistech.rosette.internal.take5.Take5Dictionary;
import com.basistech.rosette.internal.take5.Take5Exception;
import com.basistech.rosette.internal.take5.Take5Match;
import com.basistech.rosette.internal.take5.Take5Walker;
import com.basistech.rosette.internal.take5build.ByteOrderOptionHandler;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * CLI to dump keys Take5.
 * This depends on package access into Take5Dictionary.
 */
public final class Take5DumpKeys {
    @Argument(metaVar = "INPUT", required = true, usage = "Input Take5 file")
    File inputFile;

    @Option(name = "-byteOrder", aliases = {"-byte-order" }, usage = "byte order (LE or BE)", metaVar = "ORDER", handler = ByteOrderOptionHandler.class)
    ByteOrder byteOrder = ByteOrder.nativeOrder();

    @Option(name = "-entrypoint", aliases = {"-ep" }, usage = "entrypoint to dump", metaVar = "ENTRYPOINT")
    String entryPointName = "main";

    private PrintWriter pw;

    Take5DumpKeys() {
        //
    }

    public static void main(String[] args) {
        Take5DumpKeys that = new Take5DumpKeys();
        CmdLineParser parser = new CmdLineParser(that);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        try {
            that.dump();
        } catch (Take5Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void dump() throws Take5Exception {
        ByteBuffer t5Buffer;
        try {
            t5Buffer = Files.map(inputFile);
        } catch (IOException e) {
            System.err.println(String.format("Failed to open %s: %s", inputFile.getAbsolutePath(), e.getMessage()));
            System.exit(1);
            return;
        }

        pw = new PrintWriter(new OutputStreamWriter(System.out, Charsets.UTF_8));

        Take5Dictionary dict1 = new Take5Dictionary(t5Buffer, t5Buffer.capacity(), entryPointName);
        Take5Match startState = dict1.getStartMatch();
        char[] buffer = new char[4096];

        dict1.walk(new Take5Walker() {
            @Override
            public void foundAccept(Take5Match match, char[] buffer, int buflen) {
                pw.print(new String(buffer, 0, match.getLength()));
                pw.print("\n");
            }

            @Override
            public void foundLimit(Take5Match match, char[] buffer, int buflen) {

            }

            @Override
            public void foundBoth(Take5Match match, char[] buffer, int buflen) {

            }
        },
                startState,
                buffer,
                buffer.length);



        pw.flush();
    }


}
