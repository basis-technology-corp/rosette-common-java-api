/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

//import java.nio.ByteOrder;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;

/**
 * Command line Take5 builder.  Still a work in progress.  XXX
 */
public final class BuildCmd {

    private BuildCmd() {
    }

    static void usage(String msg) {
        System.err.print(msg + "\n");
        System.err.print("Usage: <input> <output>\n");
        System.exit(64);        // EX_USAGE
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            usage("Wrong number of arguments: " + args.length);
        }
        String input = args[0];
        String output = args[1];
        
        InputStreamReader instream = null; // avoid compiler complaint
        try {
            instream = new InputStreamReader(new FileInputStream(input), "UTF-8");
        } catch (IOException e) {
            System.err.print("Error opening input:\n");
            System.err.print("    " + e + "\n");
            System.exit(66);    // EX_NOINPUT
        }

        Take5Builder builder;
        try {
            builder = Take5Builder.Builder.engine(Take5Builder.Engine.FSA).mode(Take5Builder.Mode.VALUE).valueSize(16).build();
            Take5EntryPoint ep = builder.newEntryPoint("main");
            ep.inputName = input; // a secret known only to the command line tool
            ep.loadContent(new Scanner(new BufferedReader(instream), input));
        } catch (Take5BuildException e) {
            System.err.print("Error building:\n");
            System.err.print("    " + e.getMessage() + "\n");
            System.exit(65);    // EX_DATAERR
            return; // fool checkstyle.
        }

        builder.formatDescription(System.out);

        try {
            builder.buildFile(output);
        } catch (IOException e) {
            System.err.print("Error while creating output:\n");
            System.err.print("    " + e + "\n");
            System.exit(73);    // EX_CANTCREAT
        }
    }
}
