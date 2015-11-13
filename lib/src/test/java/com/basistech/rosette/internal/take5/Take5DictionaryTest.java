/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5;

import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Tests of the Take5 runtime.
 */
@RunWith(Parameterized.class)
public class Take5DictionaryTest extends Assert {
    private static final String[] DAYS = {
        "Friday",
        "Monday",
        "Saturday",
        "Sun",
        "Sundae",
        "Sundaes",
        "Sunday",
        "Thursday",
        "Tuesday",
        "Wednesday",
        "july 4th",
    };

    private static final String[] NEXT_LETTERS = {
        "a", "b", "c", "d", "e", "f",
        "gaa", "gab", "gac",
        "gb", "gc", "gd", "ge", "gf", "gg", "gh", "gi",
        "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
        "r", "s", "t", "u", "v", "w", "x", "y", "z",
    };

    private final ByteOrder order;
    private Take5Dictionary daysDictionary;
    private ByteBuffer daysData;
    private Take5Dictionary nextLettersDictionary;

    public Take5DictionaryTest(ByteOrder order) {
        this.order = order;
    }

    @Parameterized.Parameters(name = "{index}: endian {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {ByteOrder.BIG_ENDIAN},
            {ByteOrder.LITTLE_ENDIAN}
        });
    }

    String endianDictionaryName(String name) {
        if (order == ByteOrder.BIG_ENDIAN) {
            return name + "-BE.bin";
        } else {
            return name + "-LE.bin";
        }
    }

    Take5Dictionary openDictionary(String path, ByteBuffer[] outData) throws IOException, Take5Exception {
        return openDictionary(path, "main", outData);
    }

    Take5Dictionary openDictionary(String path, String entryPoint, ByteBuffer[] outData) throws IOException, Take5Exception {
        File dictFile = new File(endianDictionaryName(path));
        return openDictionary(entryPoint, outData, dictFile);
    }

    Take5Dictionary openDictionary(String entryPoint, ByteBuffer[] outData, File dictFile) throws IOException, Take5Exception {
        MappedByteBuffer mappedDict = Files.map(dictFile, MapMode.READ_ONLY);
        if (outData != null) {
            outData[0] = mappedDict;
        }
        return new Take5DictionaryBuilder(mappedDict).entrypoint(entryPoint).build();
    }

    @Before
    public void before() throws IOException, Take5Exception {
        ByteBuffer[] data = new ByteBuffer[1];
        daysDictionary = openDictionary("src/test/dicts/days", data);
        daysData = data[0];
        nextLettersDictionary = openDictionary("src/test/dicts/next_letters", null);
    }

    /**
     * Test method for
     * {@link
     * com.basistech.rosette.internal.take5.Take5Dictionary#Take5Dictionary(java.nio.ByteBuffer, long)}.
     */
    @Test
    public void testTake5Dictionary() {
        assertNotNull(daysDictionary);
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#getCopyright()}.
     */
    @Test
    public void testGetCopyright() {
        String cp = daysDictionary.getCopyright();
        assertEquals("Copyright Test", cp);
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#getContentFlags()}.
     */
    @Test
    public void testGetContentFlags() {
        assertEquals(42, daysDictionary.getContentFlags());
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#getMinimumContentVersion()}.
     */
    @Test
    public void testGetMinimumContentVersion() {
        assertEquals(24, daysDictionary.getMinimumContentVersion());
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#getMaximumContentVersion()}.
     */
    @Test
    public void testGetMaximumContentVersion() {
        assertEquals(24, daysDictionary.getMaximumContentVersion());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#maximumMatchCount()}.
     */
    @Test
    public void testMaximumMatchCount() {
        assertEquals(3, daysDictionary.maximumMatchCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#maximumWordLength()}.
     */
    @Test
    public void testMaximumWordLength() {
        assertEquals(9, daysDictionary.maximumWordLength());
        assertEquals(3, nextLettersDictionary.maximumWordLength());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#maximumCharacter()}.
     */
    @Test
    public void testMaximumCharacter() {
        assertEquals(121, daysDictionary.maximumCharacter());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#minimumCharacter()}.
     */
    @Test
    public void testMinimumCharacter() {
        assertEquals(32, daysDictionary.minimumCharacter());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#maximumValueSize()}.
     */
    @Test
    public void testMaximumValueSize() {
        assertEquals(42, daysDictionary.maximumValueSize());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#wordCount()}.
     */
    @Test
    public void testWordCount() {
        assertEquals(11, daysDictionary.wordCount());
        assertEquals(NEXT_LETTERS.length, nextLettersDictionary.wordCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#fsaStateCount()}.
     */
    @Test
    public void testFsaStateCount() {
        assertEquals(33, daysDictionary.fsaStateCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#fsaAcceptStateCount()}.
     */
    @Test
    public void testFsaAcceptStateCount() {
        assertEquals(0, daysDictionary.fsaAcceptStateCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#fsaEdgeCount()}.
     */
    @Test
    public void testFsaEdgeCount() {
        assertEquals(40, daysDictionary.fsaEdgeCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#fsaAcceptEdgeCount()}.
     */
    @Test
    public void testFsaAcceptEdgeCount() {
        assertEquals(6, daysDictionary.fsaAcceptEdgeCount());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#getData()}.
     */
    @Test
    public void testGetData() {
        // how to test that it is a copy?
        assertFalse(daysData == daysDictionary.getData());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#creationDate()}.
     */
    @Test
    public void testCreationDate() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/mixed", null);
        Date cd = dict.creationDate();
        assertNotNull(cd);
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTime(cd);
        // The mixed binaries were built with "-X", so they have a known
        // date, 2005/12/31 23:59:60.259 UTC, which
        // Take5Dictionary.creationDate clamps down to 2005/12/31
        // 23:59:59.999 UTC.
        assertEquals(2005, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(31, calendar.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, calendar.get(Calendar.MINUTE));
        assertEquals(59, calendar.get(Calendar.SECOND));
        assertEquals(999, calendar.get(Calendar.MILLISECOND));
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#getMetadata()}.
     * @throws Take5Exception
     * @throws IOException
     */
    @Test
    public void testGetMetadata() throws IOException, Take5Exception {
        Take5Dictionary tsDict = openDictionary("src/test/dicts/trie_strings", null);
        Map<String, String> md = tsDict.getMetadata();
        String euro = md.get("euro");
        assertEquals("\u20ac", euro);

    }

    /**
     * Test {@link com.basistech.rosette.internal.take5.Take5Dictionary#setSkipBits}.
     * @throws Take5Exception
     * @throws IOException
     */
    @Test
    public void testSetSkipBits() throws IOException, Take5Exception {
        byte[] skipBits = new byte[8192];
        char c = 'x';
        skipBits[c >> 3] |= 1 << (c & 7);

        Take5Dictionary tsDict = openDictionary("src/test/dicts/trie_strings", null);
        byte[] oldSkipBits = tsDict.setSkipBits(skipBits);
        assertNull(oldSkipBits);

        // The C++ unit test does this first test, but for us it is a
        // terrible test because the result would be exactly the same even
        // if skipBits wern't set at all!  But what the heck:
        Take5Match match = tsDict.matchLongest("tryixxxx");
        assertEquals(3, match.getLength());
        assertEquals(3, match.getIndex());

        // But these next two actually check that the thing is working:
        match = tsDict.matchLongest("xtxrxyixxxx");
        assertEquals(6, match.getLength());
        assertEquals(3, match.getIndex());
        match = tsDict.matchLongest("xtxrxyxixxxx");
        assertEquals(7, match.getLength());
        assertEquals(3, match.getIndex());
    }

    /**
     * Test {@link com.basistech.rosette.internal.take5.Take5Dictionary#setSqueezeSpaces}.
     */
    @Test
    public void testSetSqueezeSpaces() throws Take5Exception {
        Take5Match match;

        boolean oldFlag = daysDictionary.setSqueezeSpaces(true);
        assertFalse(oldFlag);

        match = daysDictionary.matchExact("july4th");
        assertNull(match);
        match = daysDictionary.matchExact("july 4th");
        assertNotNull(match);
        match = daysDictionary.matchExact("july  4th");
        assertNotNull(match);
        match = daysDictionary.matchExact("july   4th");
        assertNotNull(match);
        match = daysDictionary.matchExact("july 4t h");
        assertNull(match);

        oldFlag = daysDictionary.setSqueezeSpaces(false);
        assertTrue(oldFlag);
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#matchExact(java.lang.String)}.
     * @throws Take5Exception
     */
    @Test
    public void testMatchExact() throws Take5Exception {
        Take5Match match = daysDictionary.matchExact("Wednesday");
        assertNotNull(match);
        assertEquals(9, match.getIndex());
        char[] buffer = new char[100];
        System.arraycopy("Wednesday".toCharArray(), 0, buffer, 33, "Wednesday".length());
        match = daysDictionary.matchExact(buffer, 33, "Wednesday".length());
        assertNotNull(match);
        assertEquals(9, match.getIndex());
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#matchLongest(java.lang.String)}.
     * @throws Take5Exception
     */
    @Test
    public void testMatchLongest() throws Take5Exception {
        Take5Match match = daysDictionary.matchLongest("Sund");
        assertEquals(3, match.getLength()); // the matching word is Sun
        assertEquals("yellow", match.getStringValue());

        char[] buffer = new char[100];
        System.arraycopy("Sund".toCharArray(), 0, buffer, 33, "Sund".length());
        match = daysDictionary.matchLongest(buffer, 33, 4);
        assertEquals(3, match.getLength()); // the matching word is Sun
        assertEquals("yellow", match.getStringValue());
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#matchMultiple(java.lang.String,
     *        com.basistech.rosette.internal.take5.Take5Match[])}.
     * @throws Take5Exception
     */
    @Test
    public void testMatchMultiple() throws Take5Exception {
        Take5Match[] matches = new Take5Match[10];
        int count = daysDictionary.matchMultiple("Sund", matches);
        // not a very interesting test.
        assertEquals(1, count);

        char[] buffer = new char[100];
        System.arraycopy("Sund".toCharArray(), 0, buffer, 33, "Sund".length());
        count = daysDictionary.matchMultiple(buffer, 33, 4, matches);
        assertEquals(1, count);
    }

    /**
     * Test method for
     * {@link com.basistech.rosette.internal.take5.Take5Dictionary#take5Search(char[], int, int,
     *  com.basistech.rosette.internal.take5.Take5Match, com.basistech.rosette.internal.take5.Take5Match[])}.
     * @throws Take5Exception
     */
    @Test
    public void testTake5SearchCharArrayTake5MatchTake5MatchArray() throws Take5Exception {
        Take5Match match = new Take5Match();
        int count = daysDictionary.take5Search("Sund".toCharArray(), 0, 4, match, null);
        assertEquals(1, count);
        assertEquals(3, match.getLength());
    }

    /**
     * Test method for {@link com.basistech.rosette.internal.take5.Take5Dictionary#nextLetters}.
     * @throws Take5Exception
     * @throws IOException
     */
    @Test
    public void testNextLetters() throws IOException, Take5Exception {
        Take5Dictionary dict = nextLettersDictionary;
        Take5Match match = dict.getStartMatch();

        assertEquals(0, match.length);

        // There should be 26 edges out of the "" state:
        Take5Match[] matches = dict.nextLetters(match);
        assertNotNull(matches);
        assertEquals(26, matches.length);

        match = matches[5];
        assertNotNull(match);
        assertEquals(1, match.length);
        assertEquals('f', match.c);
        assertTrue(match.isAcceptState());

        match = matches[6];
        assertNotNull(match);
        assertEquals(1, match.length);
        assertEquals('g', match.c);
        assertFalse(match.isAcceptState());

        // There should be 9 edges out of the "g" state:
        matches = dict.nextLetters(match);
        assertEquals(9, matches.length);
        assertEquals(2, matches[8].length);
    }

    /**
     * Test {@link
     * com.basistech.rosette.internal.take5.Take5Dictionary#reverseLookup}
     */
    @Test
    public void testReverseLookup() throws Take5Exception {
        testReverseLookupAll(nextLettersDictionary, NEXT_LETTERS);
        testReverseLookupAll(daysDictionary, DAYS);
    }

    // XXX: Unfortunately, the way this works wont really work as a test of
    // reverse lookup in the perfect hash table case.  When you get hash
    // table reverse lookup working, you'll have to test it some other way.
    private static void testReverseLookupAll(Take5Dictionary dict, String[] keys) throws Take5Exception {
        char[] buffer = new char[dict.maximumWordLength()];
        for (int i = 0; i < keys.length; i++) {
            int len = dict.reverseLookup(i, buffer);
            assertEquals(keys[i].length(), len);
            assertEquals(keys[i], new String(buffer, 0, len));
        }
    }

    /**
     * Test {@link
     * com.basistech.rosette.internal.take5.Take5Dictionary#walk}
     */
    @Test
    public void testWalk() throws Take5Exception {
        Take5Dictionary dict = daysDictionary;
        Take5Match start = dict.getStartMatch();
        char[] buffer = new char[50];
        TestWalker walker = new TestWalker(DAYS);

        // The numbers below were generated using pencil and paper for what
        // seemed to be the interesting tree depths.  If you mess with the
        // keys in DAYS.txt, you'll just have to recompute what they should
        // be.

        // Depth 9 -- just deep enough.
        walker.reset();
        dict.walk(walker, start, buffer, 9);
        walker.check(11, 0, 0);

        // Depth 8 -- just one short of enough.
        walker.reset();
        dict.walk(walker, start, buffer, 8);
        walker.check(10, 1, 0);

        // Depth 6 -- "Sundae" gives both a match and a sub-tree iterator
        // for "Sundaes".
        walker.reset();
        dict.walk(walker, start, buffer, 6);
        walker.check(4, 5, 1);

        // Depth 3 -- "Sun" gives both a match and a sub-tree iterator for
        // "Sundae", "Sundaes" and "Sunday".
        walker.reset();
        dict.walk(walker, start, buffer, 3);
        walker.check(0, 7, 1);

        // Depth 1 -- sub-tree iterators for each initial letter.
        walker.reset();
        dict.walk(walker, start, buffer, 1);
        walker.check(0, 6, 0);

        // Depth 0 -- degenerate case: no room, so you just get an iterator
        // for the whole tree.
        walker.reset();
        dict.walk(walker, start, buffer, 0);
        walker.check(0, 1, 0);
    }

    private static class TestWalker implements Take5Walker {
        int countAccept;
        int countLimit;
        int countBoth;
        String[] keys;

        TestWalker(String[] keys) {
            this.keys = keys;
        }

        void reset() {
            countAccept = 0;
            countLimit = 0;
            countBoth = 0;
        }

        private void keyCheck(Take5Match m, char[] buf) {
            int idx = m.getIndex();
            int len = m.getLength();
            assertTrue(0 <= idx && idx < keys.length);
            assertEquals(keys[idx].length(), len);
            assertEquals(keys[idx], new String(buf, 0, len));
        }

        public void foundAccept(Take5Match m, char[] buf, int len) {
            // The documentation promises nothing about isAcceptState() in
            // these cases, but the code makes sure the accept bit is not
            // set so that the foundBoth case returns a useful iterator, so
            // here we check to make sure that that useful property is
            // actually true.
            assertFalse("walker match isAcceptState() should be false", m.isAcceptState());
            keyCheck(m, buf);
            countAccept++;
        }

        public void foundLimit(Take5Match m, char[] buf, int len) {
            // See above.
            assertFalse("walker match isAcceptState() should be false", m.isAcceptState());
            countLimit++;
        }

        public void foundBoth(Take5Match m, char[] buf, int len) {
            // See above.
            assertFalse("walker match isAcceptState() should be false", m.isAcceptState());
            keyCheck(m, buf);
            countBoth++;
        }

        void check(int nA, int nL, int nB) {
            assertEquals(nA, countAccept);
            assertEquals(nL, countLimit);
            assertEquals(nB, countBoth);
        }
    }

    /**
     * Test {@link
     * com.basistech.rosette.internal.take5.Take5Dictionary#setEntryPoint}
     * and entry points in general.
     */
    @Test
    public void testEntryPoints() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/unified", "next_letters", null);

        // The last values in the entry point block are the min and max
        // character values, so checking them makes fairly sure that the
        // whole block was read correctly.
        assertEquals(0x61, dict.minimumCharacter());
        assertEquals(0x7A, dict.maximumCharacter());

        // There should be 26 edges out of the "" state:
        Take5Match start = dict.getStartMatch();
        Take5Match[] matches = dict.nextLetters(start);
        assertEquals(26, matches.length);
        // There should be 9 edges out of the "g" state:
        matches = dict.nextLetters(matches[6]);
        assertEquals(9, matches.length);

        Take5Match match = dict.matchExact("gab");
        assertNotNull(match);
        assertEquals(3, match.getLength());
        assertEquals(7, match.getIndex());

        match = dict.matchExact("gad");
        assertNull(match);

        testReverseLookupAll(dict, NEXT_LETTERS);

        dict.setEntryPoint("main"); // this is the "days" dictionary
        assertEquals(0x20, dict.minimumCharacter());
        assertEquals(0x79, dict.maximumCharacter());

        match = dict.matchExact("Sundae");
        assertNotNull(match);
        assertEquals(6, match.getLength());
        assertEquals(4, match.getIndex());

        match = dict.matchExact("Foobar");
        assertNull(match);

        testReverseLookupAll(dict, DAYS);
    }

    /**
     * Test entry points in the perfect hash format.
     */
    @Test
    public void testEntryPointsFromHash() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/unified-hash", "next_letters", null);

        Take5Match match = dict.matchExact("gab");
        assertNotNull(match);
        assertEquals(3, match.getLength());

        match = dict.matchExact("gad");
        assertNull(match);

        // Not yet: testReverseLookupAll(dict, NEXT_LETTERS);

        dict.setEntryPoint("main"); // this is the "days" dictionary
        assertEquals(0x20, dict.minimumCharacter());
        assertEquals(0x79, dict.maximumCharacter());

        match = dict.matchExact("Sundae");
        assertNotNull(match);
        assertEquals(6, match.getLength());
        assertEquals("vanilla", match.getStringValue());

        match = dict.matchExact("Sundaes");
        assertNotNull(match);
        assertEquals(7, match.getLength());
        assertEquals("chocolate/strawberry", match.getStringValue());

        match = dict.matchExact("Sun");
        assertNotNull(match);
        assertEquals(3, match.getLength());
        assertEquals("yellow", match.getStringValue());

        match = dict.matchExact("Foobar");
        assertNull(match);

        // Not yet: testReverseLookupAll(dict, DAYS);
    }

    /**
     * Test method for {@link
     * com.basistech.rosette.internal.take5.Take5Dictionary#advanceByChar(
     *com.basistech.rosette.internal.take5.Take5Match, char)}.
     */
    @Test
    @Ignore
    public void testAdvanceByChar() {
        // We're not even sure this is doing the right thing...
        fail("Not yet implemented");
    }

    @Test
    public void testGetComplexData() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/mixed", null);
        Take5Match match = dict.matchExact("key0");
        assertNotNull(match);
        ByteBuffer data = match.getComplexData(8 + 16);
        data.position(8);
        FloatBuffer floats = data.asFloatBuffer();
        checkKey0Values(data, floats);
    }

    @Test
    public void testGetComplexDataFromHash() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/mixed-hash", null);
        Take5Match match = dict.matchExact("key0");
        assertNotNull(match);
        ByteBuffer data = match.getComplexData(8 + 16);
        data.position(8);
        FloatBuffer floats = data.asFloatBuffer();
        checkKey0Values(data, floats);
    }

    private void checkKey0Values(ByteBuffer data, FloatBuffer floats) {
        assertEquals(4, data.get(0));
        assertEquals(0, data.get(1));
        assertEquals(2, data.get(2));
        assertEquals(3, data.get(3));
        assertEquals(5, data.get(4));
        assertEquals(0, data.get(5));
        assertEquals(0, data.get(6));
        assertEquals(0, data.get(7));
        assertEquals(0.11f, floats.get(0), 0.00001);
        assertEquals(-0.22f, floats.get(1), 0.00001);
        assertEquals(0.33f, floats.get(2), 0.0001);
        assertEquals(-0.44, floats.get(3), 0.0001);
    }

    @Test
    public void testGetOffsetValue() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/mixed", null);
        Take5Match match = dict.matchExact("key0");
        assertNotNull(match);
        int offset = match.getOffsetValue();
        ByteBuffer data = dict.getData();
        data.position(offset);
        ByteBuffer bytes = data.slice();
        bytes.order(data.order());
        data.position(offset + 8);
        FloatBuffer floats = data.asFloatBuffer();
        checkKey0Values(bytes, floats);
    }

    @Test
    public void testWalkPerfhashKeys() throws Exception {
        Take5Dictionary dict = openDictionary("src/test/dicts/hex", null);
        Set<String> keys = new HashSet<String>();
        for (String k : dict.keyIterator()) {
            assertTrue("each key added: " + k, keys.add(k));
        }
        assertEquals(53, keys.size());

    }
}
