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

package com.basistech.rosette.internal.take5build;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.List;

/**
 *
 */
public class InputFileTest extends Assert {

    static class Pair {

        char[] key;
        byte[] payload;
        int alignment;

        Pair(Take5Pair pair) {
            this.key = pair.getKey();
            this.payload = pair.getValue();
            this.alignment = pair.getAlignment();
        }
    }

    @Test
    public void testSimpleKeysAndPayloads() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "simple-keys-and-payloads.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile();
        inputFile.setPayloads(true);
        inputFile.setSimpleKeys(true);
        inputFile.read(source);

        List<Pair> resultPairs = Lists.newArrayList();
        Iterable<Take5Pair> pairs = inputFile.getPairs();

        for (Take5Pair pair : pairs) {
            resultPairs.add(new Pair(pair));
        }

        assertEquals(11, resultPairs.size());
        Pair pair = resultPairs.get(0);
        assertArrayEquals("key0".toCharArray(), pair.key);
        pair = resultPairs.get(2);
        assertArrayEquals("key2".toCharArray(), pair.key);
        //BE? I'm too tired to sort this out.
        assertArrayEquals("some 2-byte string\u0000".toCharArray(), new String(pair.payload, Charsets.UTF_16BE).toCharArray());
        pair = resultPairs.get(10);
        assertArrayEquals("key9\\u".toCharArray(), pair.key);
    }

    @Test
    public void testKeysWithEscapes() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "keys-with-escapes.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile();
        inputFile.setPayloads(false);
        inputFile.setSimpleKeys(false);
        inputFile.read(source);

        List<Pair> resultPairs = Lists.newArrayList();
        Iterable<Take5Pair> pairs = inputFile.getPairs();

        for (Take5Pair pair : pairs) {
            resultPairs.add(new Pair(pair));
        }

        assertArrayEquals("1hello".toCharArray(), resultPairs.get(0).key);
        assertArrayEquals("2h\tello".toCharArray(), resultPairs.get(1).key);
        assertArrayEquals("3h\u2e00ello".toCharArray(), resultPairs.get(2).key);
        assertArrayEquals("4h\u0000ello".toCharArray(), resultPairs.get(3).key);
    }
}
