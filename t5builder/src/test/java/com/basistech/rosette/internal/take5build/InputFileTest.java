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
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
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
    public void simplePayloads() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "simple-payloads.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
        inputFile.setPayloads(true);
        inputFile.setSimplePayloads(true);
        inputFile.setSimpleKeys(true);
        inputFile.read(source);
        Charset charset;
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            charset = Charsets.UTF_16BE;
        } else {
            charset = Charsets.UTF_16LE;
        }

        Iterable<Take5Pair> pairs = inputFile.getPairs();
        Iterator<Take5Pair> it = pairs.iterator();
        Take5Pair pair = it.next();
        assertArrayEquals("key0".toCharArray(), pair.getKey());
        assertArrayEquals("frog\u0000".getBytes(charset), pair.getValue());
        pair = it.next();
        assertArrayEquals("key1".toCharArray(), pair.getKey());
        assertArrayEquals("goo\"se\u0000".getBytes(charset), pair.getValue());
        pair = it.next();
        assertArrayEquals("key2".toCharArray(), pair.getKey());
        // note: this tests that we do _not_ process escapes!
        assertArrayEquals("helter\\u2012skelter\u0000".getBytes(charset), pair.getValue());
    }

    @Test
    public void noPayloads() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "keys-with-escapes.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
        inputFile.setPayloads(false);
        inputFile.setSimpleKeys(false);
        inputFile.read(source);

        Iterable<Take5Pair> pairs = inputFile.getPairs();
        for (Take5Pair pair : pairs) {
            assertNull(pair.getValue());
        }
    }

    // test an entire file of keys with no values, each gets a value of a null-terminated zero-length string.
    @Test
    public void emptySimplePayloads() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "keys-with-escapes.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
        inputFile.setPayloads(true);
        inputFile.setSimplePayloads(true);
        inputFile.setSimpleKeys(false);
        inputFile.read(source);

        Iterable<Take5Pair> pairs = inputFile.getPairs();
        for (Take5Pair pair : pairs) {
            assertArrayEquals(new byte[] { 0, 0 }, pair.getValue());
        }
    }

    @Test
    public void testBom() throws Exception {
        File input = File.createTempFile("t5.", ".txt");
        Files.write("\ufeffkey0\t\"value\"", input, Charsets.UTF_8);
        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
        inputFile.setPayloads(true);
        inputFile.setSimpleKeys(false);
        CharSource source = Files.asCharSource(input, Charsets.UTF_8);
        inputFile.read(source);
        Iterable<Take5Pair> pairs = inputFile.getPairs();
        Take5Pair pair = pairs.iterator().next();
        assertArrayEquals("key0".toCharArray(), pair.getKey());

        Files.write("\ufeffkey0", input, Charsets.UTF_8);
        inputFile = new InputFile(ByteOrder.nativeOrder());
        inputFile.setPayloads(false);
        inputFile.setSimpleKeys(false);
        source = Files.asCharSource(input, Charsets.UTF_8);
        inputFile.read(source);
        pairs = inputFile.getPairs();
        pair = pairs.iterator().next();
        assertArrayEquals("key0".toCharArray(), pair.getKey());
    }

    @Test
    public void testSimpleKeysAndPayloads() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "simple-keys-and-payloads.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
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
        ByteBuffer dataBuffer = ByteBuffer.wrap(pair.payload);
        dataBuffer.order(ByteOrder.nativeOrder());
        //BE? I'm too tired to sort this out.
        assertEquals(CharBuffer.wrap("some 2-byte string\u0000"), dataBuffer.asCharBuffer());
        pair = resultPairs.get(10);
        assertArrayEquals("key9\\u".toCharArray(), pair.key);
    }

    @Test
    public void testKeysWithEscapes() throws Exception {
        URL url = Resources.getResource(PayloadLexerTest.class, "keys-with-escapes.txt");
        CharSource source = Resources.asCharSource(url, Charsets.UTF_8);

        InputFile inputFile = new InputFile(ByteOrder.nativeOrder());
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
