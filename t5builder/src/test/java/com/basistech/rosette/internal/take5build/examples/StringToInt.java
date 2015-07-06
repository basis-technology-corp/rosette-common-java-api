/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build.examples;

import com.basistech.rosette.internal.take5.Take5Dictionary;
import com.basistech.rosette.internal.take5.Take5Match;
import com.basistech.rosette.internal.take5build.ReusableTake5Pair;
import com.basistech.rosette.internal.take5build.Take5Builder;
import com.basistech.rosette.internal.take5build.Take5BuilderFactory;
import com.basistech.rosette.internal.take5build.Take5EntryPoint;
import com.basistech.rosette.internal.take5build.Take5Pair;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSink;
import com.google.common.io.Files;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

/**
 * Example that demonstrates payload details.
 * This makes an LE take5, always, even if you run it on a BE machine.
 */
public class StringToInt {
    @Argument(index = 0, usage = "Output T5", metaVar = "OUTPUT", required = true)
    File outputFile;

    public static void main(String args[]) throws Exception {
        StringToInt that = new StringToInt();
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

    private byte[] makePayload(int value) {
        byte[] data = new byte[4];
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return data;
    }

    private void build() throws Exception {
        List<Take5Pair> data = Lists.newArrayList();
        data.add(new ReusableTake5Pair("a", makePayload(1000), 4));
        data.add(new ReusableTake5Pair("b", makePayload(2000), 4));
        data.add(new ReusableTake5Pair("c", makePayload(2000), 4));

        Take5BuilderFactory builderFactory = new Take5BuilderFactory();
        builderFactory.valueSize(4)
                .putMetadata("Humpty", "Dumpty");
        Take5Builder builder = builderFactory.build();
        Take5EntryPoint entrypoint = builder.newEntryPoint("main");
        entrypoint.loadContent(data.iterator());
        ByteSink sink = Files.asByteSink(outputFile);
        builder.buildToSink(sink);

        ByteBuffer dictBuffer = Files.map(outputFile);
        dictBuffer.order(ByteOrder.LITTLE_ENDIAN);
        Take5Dictionary dict = new Take5Dictionary(dictBuffer, dictBuffer.capacity());
        Take5Match match = dict.matchExact("a");
        int val = dictBuffer.getInt(match.getOffsetValue());
        System.out.printf("a -> %d\n", val);
    }
}
