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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
@SuppressWarnings("deprecation")
public class ObsoleteTake5DictionaryTest extends Assert {
    
    private Take5Dictionary daysDictionary;
    private ByteBuffer daysData;
    
    static String endianDictionaryName(String name) {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            return name + "-BE.bin";
        } else {
            return name + "-LE.bin";
        }
    }
    
    static Take5Dictionary openDictionary(String path, ByteBuffer[] outData) 
        throws IOException, Take5Exception {
        String fullName = endianDictionaryName(path);
        RandomAccessFile randomAccessFile = new RandomAccessFile(fullName, "r");
        MappedByteBuffer mappedDict = randomAccessFile.getChannel().map(MapMode.READ_ONLY, 0,
                                                                        randomAccessFile.length());
        if (outData != null) {
            outData[0] = mappedDict;
        }
        return new Take5Dictionary(mappedDict, randomAccessFile.length());
    }
    
    @Before
    public void before() throws IOException, Take5Exception {
        ByteBuffer[] data = new ByteBuffer[1];
        daysDictionary = openDictionary("test_dicts/days", data);
        daysData = data[0];
    }

    /**
     * Test method for 
     * {@link com.basistech.internal.take5.Take5Dictionary#Take5Dictionary(java.nio.ByteBuffer, long)}.
     */
    @Test
    public void testTake5Dictionary() {
        assertNotNull(daysDictionary);
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getCopyright()}.
     */
    @Test
    public void testGetCopyright() {
        String cp = daysDictionary.getCopyright();
        assertEquals("Copyright Test", cp);
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getContentFlags()}.
     */
    @Test
    public void testGetContentFlags() {
        assertEquals(42, daysDictionary.getContentFlags());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getMinimumContentVersion()}.
     */
    @Test
    public void testGetMinimumContentVersion() {
        assertEquals(24, daysDictionary.getMinimumContentVersion());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getMaximumContentVersion()}.
     */
    @Test
    public void testGetMaximumContentVersion() {
        assertEquals(24, daysDictionary.getMaximumContentVersion());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#maximumMatchCount()}.
     */
    @Test
    public void testMaximumMatchCount() {
        assertEquals(3, daysDictionary.maximumMatchCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#maximumWordLength()}.
     */
    @Test
    public void testMaximumWordLength() {
        assertEquals(9, daysDictionary.maximumWordLength());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#maximumCharacter()}.
     */
    @Test
    public void testMaximumCharacter() {
        assertEquals(121, daysDictionary.maximumCharacter());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#minimumCharacter()}.
     */
    @Test
    public void testMinimumCharacter() {
        assertEquals(32, daysDictionary.minimumCharacter());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#maximumValueSize()}.
     */
    @Test
    public void testMaximumValueSize() {
        assertEquals(42, daysDictionary.maximumValueSize());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#wordCount()}.
     */
    @Test
    public void testWordCount() {
        assertEquals(11, daysDictionary.wordCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#fsaStateCount()}.
     */
    @Test
    public void testFsaStateCount() {
        assertEquals(33, daysDictionary.fsaStateCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#fsaAcceptStateCount()}.
     */
    @Test
    public void testFsaAcceptStateCount() {
        assertEquals(0, daysDictionary.fsaAcceptStateCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#fsaEdgeCount()}.
     */
    @Test
    public void testFsaEdgeCount() {
        assertEquals(40, daysDictionary.fsaEdgeCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#fsaAcceptEdgeCount()}.
     */
    @Test
    public void testFsaAcceptEdgeCount() {
        assertEquals(6, daysDictionary.fsaAcceptEdgeCount());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getData()}.
     */
    @Test
    public void testGetData() {
        // how to test that it is a copy?
        assertFalse(daysData == daysDictionary.getData());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#creationDate()}.
     */
    @Test
    public void testCreationDate() {
        Date cd = daysDictionary.creationDate();
        assertNotNull(cd);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(cd);
        // We don't want to check exact dates because LE/BE binaries aren't
        // built at the same exact time.
        assertTrue(2009 <= calendar.get(Calendar.YEAR));
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#getMetadata()}.
     * @throws Take5Exception 
     * @throws IOException 
     */
    @Test
    public void testGetMetadata() throws IOException, Take5Exception {
        Take5Dictionary tsDict = openDictionary("test_dicts/trie_strings", null);
        Map<String, String> md = tsDict.getMetadata();
        String euro = md.get("euro");
        assertEquals("\u20ac", euro);
        
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#setSkipBits(byte[])}.
     * @throws Take5Exception 
     * @throws IOException 
     */
    @Test
    public void testSetSkipBits() throws IOException, Take5Exception {
        byte[] skipBits = new byte[8192];
        char c = 'x';
        skipBits[c >> 3] |= 1 << (c & 7);
        
        Take5Dictionary tsDict = openDictionary("test_dicts/trie_strings", null);
        byte[] oldSkipBits = tsDict.setSkipBits(skipBits);
        assertNull(oldSkipBits);
        Take5Match match = tsDict.matchLongest("tryixxxx");
        assertEquals(3, match.getLength());
        assertEquals(3, match.getIndex());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#matchExact(java.lang.String)}.
     * @throws Take5Exception 
     */
    @Test
    public void testMatchExact() throws Take5Exception {
        Take5Match match = daysDictionary.matchExact("Wednesday");
        assertNotNull(match);
        assertEquals(9, match.getIndex());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#matchLongest(java.lang.String)}.
     * @throws Take5Exception 
     */
    @Test
    public void testMatchLongest() throws Take5Exception {
        Take5Match match = daysDictionary.matchLongest("Sund");
        assertEquals(3, match.getLength()); // the matching word is Sun
        assertEquals("yellow", match.getStringValue());
    }

    /**
     * Test method for 
     * {@link com.basistech.internal.take5.Take5Dictionary#matchMultiple(java.lang.String, 
     *        com.basistech.internal.take5.Take5Match[])}.
     * @throws Take5Exception 
     */
    @Test
    public void testMatchMultiple() throws Take5Exception {
        Take5Match[] matches = new Take5Match[10];
        int count = daysDictionary.matchMultiple("Sund", matches);
        // not a very interesting test.
        assertEquals(1, count);
    }

    /**
     * Test method for 
     * {@link com.basistech.internal.take5.Take5Dictionary#take5Search(char[], 
     *        com.basistech.internal.take5.Take5Match, com.basistech.internal.take5.Take5Match[])}.
     * @throws Take5Exception 
     */
    @Test
    public void testTake5SearchCharArrayTake5MatchTake5MatchArray() throws Take5Exception {
        Take5Match match = new Take5Match();
        int count = daysDictionary.take5Search("Sund".toCharArray(), match, null);
        assertEquals(1, count);
        assertEquals(3, match.getLength());
    }

    /**
     * Test method for {@link com.basistech.internal.take5.Take5Dictionary#nextLetters(int, int)}.
     * @throws Take5Exception 
     * @throws IOException 
     */
    @Test
    public void testNextLetters() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("test_dicts/next_letters", null);
        Take5Match startMatch = dict.getStartMatch();
        Take5Match[] matches = dict.nextLetters(startMatch.getState(), startMatch.getIndex());
        assertNotNull(matches);
    }

    /**
     * Test method for {@link com.basistech.internal.take5.
     *  Take5Dictionary#advanceByChar(com.basistech.internal.take5.Take5Match, char)}.
     */
    @Test
    @org.junit.Ignore
    public void testAdvanceByChar() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.basistech.internal.take5.
     * Take5Dictionary#take5Search(char[], com.basistech.internal.take5.Take5Match, 
     * com.basistech.internal.take5.Take5Match[], int, int)}.
     */
    @Test
    @org.junit.Ignore
    public void testTake5SearchCharArrayTake5MatchTake5MatchArrayIntInt() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testGetComplexData() throws IOException, Take5Exception {
        Take5Dictionary dict = openDictionary("test_dicts/mixed", null);
        Take5Match match = dict.matchExact("key0");
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
        Take5Dictionary dict = openDictionary("test_dicts/mixed", null);
        Take5Match match = dict.matchExact("key0");
        int offset = match.getOffsetValue();
        ByteBuffer data = dict.getData();
        data.position(offset);
        ByteBuffer bytes = data.slice();
        data.position(offset + 8);
        FloatBuffer floats = data.asFloatBuffer();
        checkKey0Values(bytes, floats);
    }
}
