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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@SuppressWarnings("PMD")
public final class Take5Cmd {

    boolean verbose;
    byte[] skipBits;
    String lookupFunc;
    String dictionary;
    String wordList;

    private Take5Cmd() {

    }

    public boolean readSkipBits(byte[] localSkipBits, String bits) {
        String[] codePoints = bits.split(" ");
        for (String codePoint2 : codePoints) {
            if (codePoint2.length() != 4) {
                System.err.println("Not long enough to be a codepoint, '" + codePoint2 + "'.");
                return false;
            }

            int codePoint = Integer.valueOf(codePoint2, 16).intValue();
            localSkipBits[codePoint >> 3] |= 1 << (codePoint & 7);
        }
        return true;
    }

    private void parseArgs(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage:");
            System.err.println("  com.basistech.internal.take5.Take5Cmd [-v] [-s skiplist]");
            System.err.println("                               lookupFunc dictionary wordList");
            return;
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-v")) {
                verbose = true;
            } else if (args[i].equals("-s")) {
                if (i + 1 == args.length) {
                    System.err.println("Missing argument to '-s'.");
                    return;
                }
                skipBits = new byte[8192];
                if (!readSkipBits(skipBits, args[++i])) {
                    return;
                }
            } else if (lookupFunc == null) {
                lookupFunc = args[i];
            } else if (dictionary == null) {
                dictionary = args[i];
            } else if (wordList == null) {
                wordList = args[i];
            } else {
                System.err.println("Too few arguments.");
            }
        }

        if (lookupFunc == null) {
            System.err.println("Missing lookup function.");
        }

        if (dictionary == null) {
            System.err.println("Missing dictionary.");
        }

        if (wordList == null) {
            System.err.println("Word list missing.");
        }

        if (!"FindExact".equals(lookupFunc) && !"FindLongest".equals(lookupFunc)
            && !"FindAllMatches".equals(lookupFunc)) {
            System.err.println("Unknown lookup function '" + lookupFunc + "'.");
        }
    }

    public static void main(String[] args) {
        Take5Cmd cmd = new Take5Cmd();
        cmd.parseArgs(args);
        try {
            cmd.work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void work() throws FileNotFoundException, IOException, Take5Exception,
        UnsupportedEncodingException {
        RandomAccessFile file = new RandomAccessFile(dictionary, "r");
        FileChannel chan = file.getChannel();
        ByteBuffer data = chan.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        Take5Dictionary dict = new Take5Dictionary(data, file.length());
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(wordList), "UTF-8"));
        dict.setSkipBits(skipBits);

        String word;
        int nwords = 0;
        int found = 0;
        long start = System.currentTimeMillis();

        while ((word = in.readLine()) != null) {
            word = word.split("\t")[0];

            if (verbose) {
                System.out.print(word + "\t");
            }

            if ("FindExact".equals(lookupFunc)) {
                Take5Match match = dict.matchExact(word);
                if (match != null) {
                    found++;
                    if (verbose) {
                        System.out.println(match.getStringValue());
                    }
                } else {
                    if (verbose) {
                        System.out.println("<not found>");
                    }
                }
            } else if ("FindLongest".equals(lookupFunc)) {
                Take5Match match = dict.matchLongest(word);
                found++;
                if (verbose) {
                    if (match != null) {
                        if (match.getLength() != word.length() || skipBits != null) {
                            System.out.print("[" + match.getLength() + "]");
                        }
                        System.out.println(match.getStringValue());
                    } else {
                        System.out.println("<not found>");
                    }
                }
            } else if ("FindAllMatches".equals(lookupFunc)) {
                Take5Match[] matches = new Take5Match[32];
                int c = dict.matchMultiple(word, matches);
                found++;
                if (!verbose) {
                    // empty
                } else if (c == 0) {
                    System.out.println("<not found>");
                } else {
                    for (int i = 0; i < c; i++) {
                        if (matches[i].getLength() != word.length() || skipBits != null) {
                            System.out.print("[" + matches[i].getLength() + "]");
                        }
                        if (i + 1 >= c) {
                            System.out.println(matches[i].getStringValue());
                        } else {
                            System.out.print(matches[i].getStringValue() + "\t");
                        }
                    }
                }
            } else {
                System.err.println("Unknown lookup function '" + lookupFunc + "'.");
                return;
            }
            nwords++;
        }

        long end = System.currentTimeMillis();
        System.out.println("Found " + found + " words out of " + nwords + " in " + (end - start) / 1000.0
                           + " seconds.");
    }
}
