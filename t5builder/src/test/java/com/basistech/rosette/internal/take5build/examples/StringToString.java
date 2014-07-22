/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build.examples;

import com.basistech.rosette.internal.take5build.MapTake5PairSource;
import com.basistech.rosette.internal.take5build.Take5Builder;
import com.basistech.rosette.internal.take5build.Take5BuilderFactory;
import com.basistech.rosette.internal.take5build.Take5EntryPoint;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSink;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Demonstrate building a basic String->(null-terminated) String take5.
 */
public final class StringToString {

    @Argument(usage = "tsv: key, value", metaVar = "INPUT", required = true)
    File inputFile;

    @Argument(index = 1, usage = "Output T5", metaVar = "OUTPUT", required = true)
    File outputFile;

    public static void main(String args[]) throws Exception {
        StringToString that = new StringToString();
        CmdLineParser parser = new CmdLineParser(that);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
        that.build();
    }

    private void build() throws Exception {
        final Map<String, CharSequence> data = Maps.newTreeMap(); // has to be ordered.
        Files.readLines(inputFile, Charsets.UTF_8, new LineProcessor<Void>() {
            @Override
            public boolean processLine(String line) throws IOException {
                String[] bits = line.split("\t");
                data.put(bits[0], bits[1]);
                return true;
            }

            @Override
            public Void getResult() {
                return null;
            }
        });

        Take5BuilderFactory builderFactory = new Take5BuilderFactory();
        builderFactory.putMetadata("Humpty", "Dumpty");
        Take5Builder builder = builderFactory.build();
        Take5EntryPoint entrypoint = builder.newEntryPoint("main");
        MapTake5PairSource source = new MapTake5PairSource(builder, data);
        entrypoint.loadContent(source.iterator());

        ByteSink sink = Files.asByteSink(outputFile);
        builder.buildToSink(sink);
    }
}
