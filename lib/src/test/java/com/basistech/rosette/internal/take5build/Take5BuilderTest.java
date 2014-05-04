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

//import java.io.IOException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import com.basistech.rosette.internal.take5.Take5Dictionary;
import com.basistech.rosette.internal.take5.Take5Exception;
import com.basistech.rosette.internal.take5.Take5Match;
import org.junit.rules.ExpectedException;

/**
 * Unit tests for Take5Builder.
 */
public class Take5BuilderTest {

    //CHECKSTYLE:OFF
    @Rule
    public ExpectedException exception = ExpectedException.none();
    //CHECKSTYLE:ON

    // All the words in /usr/share/dict/words that match "^[a-f]+$"
    private static String[] hexWords = {
            "a", "abed", "accede", "acceded", "ace", "aced", "ad", "add", "added",
            "baa", "baaed", "babe", "bad", "bade", "be", "bead", "beaded", "bed",
            "bedded", "bee", "beef", "beefed", "cab", "cabbed", "cad", "cede",
            "ceded", "dab", "dabbed", "dad", "dead", "deaf", "deb", "decade",
            "decaf", "deed", "deeded", "deface", "defaced", "ebb", "ebbed",
            "efface", "effaced", "fa", "facade", "face", "faced", "fad", "fade",
            "faded", "fed", "fee", "feed",
    };

    private static String[] dmwwExample = {
            "a",
            "ai",
            "aient",
            "ais",
            "ait",
            "ant",
            "as",
            "asse",
            "assent",
            "asses",
            "assiez",
            "assions",
            "e",
            "ent",
            "er",
            "era",
            "erai",
            "eraient",
            "erais",
            "erait",
            "eras",
            "erez",
            "eriez",
            "erions",
            "erons",
            "eront",
            "es",
            "ez",
            "iez",
            "ions",
            "ons",
            "\u00E2mes",
            "\u00E2t",
            "\u00E2tes",
            "\u00E8rent",
            "\u00E9",
            "\u00E9e",
            "\u00E9es",
            "\u00E9s",
    };

    private static byte[] data = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 33, 34, 35, 36, 37, 38, 39,
            40, 41, 42, 43, 44, 45, 46, 47, 48, 49,
            50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
            60, 61, 62, 63, 64, 65, 66, 67, 68, 69,
            70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
            80, 81, 82, 83, 84, 85, 86, 87, 88, 89,
            90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
    };

    // 5 offsets...
    private static int[] offsets = {
            10, 20, 30, 40, 50
    };

    // 9 lengths...
    private static int[] lengths = {
            -1, 0, 2, 8, 11, 13, 16, 17, 32
    };

    // So there are 45 (offset, length) pairs.  Since the lengths of 0 and
    // -1 are special cases, this works out to 37 different payloads, with
    // the pattern cycling mod 45.  With the following 6 alignments cycling
    // mod 6 we get just a few payloads with more than one alignment
    // specified -- but that's enough for testing.  (All assuming we're
    // testing using the 53 element hexWords.)
    private static int[] alignments = {
            1, 2, 2, 4, 8, 16
    };

    static class LocalPair extends ReusableTake5Pair {
        LocalPair(String key) {
            super(key);
        }
    }

    @Test
    public void testSubclassedPair() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.INDEX).build();
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        for (String s : dmwwExample) {
            keys.add(new LocalPair(s));
        }
        ep.loadContent(keys.iterator());
        assertEquals(39, builder.totalKeyCount);
        assertEquals(23, builder.stateCount);
        assertEquals(45, builder.edgeCount);
        assertEquals(23, builder.acceptEdgeCount);
        assertEquals(0, ep.indexOffset);
        assertEquals(39, ep.keyCount);
        assertEquals(23, ep.stateCount);
        assertEquals(45, ep.edgeCount);
        assertEquals(23, ep.acceptEdgeCount);
        assertEquals(5, ep.maxMatches);
        assertEquals(7, ep.maxKeyLength);
        assertEquals('a', ep.minCharacter);
        assertEquals(0xE9, ep.maxCharacter);
        assertFalse(ep.acceptEmpty);
        ByteBuffer t5 = builder.buildBuffer();
        Take5Dictionary dict = new Take5Dictionary(t5, t5.limit());
        assertEquals(7, dict.maximumWordLength());
        int i = 0;
        char[] sbuf = new char[dict.maximumWordLength()];
        for (String s : dmwwExample) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            assertEquals(i, m.getIndex());
            int len = dict.reverseLookup(i, sbuf);
            assertEquals(s.length(), len);
            assertEquals(s, new String(sbuf, 0, len));
            i++;
        }
    }

    /**
     * Run the canonical example.
     */
    @Test
    public void testCanonicalExample() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.INDEX).build();
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        for (String s : dmwwExample) {
            keys.add(new ReusableTake5Pair(s));
        }
        ep.loadContent(keys.iterator());
        assertEquals(39, builder.totalKeyCount);
        assertEquals(23, builder.stateCount);
        assertEquals(45, builder.edgeCount);
        assertEquals(23, builder.acceptEdgeCount);
        assertEquals(0, ep.indexOffset);
        assertEquals(39, ep.keyCount);
        assertEquals(23, ep.stateCount);
        assertEquals(45, ep.edgeCount);
        assertEquals(23, ep.acceptEdgeCount);
        assertEquals(5, ep.maxMatches);
        assertEquals(7, ep.maxKeyLength);
        assertEquals('a', ep.minCharacter);
        assertEquals(0xE9, ep.maxCharacter);
        assertFalse(ep.acceptEmpty);
        ByteBuffer t5 = builder.buildBuffer();
        Take5Dictionary dict = new Take5Dictionary(t5, t5.limit());
        assertEquals(7, dict.maximumWordLength());
        int i = 0;
        char[] sbuf = new char[dict.maximumWordLength()];
        for (String s : dmwwExample) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            assertEquals(i, m.getIndex());
            int len = dict.reverseLookup(i, sbuf);
            assertEquals(s.length(), len);
            assertEquals(s, new String(sbuf, 0, len));
            i++;
        }
    }

    /**
     * Test that the empty key works.
     */
    @Test
    public void testEmptyString() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().build();
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = Lists.newArrayList();
        keys.add(new ReusableTake5Pair(""));
        for (String s : hexWords) {
            keys.add(new ReusableTake5Pair(s));
        }
        ep.loadContent(keys.iterator());
        assertEquals(34, builder.stateCount);
        assertEquals(54, ep.keyCount);
        assertTrue(ep.acceptEmpty);
    }

    /**
     * Test for duplicates in the input.
     */
    @Test
    public void testDuplicateKey() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().build();
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = Lists.newArrayList();
        keys.add(new ReusableTake5Pair("abc"));
        keys.add(new ReusableTake5Pair("abd"));
        keys.add(new ReusableTake5Pair("abd"));
        keys.add(new ReusableTake5Pair("abe"));
        try {
            ep.loadContent(keys.iterator());
        } catch (Take5ParseError e) {
            assertEquals("Keys out of order", e.message);
            assertEquals(2, e.keyNumber);
            return;
        }
        fail("duplicate key not detected?");
    }

    /**
     * Test for unsorted input.
     */
    @Test
    public void testUnsortedInput() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.INDEX).build();
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = Lists.newArrayList();
        keys.add(new ReusableTake5Pair("abc"));
        keys.add(new ReusableTake5Pair("abe"));
        keys.add(new ReusableTake5Pair("abd"));
        keys.add(new ReusableTake5Pair("abf"));
        try {
            ep.loadContent(keys.iterator());
        } catch (Take5ParseError e) {
            assertEquals("Keys out of order", e.message);
            assertEquals(2, e.keyNumber);
            return;
        }
        fail("unsorted key not detected?");
    }

    /**
     * Test storing a bunch of random payloads.
     */
    @Test
    public void testPayloads() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.PTR).valueSize(16).build();

        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        int i = 0;
        for (String s : hexWords) {
            keys.add(generatePayload(s, i++));
        }
        ep.loadContent(keys.iterator());
        assertEquals(53, builder.totalKeyCount);
        assertEquals(34, builder.stateCount);
        assertEquals(60, builder.edgeCount);
        assertEquals(25, builder.acceptEdgeCount);
        ByteBuffer t5 = builder.buildBuffer();
        Take5Dictionary dict = new Take5Dictionary(t5, t5.limit());
        int j = 0;
        for (String s : hexWords) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            checkPayload(t5, m.getOffsetValue(), j++);
        }
    }

    private Take5Dictionary loadGenerated(Take5Builder builder, Take5EntryPoint[] returnEntrypoint) throws Take5BuildException, Take5Exception {
        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        int i = 0;
        for (String s : hexWords) {
            keys.add(generatePayload(s, i++));
        }

        ep.loadContent(keys.iterator());
        returnEntrypoint[0] = ep;
        ByteBuffer t5 = builder.buildBuffer();
        return new Take5Dictionary(t5, t5.limit());
    }

    @Test
    public void testPerfhash() throws Exception {
        Take5EntryPoint[] ep = new Take5EntryPoint[1];
        Take5Dictionary dict = loadGenerated(new Take5Builder.Factory().engine(Take5Builder.Engine.PERFHASH).keyFormat(Take5Builder.KeyFormat.HASH_STRING).valueFormat(Take5Builder.ValueFormat.PTR).valueSize(16).build(), ep);
        int j = 0;
        for (String s : hexWords) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            checkPayload(dict.getData(), m.getOffsetValue(), j++);
        }
    }

    @Test
    public void testIgnoredPayloads() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.IGNORE).valueSize(16).build();

        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        int i = 0;
        for (String s : hexWords) {
            keys.add(generatePayload(s, i++));
        }
        ep.loadContent(keys.iterator());

        ByteBuffer t5 = builder.buildBuffer();
        Take5Dictionary dict = new Take5Dictionary(t5, t5.limit());
        int j = 0;
        for (String s : hexWords) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            try {
                checkPayload(t5, m.getOffsetValue(), j++);
                fail("Failed to throw lack of payloads");
            } catch (Take5Exception e) {
                assertEquals(Take5Exception.NO_POINTERS_HERE, e.getType());
            }
        }
    }

    @Test
    public void testIndices() throws Exception {
        Take5Builder builder = new Take5Builder.Factory().valueFormat(Take5Builder.ValueFormat.INDEX).valueSize(16).build();

        Take5EntryPoint ep = builder.newEntryPoint("main");
        List<Take5Pair> keys = new ArrayList<Take5Pair>();
        int i = 0;
        for (String s : hexWords) {
            keys.add(generatePayload(s, i++));
        }
        ep.loadContent(keys.iterator());

        ByteBuffer t5 = builder.buildBuffer();
        Take5Dictionary dict = new Take5Dictionary(t5, t5.limit());
        int j = 0;
        for (String s : hexWords) {
            Take5Match m = dict.matchExact(s);
            assertNotNull(m);
            try {
                checkPayload(t5, m.getOffsetValue(), j++);
                fail("Failed to throw lack of payloads");
            } catch (Take5Exception e) {
                assertEquals(Take5Exception.NO_POINTERS_HERE, e.getType());
            }
            //TODO: How do we know the right answer?
        }
    }

    private Take5Pair generatePayload(String key, int i) {
        int offset = offsets[i % offsets.length];
        int length = lengths[i % lengths.length];
        int alignment = alignments[i % alignments.length];
        ReusableTake5Pair p = new ReusableTake5Pair(key);
        if (length >= 0) {
            p.setValue(data, alignment, offset, length);
        }
        return p;
    }

    private void checkPayload(ByteBuffer t5, int valOffset, int i) {
        int length = lengths[i % lengths.length];
        if (valOffset == 0) {
            assertTrue(length < 0);
        } else {
            assertFalse(length < 0);
            assertTrue(valOffset > 0);
            int offset = offsets[i % offsets.length];
            int alignment = alignments[i % alignments.length];
            assertTrue(valOffset % alignment == 0);
            byte[] val = new byte[length];
            t5.position(valOffset);
            t5.get(val);
            assertTrue(Utils.equalBytes(val, 0, data, offset, length));
        }
    }
}
