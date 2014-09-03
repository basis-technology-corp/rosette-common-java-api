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

//CHECKSTYLE:OFF
// This code is a bit too quirky to clean up.
package com.basistech.internal.take5;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * This class implements loading and usage of a Take5 binary dictionary.
 * @deprecated
 */
// CHECKSTYLE:OFF
@SuppressWarnings({"PMD", "deprecation"})
@Deprecated
public class Take5Dictionary {
    /* Binary format version numbers. */
    private static final int VERSION_5_0 = 0x00000500;
    private static final int VERSION_5_1 = 0x00000501;
    private static final int VERSION_5_2 = 0x00000502;
    private static final int VERSION_5_3 = 0x00000503;
    private static final int VERSION_MINIMUM = VERSION_5_0;

    /* Versions supported by runtime. */
    public static final int RT_MIN_VERSION = VERSION_5_0;
    public static final int RT_MAX_VERSION = VERSION_5_3;

    ByteBuffer data; /* Binary dictionary data. */
    long dataSize; /* Size of dictionary data. */
    byte[] skipBits; /* Characters to skip. */

    int valueData; /* Beginning of value data section. */
    int valueFormat; /* Value data format. */
    int valueSize; /* Value data size. */

    int fsaData; /* Beginning of FSA data section. */
    int fsaEngine; /* FSA engine type. */

    int minVersion; /* Minimum engine supported. */
    int fileVersion; /* Maximum engine supported. */
    int contentMinVersion; /* Minimum content version. */
    int contentMaxVersion; /* Maximum content version. */
    int flags; /* Engine flags. */
    int contentFlags; /* Content flags. */
    String copyrightString; /* Copyright string. */
    int maxMatches; /* Maximum number of matches to expect. */
    int maxWordLength; /* Length of longest word. */
    int minCharacter; /* Minimum character value. */
    int maxCharacter; /* Maximum character value. */
    int maxValueSize; /* Maximum value size, if storing values. */
    int buildDay; /* Day dictionary was built. */
    int buildMsec; /* Time dictionary was built. */
    int wordCount; /* Words recognized. */
    int stateCount; /* Number of FSA states. */
    int acceptStateCount; /* Number of accept states. */
    int edgeCount; /* Number of graph edges. */
    int acceptEdgeCount; /* Number of accepting edges. */

    int stateStart; /* State to start in. */
    int indexStart; /* Index value to start with. */

    private Map<String, String> metadata;

    /**
     * Initialize a Take5Dictionary object to match against the dictionary stored in the given ByteBuffer.
     * 
     * @throws Take5Exception if there is a problem with the given input.
     */
    public Take5Dictionary(ByteBuffer fileData, long fileSize) throws Take5Exception {
        data = fileData.duplicate();
        dataSize = fileSize;
        skipBits = null;
        metadata = new HashMap<String, String>();
        readHeader();
    }

    /**
     * Get copyright notices attached to the dictionary.
     */
    public String getCopyright() {
        return copyrightString;
    }

    /**
     * Get content flags set on the dictionary.
     */
    public int getContentFlags() {
        return contentFlags;
    }

    /**
     * Get the minimum version supported by the content of this dictionary.
     */
    public int getMinimumContentVersion() {
        return contentMinVersion;
    }

    /**
     * Get the maximum version supported by the content of this dictionary.
     */
    public int getMaximumContentVersion() {
        return contentMaxVersion;
    }

    /**
     * The maximum number of matches a request for multiple matches can return. If this is not known a value
     * of -1 is returned.
     */
    public int maximumMatchCount() {
        return maxMatches;
    }

    /**
     * The maximum length of a word in the dictionary. If the language is infinite this is -1.
     */
    public int maximumWordLength() {
        return maxWordLength;
    }

    /**
     * The highest value character stored in the dictionary.
     */
    public char maximumCharacter() {
        return (char)maxCharacter;
    }

    /**
     * The lowest value character stored in the dictionary.
     */
    public char minimumCharacter() {
        return (char)minCharacter;
    }

    /**
     * Maximum size of a value, zero if there are no values stored.
     */
    public int maximumValueSize() {
        return maxValueSize;
    }

    /**
     * Number of words in the dictionary, -1 if the language is infinite or this value is unknown.
     */
    public int wordCount() {
        return wordCount;
    }

    /**
     * Number of states in the dictionary's FSA.
     */
    public int fsaStateCount() {
        return stateCount;
    }

    /**
     * Number of accept states in the dictionary's FSA.
     */
    public int fsaAcceptStateCount() {
        return acceptStateCount;
    }

    /**
     * Number of edges in the dictionary's FSA.
     */
    public int fsaEdgeCount() {
        return edgeCount;
    }

    /**
     * Number of accept edges in the dictionary's FSA.
     */
    public int fsaAcceptEdgeCount() {
        return acceptEdgeCount;
    }

    /**
     * For complex payload, the accessors in the Take5Match cannot do all the decomposition. The caller has to
     * get an offset and fend for itself.
     * 
     * @return the entire take5.
     */
    public ByteBuffer getData() {
        ByteBuffer buffer = data.asReadOnlyBuffer();
        buffer.position(0);
        buffer.limit(buffer.capacity());
        buffer.order(data.order());
        return buffer;
    }

    /**
     * Date and time this dictionary was generated.
     */
    public Date creationDate() {
        // buildDay = (YYYY * 12 + MM) * 31 + DD;
        int day = buildDay % 31;
        int month = (buildDay - day) / 31 % 12;
        int year = ((buildDay - day) / 31 - month) / 12;
        // buildMsec = the number of milliseconds since midnight on buildDay
        int msec = buildMsec % 1000;
        int second = (buildMsec - msec) / 1000 % 60;
        int minute = (buildMsec - msec - second * 1000) / 60000 % 60;
        int hour = (buildMsec - msec - second * 1000 - minute * 60000) / 3600000;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.set(year, month, day + 1, hour, minute, second);
        return cal.getTime();
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Set a list of characters to skip over when preforming a match. This takes the form of an array of
     * exactly 8192 elements such that a character is skipped if skipBits[c >> 3] & (1 << (c & 7)) is
     * non-zero. Character skipping can be completely disabled by passing null. The previous skipBits array is
     * returned.
     */
    public byte[] setSkipBits(byte[] skipBits) {
        if (skipBits != null && skipBits.length != 8192) {
            throw new IllegalArgumentException("skipBits must have exactly 8192 elements.");
        }

        byte[] previous = this.skipBits;
        this.skipBits = skipBits;
        return previous;
    }

    /**
     * Searches for a match in the dictionary of exactly the string provided. If such a match is found the
     * associated Take5Match object is returned, otherwise null. The behavior of this function when a non-zero
     * skipBits array is specified is undefined.
     */
    public Take5Match matchExact(String data) throws Take5Exception {
        Take5Match match = new Take5Match();
        int c = take5Search(data.toCharArray(), match, null);
        if (c == 0) {
            return null;
        }
        if (match.length == data.length()) {
            return match;
        }
        return null;
    }

    /**
     * Searches for the longest string matching the given data. The Take5Match object associated with that
     * string is returned, otherwise null.
     */
    public Take5Match matchLongest(String data) throws Take5Exception {
        Take5Match match = new Take5Match();
        int c = take5Search(data.toCharArray(), match, null);
        if (c == 0) {
            return null;
        }
        return match;
    }

    /**
     * Finds all possible matches for prefixes of the given string. Up to the length of matches results are
     * generated.
     * 
     * @return the number of valid results.
     */
    public int matchMultiple(String data, Take5Match[] matches) throws Take5Exception {
        int c = take5Search(data.toCharArray(), null, matches);
        return c > matches.length ? matches.length : c;
    }

    // NOTE: Keep this up to date.
    private static final int HEADER_SIZE = 32;

    private static final int FLAG_LITTLE_ENDIAN = 0x01;
    @SuppressWarnings("unused")
    private static final int FLAG_OBFUSCATION = 0x02;
    @SuppressWarnings("unused")
    private static final int FLAG_LOOKUP_AUTOMATON = 0x04;

    @SuppressWarnings("unused")
    private static final int ENGINE_TAKE3 = 0x00000001;
    private static final int ENGINE_SEARCH = 0x00000002;

    static final int VALUE_FORMAT_NONE = 0x01000000;
    static final int VALUE_FORMAT_INDEX = 0x02000000;
    static final int VALUE_FORMAT_FIXED = 0x03000000;
    static final int VALUE_FORMAT_INDIRECT = 0x04000000;

    /**
     * Reads the header of the given data file.
     */
    private void readHeader() throws Take5Exception {
        if (dataSize < HEADER_SIZE) {
            throw new Take5Exception(Take5Exception.FILE_TOO_SHORT);
        }

        data.order(ByteOrder.nativeOrder());

        if (!(data.get() == 'T' && data.get() == '4' && data.get() == 'K' && data.get() == '3')) {
            throw new Take5Exception(Take5Exception.BAD_DATA);
        }

        fileVersion = data.getInt();
        contentMaxVersion = data.getInt();
        flags = data.getInt();
        contentFlags = data.getInt();
        minVersion = data.getInt();
        contentMinVersion = data.getInt();
        data.getInt(); /* required_alignment */
        int total_size = data.getInt();
        int copyright_string = data.getInt();
        int copyright_size = data.getInt();
        valueData = data.getInt();
        valueFormat = data.getInt();
        fsaData = data.getInt();
        fsaEngine = data.getInt();
        wordCount = data.getInt();
        stateCount = data.getInt();
        acceptStateCount = data.getInt();
        edgeCount = data.getInt();
        acceptEdgeCount = data.getInt();

        // Reverse bytes from network order fields.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            fileVersion = reverseBytes(fileVersion);
            flags = reverseBytes(flags);
            minVersion = reverseBytes(minVersion);
        }

        // Check byte order.
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN != ((flags & FLAG_LITTLE_ENDIAN) != 0)) {
            throw new Take5Exception(Take5Exception.WRONG_BYTE_ORDER);
        }

        // Check version numbers.
        if (fileVersion < VERSION_MINIMUM) {
            throw new Take5Exception(Take5Exception.FILE_TOO_OLD);
        }
        if (fileVersion < minVersion) {
            throw new Take5Exception(Take5Exception.BAD_DATA);
        }
        if (fileVersion > RT_MAX_VERSION) {
            fileVersion = RT_MAX_VERSION;
        }
        if (minVersion < RT_MIN_VERSION) {
            minVersion = RT_MIN_VERSION;
        }
        if (fileVersion < RT_MIN_VERSION) {
            throw new Take5Exception(Take5Exception.FILE_TOO_OLD);
        }
        if (minVersion > RT_MAX_VERSION) {
            throw new Take5Exception(Take5Exception.FILE_TOO_NEW);
        }

        // Check file size.
        if (total_size > dataSize) {
            throw new Take5Exception(Take5Exception.FILE_TOO_SHORT);
        }
        dataSize = total_size;

        // Extract copyright string.
        if (copyright_string != 0) {
            byte[] string = new byte[copyright_size];
            for (int i = 0; i < copyright_size; i++) {
                string[i] = data.get(copyright_string + i);
            }
            copyrightString = new String(string);
        }

        // Version 5.1 specific fields.
        if (fileVersion >= VERSION_5_1) {
            maxMatches = data.getInt();
        } else {
            maxMatches = 0;
        }

        // Version 5.2 specific fields.
        if (fileVersion >= VERSION_5_2) {
            maxWordLength = data.getInt();
            maxValueSize = data.getInt();
            minCharacter = data.getInt();
            maxCharacter = data.getInt();
            buildDay = data.getInt();
            buildMsec = data.getInt();
        } else {
            maxWordLength = 0;
            maxValueSize = -1;
            minCharacter = 0x0000;
            maxCharacter = 0xFFFF;
            buildDay = 0;
            buildMsec = 0;
        }

        if (fileVersion >= VERSION_5_3) {
            int metadata_string = data.getInt();
            int metadata_size = data.getInt();
            if (metadata_size > 0) {
                // byte order?
                ByteBuffer metadataSubset = data.duplicate();
                metadataSubset.position(metadata_string);
                metadataSubset.limit(metadata_string + 2 * metadata_size);
                metadataSubset.order(ByteOrder.nativeOrder());
                CharBuffer charified = metadataSubset.asCharBuffer();
                // this has embedded nulls, now we get to parse them out.
                while (charified.hasRemaining()) {
                    StringBuilder builder = new StringBuilder();
                    char c;
                    while (0 != (c = charified.get())) {
                        builder.append(c);
                    }
                    String key = builder.toString();
                    builder = new StringBuilder();
                    while (0 != (c = charified.get())) {
                        builder.append(c);
                    }
                    String value = builder.toString();
                    metadata.put(key, value);
                }
                metadata = Collections.unmodifiableMap(metadata);
            }
        }

        // Initialize value reading.
        switch (valueFormat & 0xFF000000) {
        default:
            throw new Take5Exception(Take5Exception.UNSUPPORTED_VALUE_FORMAT);
        case VALUE_FORMAT_NONE:
            valueSize = 0;
            break;
        case VALUE_FORMAT_INDEX:
            valueSize = 4;
            break;
        case VALUE_FORMAT_FIXED:
        case VALUE_FORMAT_INDIRECT:
            valueSize = valueFormat & 0x00FFFFFF;
            break;
        }

        // Initialize engine data.
        if (fsaEngine != ENGINE_SEARCH) {
            throw new Take5Exception(Take5Exception.UNSUPPORTED_ENGINE);
        }
        take5SearchInitialize();
    }
    
    /**
     * To use take5Search or nextLetters, use this to obtain starting values.
     * @return a new match object.
     */
    public Take5Match getStartMatch() {
        Take5Match result = new Take5Match();
        take5SearchInitialize();
        result.index = indexStart;
        result.state = stateStart;
        return result;
    }

    /**
     * Reads the take5 specific portion of the header.
     */
    private void take5SearchInitialize() {
        // Read FSA data.
        stateStart = data.getInt(fsaData);
        indexStart = data.getInt(fsaData + 4);
    }

    private static final int SEARCH_TYPE_BINARY = 0;
    private static final int SEARCH_TYPE_LINEAR = 17;
    private static final int SEARCH_TYPE_CHOICE = 34;
    private static final int SEARCH_TYPE_DISPATCH = 51;
    private static final int SEARCH_TYPE_LINEAR_MANY = 68;

    /**
     * Perform a take5 dictionary search for the given input string. If match is not null then a search for
     * the longest match is performed, the length of that match is returned. If matches is not null then a
     * search for multiple matches, up to the length of matches, is performed with the number of successful
     * matches returned. If none or both of match and matches are null then an IllegalArgumentException is
     * thrown.
     * 
     * @return the number of matches found.
     * @throws Take5Exception
     */
    int take5Search(char[] input, Take5Match match, Take5Match[] matches) throws Take5Exception {
        return take5Search(input, match, matches, stateStart, indexStart);
    }

    public Take5Match[] nextLetters(int state, int index) throws Take5Exception {

        int ptr;

        ArrayList<Take5Match> res = new ArrayList<Take5Match>();

        // Check if we have just traversed an accept arc.
        if ((state & 1) != 0) {
            // The actual state we want to be in must be even.
            state &= -2;
        }
        ptr = state;

        // Switch on the type code.
        int type = data.getShort(ptr);

        // TODO: Implement for dispatch...
        if (type >= SEARCH_TYPE_BINARY && type < SEARCH_TYPE_LINEAR) {
            int power = type - SEARCH_TYPE_BINARY;
            for (int i = 0; i < Math.pow(2, power); ++i) {
                if (data.get(ptr + 2) == -1) {
                    break;
                }
                char c = data.getChar(ptr += 2);
                int edge = state + 4 * (state - ptr); // this will be less than
                // state
                int nstate = state + data.getInt(edge);
                int nindex = index + data.getInt(edge + 4);
                Take5Match newM = new Take5Match();
                newM.dict = this;
                newM.length = 0;
                newM.state = nstate;
                newM.index = nindex;
                newM.c = c;
                res.add(newM);
            }
        } else if (type >= SEARCH_TYPE_LINEAR && type < SEARCH_TYPE_DISPATCH) {
            int len = type - SEARCH_TYPE_LINEAR;
            for (int i = 0; i < len; ++i) {
                if (data.get(ptr + 2) == -1) {
                    break;
                }
                char c = data.getChar(ptr += 2);
                int edge = state + 4 * (state - ptr); // this will be less than
                // state
                int nstate = state + data.getInt(edge);
                int nindex = index + data.getInt(edge + 4);
                Take5Match newM = new Take5Match();
                newM.dict = this;
                newM.length = 0;
                newM.state = nstate;
                newM.index = nindex;
                newM.c = c;
                res.add(newM);
            }
        } else if (type == SEARCH_TYPE_LINEAR_MANY) {
            for (int i = 0; i < 1000000000; ++i) {
                if (data.get(ptr + 2) == -1) {
                    break;
                }
                char c = data.getChar(ptr += 2);
                int edge = state + 4 * (state - ptr); // this will be less than
                // state
                int nstate = state + data.getInt(edge);
                int nindex = index + data.getInt(edge + 4);
                Take5Match newM = new Take5Match();
                newM.dict = this;
                newM.length = 0;
                newM.state = nstate;
                newM.index = nindex;
                newM.c = c;
                res.add(newM);
            }
        } else {
            throw new Take5Exception(Take5Exception.UNSUPPORTED_STATE_TYPE);
        }

        Take5Match[] ret = new Take5Match[res.size()];
        res.toArray(ret);
        return ret;
    }

    public Take5Match advanceByChar(Take5Match m, char c) throws Take5Exception {
        char[] cA = new char[1];
        cA[0] = c;
        Take5Match ret = new Take5Match();
        take5Search(cA, ret, null, m.state, m.index);
        if (ret.length == 0) {
            return null;
        }
        return ret;
    }

    int take5Search(char[] input, Take5Match match, Take5Match[] matches, int startState, int startIndex)
        throws Take5Exception {
        int state = startState, ptr;
        int index = startIndex;
        int cnt, out_cnt = 0;
        char c;

        if (input == null) {
            throw new IllegalArgumentException("'input' must not be null");
        }
        if (match == null == (matches == null)) {
            throw new IllegalArgumentException("'match' and 'matches' are mutually exclusive");
        }

        loop: for (cnt = 0; cnt < input.length; cnt++) {
            c = input[cnt];

            // Check value in skipBits.
            if (skipBits != null && (skipBits[c >> 3] & 1 << (c & 7)) != 0) {
                continue;
            }

            // Check if we have just traversed an accept arc.
            if ((state & 1) != 0) {
                // The actual state we want to be in must be even.
                state &= -2;
                if (match != null) {
                    match.length = cnt;
                    match.index = index;
                    match.dict = this;
                    match.state = state;
                } else if (matches != null && out_cnt < matches.length) {
                    matches[out_cnt] = new Take5Match();
                    matches[out_cnt].length = cnt;
                    matches[out_cnt].index = index;
                    matches[out_cnt].dict = this;
                }
                out_cnt++;
            }

            ptr = state;

            // Switch on the type code.
            switch (data.getShort(ptr)) {
            default:
                throw new Take5Exception(Take5Exception.UNSUPPORTED_STATE_TYPE);

                // Extended linear search.
            case SEARCH_TYPE_LINEAR_MANY:
                while (c > data.getChar(ptr += 2)) {
                    ;
                }
                break;

            case SEARCH_TYPE_DISPATCH + 16:
                if (c > data.getChar(ptr + (3 << 16) - 4)) {
                    ptr += 3 << 16;
                }
            case SEARCH_TYPE_DISPATCH + 15:
                if (c > data.getChar(ptr + (3 << 15) - 4)) {
                    ptr += 3 << 15;
                }
            case SEARCH_TYPE_DISPATCH + 14:
                if (c > data.getChar(ptr + (3 << 14) - 4)) {
                    ptr += 3 << 14;
                }
            case SEARCH_TYPE_DISPATCH + 13:
                if (c > data.getChar(ptr + (3 << 13) - 4)) {
                    ptr += 3 << 13;
                }
            case SEARCH_TYPE_DISPATCH + 12:
                if (c > data.getChar(ptr + (3 << 12) - 4)) {
                    ptr += 3 << 12;
                }
            case SEARCH_TYPE_DISPATCH + 11:
                if (c > data.getChar(ptr + (3 << 11) - 4)) {
                    ptr += 3 << 11;
                }
            case SEARCH_TYPE_DISPATCH + 10:
                if (c > data.getChar(ptr + (3 << 10) - 4)) {
                    ptr += 3 << 10;
                }
            case SEARCH_TYPE_DISPATCH + 9:
                if (c > data.getChar(ptr + (3 << 9) - 4)) {
                    ptr += 3 << 9;
                }
            case SEARCH_TYPE_DISPATCH + 8:
                if (c > data.getChar(ptr + (3 << 8) - 4)) {
                    ptr += 3 << 8;
                }
            case SEARCH_TYPE_DISPATCH + 7:
                if (c > data.getChar(ptr + (3 << 7) - 4)) {
                    ptr += 3 << 7;
                }
            case SEARCH_TYPE_DISPATCH + 6:
                if (c > data.getChar(ptr + (3 << 6) - 4)) {
                    ptr += 3 << 6;
                }
            case SEARCH_TYPE_DISPATCH + 5:
                if (c > data.getChar(ptr + (3 << 5) - 4)) {
                    ptr += 3 << 5;
                }
            case SEARCH_TYPE_DISPATCH + 4:
                if (c > data.getChar(ptr + (3 << 4) - 4)) {
                    ptr += 3 << 4;
                }
            case SEARCH_TYPE_DISPATCH + 3:
                if (c > data.getChar(ptr + (3 << 3) - 4)) {
                    ptr += 3 << 3;
                }
            case SEARCH_TYPE_DISPATCH + 2:
                if (c > data.getChar(ptr + (3 << 2) - 4)) {
                    ptr += 3 << 2;
                }
            case SEARCH_TYPE_DISPATCH + 1:
                if (c > data.getChar(ptr + (3 << 1) - 4)) {
                    ptr += 3 << 1;
                }
            case SEARCH_TYPE_DISPATCH:
                if (c > data.getChar(ptr + 2)) {
                    break loop;
                }
                if (data.getChar(ptr + 2) - c >= data.getChar(ptr + 4)) {
                    break loop;
                }
                c -= data.getChar(ptr + 6);
                int edge = state - 4 * (c + 1);
                state += data.getInt(edge);
                index += data.getInt(edge + 4);
                continue;

                // Linear search.
            case SEARCH_TYPE_LINEAR + 16:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 15:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 14:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 13:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 12:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 11:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 10:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 9:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 8:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 7:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 6:
            case SEARCH_TYPE_CHOICE + 6:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 5:
            case SEARCH_TYPE_CHOICE + 5:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 4:
            case SEARCH_TYPE_CHOICE + 4:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 3:
            case SEARCH_TYPE_CHOICE + 3:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 2:
            case SEARCH_TYPE_CHOICE + 2:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR + 1:
            case SEARCH_TYPE_CHOICE + 1:
                if (c <= data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_LINEAR:
            case SEARCH_TYPE_CHOICE:
                break loop;
            // Binary search.
            case SEARCH_TYPE_BINARY + 16:
                if (c > data.getChar(ptr + (1 << 16))) {
                    ptr += 1 << 16;
                }
            case SEARCH_TYPE_BINARY + 15:
                if (c > data.getChar(ptr + (1 << 15))) {
                    ptr += 1 << 15;
                }
            case SEARCH_TYPE_BINARY + 14:
                if (c > data.getChar(ptr + (1 << 14))) {
                    ptr += 1 << 14;
                }
            case SEARCH_TYPE_BINARY + 13:
                if (c > data.getChar(ptr + (1 << 13))) {
                    ptr += 1 << 13;
                }
            case SEARCH_TYPE_BINARY + 12:
                if (c > data.getChar(ptr + (1 << 12))) {
                    ptr += 1 << 12;
                }
            case SEARCH_TYPE_BINARY + 11:
                if (c > data.getChar(ptr + (1 << 11))) {
                    ptr += 1 << 11;
                }
            case SEARCH_TYPE_BINARY + 10:
                if (c > data.getChar(ptr + (1 << 10))) {
                    ptr += 1 << 10;
                }
            case SEARCH_TYPE_BINARY + 9:
                if (c > data.getChar(ptr + (1 << 9))) {
                    ptr += 1 << 9;
                }
            case SEARCH_TYPE_BINARY + 8:
                if (c > data.getChar(ptr + (1 << 8))) {
                    ptr += 1 << 8;
                }
            case SEARCH_TYPE_BINARY + 7:
                if (c > data.getChar(ptr + (1 << 7))) {
                    ptr += 1 << 7;
                }
            case SEARCH_TYPE_BINARY + 6:
                if (c > data.getChar(ptr + (1 << 6))) {
                    ptr += 1 << 6;
                }
            case SEARCH_TYPE_BINARY + 5:
                if (c > data.getChar(ptr + (1 << 5))) {
                    ptr += 1 << 5;
                }
            case SEARCH_TYPE_CHOICE + 9:
            case SEARCH_TYPE_CHOICE + 10:
            case SEARCH_TYPE_CHOICE + 11:
            case SEARCH_TYPE_CHOICE + 12:
            case SEARCH_TYPE_CHOICE + 13:
            case SEARCH_TYPE_CHOICE + 14:
            case SEARCH_TYPE_CHOICE + 15:
            case SEARCH_TYPE_CHOICE + 16:
            case SEARCH_TYPE_BINARY + 4:
                if (c > data.getChar(ptr + (1 << 4))) {
                    ptr += 1 << 4;
                }
            case SEARCH_TYPE_CHOICE + 7:
            case SEARCH_TYPE_CHOICE + 8:
            case SEARCH_TYPE_BINARY + 3:
                if (c > data.getChar(ptr + (1 << 3))) {
                    ptr += 1 << 3;
                }
            case SEARCH_TYPE_BINARY + 2:
                if (c > data.getChar(ptr + (1 << 2))) {
                    ptr += 1 << 2;
                }
            case SEARCH_TYPE_BINARY + 1:
                if (c == data.getChar(ptr += 2)) {
                    break;
                }
            case SEARCH_TYPE_BINARY:
                if (c == data.getChar(ptr += 2)) {
                    break;
                }
            }

            if (c != data.getChar(ptr)) {
                break loop;
            }
            int edge = state + 4 * (state - ptr); // this will be less than
            // state
            state += data.getInt(edge);
            index += data.getInt(edge + 4);
        }

        if ((state & 1) != 0) {
            if (match != null) {
                match.length = cnt;
                match.index = index;
                match.dict = this;
                match.state = state;
            } else if (matches != null && out_cnt < matches.length) {
                matches[out_cnt] = new Take5Match();
                matches[out_cnt].length = cnt;
                matches[out_cnt].index = index;
                matches[out_cnt].dict = this;
            }
            out_cnt++;
        }

        return out_cnt;
    }

    /**
     * Byte-swap the given integer.
     */
    private static int reverseBytes(int i) {
        return i << 24 | i << 8 & 0xff0000 | i >> 8 & 0xff00 | i >> 24 & 0xff;
    }
}
