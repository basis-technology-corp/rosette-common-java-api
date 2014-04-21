/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.internal.take5;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public final class Take5Perf {
    private Take5Perf() {

    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage:\n  java com.basistech.internal.take5.Take5Perf dictionary words");
            return;
        }

        try {
            RandomAccessFile file = new RandomAccessFile(args[0], "r");
            FileChannel chan = file.getChannel();
            ByteBuffer data = chan.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            Take5Dictionary dict = new Take5Dictionary(data, file.length());
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[1]),
                                                                         "UTF-8"));

            List<String> words = new ArrayList<String>();
            String word;
            while ((word = in.readLine()) != null) {
                words.add(word);
            }
            int nwords = words.size();
            int found = 0;

            long start = System.currentTimeMillis();
            for (int i = 0; i < nwords; i++) {
                if (dict.matchExact(words.get(i)) != null) {
                    found++;
                }
            }
            long end = System.currentTimeMillis();

            System.out.println("Found " + found + " out of " + nwords + " words in " + (end - start) / 1000.0
                               + " seconds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
