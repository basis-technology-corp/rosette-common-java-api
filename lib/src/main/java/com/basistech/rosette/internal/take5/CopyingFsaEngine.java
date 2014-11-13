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

package com.basistech.rosette.internal.take5;

import java.nio.ByteBuffer;

/**
 * FSA engine with a copy.
 */
//CHECKSTYLE:OFF
class CopyingFsaEngine implements FsaGuts {
    final ByteBuffer data;
    final char[] charData;
    final int guardIndexDelta;

    public CopyingFsaEngine(ByteBuffer data, char[] charData, int guardIndexDelta) {
        this.data = data;
        this.charData = charData;
        this.guardIndexDelta = guardIndexDelta;
    }

    /**
     * Count the number of transitons out of the given state.
     */
    @Override
    public int edgeCount(int state) {
        state &= -2;            // clear accept bit
        int ptr = state;
        int type = data.getShort(ptr);
        if (type < 0) {
            throw new Take5RuntimeException(Take5Exception.UNSUPPORTED_STATE_TYPE);
        } else if (type == Take5Dictionary.SEARCH_TYPE_BINARY + 0) {
            return 1;
        } else if (type == Take5Dictionary.SEARCH_TYPE_BINARY + 1) {
            return 2;
        } else if (type < Take5Dictionary.SEARCH_TYPE_BINARY_END) {
            assert(Take5Dictionary.SEARCH_TYPE_BINARY == 0);
            // Skip searching the bottom half of the space since we know
            // the final character must be in the top half.
            ptr += (1 << (type - Take5Dictionary.SEARCH_TYPE_BINARY));
            switch (type) {
                case Take5Dictionary.SEARCH_TYPE_BINARY + 16:
                    if (charData[(ptr + (1 << 15)) >> 1] != '\uFFFF') {
                        ptr += (1 << 15);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 15:
                    if (charData[(ptr + (1 << 14)) >> 1] != '\uFFFF') {
                        ptr += (1 << 14);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 14:
                    if (charData[(ptr + (1 << 13)) >> 1] != '\uFFFF') {
                        ptr += (1 << 13);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 13:
                    if (charData[(ptr + (1 << 12)) >> 1] != '\uFFFF') {
                        ptr += (1 << 12);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 12:
                    if (charData[(ptr + (1 << 11)) >> 1] != '\uFFFF') {
                        ptr += (1 << 11);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 11:
                    if (charData[(ptr + (1 << 10)) >> 1] != '\uFFFF') {
                        ptr += (1 << 10);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 10:
                    if (charData[(ptr + (1 << 9)) >> 1] != '\uFFFF') {
                        ptr += (1 << 9);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 9:
                    if (charData[(ptr + (1 << 8)) >> 1] != '\uFFFF') {
                        ptr += (1 << 8);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 8:
                    if (charData[(ptr + (1 << 7)) >> 1] != '\uFFFF') {
                        ptr += (1 << 7);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 7:
                    if (charData[(ptr + (1 << 6)) >> 1] != '\uFFFF') {
                        ptr += (1 << 6);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 6:
                    if (charData[(ptr + (1 << 5)) >> 1] != '\uFFFF') {
                        ptr += (1 << 5);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 5:
                    if (charData[(ptr + (1 << 4)) >> 1] != '\uFFFF') {
                        ptr += (1 << 4);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 4:
                    if (charData[(ptr + (1 << 3)) >> 1] != '\uFFFF') {
                        ptr += (1 << 3);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 3:
                    if (charData[(ptr + (1 << 2)) >> 1] != '\uFFFF') {
                        ptr += (1 << 2);
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 2:
                    if (charData[(ptr + 2) >> 1] == '\uFFFF') {
                        ptr += 2;
                    } else {
                        ptr += 4;
                    }
                    break;
            }
        } else if (type < Take5Dictionary.SEARCH_TYPE_LINEAR_END) {
            assert(Take5Dictionary.SEARCH_TYPE_LINEAR == Take5Dictionary.SEARCH_TYPE_BINARY_END);
            return type - Take5Dictionary.SEARCH_TYPE_LINEAR;
        } else if (type < Take5Dictionary.SEARCH_TYPE_CHOICE_END) {
            assert(Take5Dictionary.SEARCH_TYPE_CHOICE == Take5Dictionary.SEARCH_TYPE_LINEAR_END);
            return type - Take5Dictionary.SEARCH_TYPE_CHOICE;
        } else if (type == Take5Dictionary.SEARCH_TYPE_LINEAR_MANY) {
            while (charData[(ptr += 2) >> 1] != '\uFFFF') {
                ;
            }
        } else {
            throw new Take5RuntimeException(Take5Exception.UNSUPPORTED_STATE_TYPE);
        }
        /* If we get here, ptr points to the last character, but that may,
           or may not, be a guard edge. */
        int i = (ptr - state) >> 1;
        if (data.getInt(state - (8 * i) + 4) == guardIndexDelta) {
            return i - 1;
        } else {
            return i;
        }
    }

    @Override
    public int take5SearchInternal(Take5Dictionary dict,
                                   char[] input, int offset, int length,
                                   Take5Match match, Take5Match[] matches,
                                   int startState, int startIndex) {
        int state = startState;
        int index = startIndex;
        int out_cnt = 0;
        int ptr;
        int cnt;
        char c;
        boolean squeezing = false;

        if (dict.fsaEngine != Take5Dictionary.ENGINE_SEARCH) {
            throw new UnsupportedOperationException("This Take5Dictionary is not a state machine.");
        }

        if (input == null) {
            throw new IllegalArgumentException("'input' must not be null");
        }
        if ((match == null) == (matches == null)) {
            throw new IllegalArgumentException("'match' and 'matches' are mutually exclusive");
        }

        if (match != null) {
            match.dict = dict;
        }

        int limit = offset + length;
        loop: for (cnt = offset; cnt < limit; cnt++) {
            c = input[cnt];

            // If we're squeezing spaces, then we skip all but the first
            // space in a run of spaces.
            if (dict.squeezeSpaces) {
                if (c == 0x0020) {
                    if (squeezing) continue;
                    squeezing = true;
                } else {
                    squeezing = false;
                }
            }

            // Check value in skipBits.
            if (dict.skipBits != null && (dict.skipBits[c >> 3] & 1 << (c & 7)) != 0) {
                continue;
            }

            // Check if we have just traversed an accept arc.
            if ((state & 1) != 0) {
                state &= -2;    // clear accept bit
                if (match != null) {
                    match.length = cnt - offset;
                    match.index = index;
                    match.state = state;
                } else if (matches != null && out_cnt < matches.length) {
                    matches[out_cnt] = new Take5Match(dict, cnt - offset, index, state);
                }
                out_cnt++;
            }

            ptr = state;

            // Switch on the type code.
            short stateType = data.getShort(ptr);
            switch (stateType) {
                default:
                    throw new Take5RuntimeException(Take5Exception.UNSUPPORTED_STATE_TYPE,
                            "State: " + Integer.toString(stateType));

                    // Take5Dictionary.SEARCH_TYPE_DISPATCH is depreciated.  The code to
                    // support it has been deleted from here.  The compiler
                    // <EM>never</EM> generated these type codes and enough
                    // people have come to depend on that fact that we have
                    // declared them to be depreciated.  It is therefore
                    // officially safe to depend on never encountering them.

                    // Empty state: In fact the compiler has always generated
                    // ..._LINEAR+0 for an empty state, and that fact is now
                    // <EM>guaranteed</EM>, so we <EM>could</EM> signal an
                    // error if we see ..._CHOICE+0.
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 0:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 0:
                    break loop;

                // Extended linear search:
                case Take5Dictionary.SEARCH_TYPE_LINEAR_MANY:
                    while (c > charData[(ptr += 2) >> 1]) {
                        ;
                    }
                    break;

                // Linear search:
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 16:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 15:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 14:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 13:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 12:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 11:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 10:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 9:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 8:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 7:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 6:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 6:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 5:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 5:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 4:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 4:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 3:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 3:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 2:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 2:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 1:
                    if (c <= charData[(ptr += 2) >> 1]) {
                        break;
                    }
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 1:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 1:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 0:
                    ptr += 2;
                    break;

                // Binary search:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 16:
                    if (c > charData[(ptr + (1 << 16)) >> 1]) {
                        ptr += 1 << 16;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 15:
                    if (c > charData[(ptr + (1 << 15)) >> 1]) {
                        ptr += 1 << 15;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 14:
                    if (c > charData[(ptr + (1 << 14)) >> 1]) {
                        ptr += 1 << 14;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 13:
                    if (c > charData[(ptr + (1 << 13)) >> 1]) {
                        ptr += 1 << 13;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 12:
                    if (c > charData[(ptr + (1 << 12)) >> 1]) {
                        ptr += 1 << 12;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 11:
                    if (c > charData[(ptr + (1 << 11)) >> 1]) {
                        ptr += 1 << 11;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 10:
                    if (c > charData[(ptr + (1 << 10)) >> 1]) {
                        ptr += 1 << 10;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 9:
                    if (c > charData[(ptr + (1 << 9)) >> 1]) {
                        ptr += 1 << 9;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 8:
                    if (c > charData[(ptr + (1 << 8)) >> 1]) {
                        ptr += 1 << 8;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 7:
                    if (c > charData[(ptr + (1 << 7)) >> 1]) {
                        ptr += 1 << 7;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 6:
                    if (c > charData[(ptr + (1 << 6)) >> 1]) {
                        ptr += 1 << 6;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 5:
                    if (c > charData[(ptr + (1 << 5)) >> 1]) {
                        ptr += 1 << 5;
                    }
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 9:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 10:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 11:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 12:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 13:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 14:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 15:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 16:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 4:
                    if (c > charData[(ptr + (1 << 4)) >> 1]) {
                        ptr += 1 << 4;
                    }
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 7:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 8:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 3:
                    if (c > charData[(ptr + (1 << 3)) >> 1]) {
                        ptr += 1 << 3;
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 2:
                    if (c > charData[(ptr + (1 << 2)) >> 1]) {
                        ptr += 1 << 2;
                    }
                    if (c > charData[(ptr += 2) >> 1]) {
                        ptr += 2;
                    }
                    break;
            }

            if (c != charData[ptr >> 1]) {
                break loop;
            }
            int edge = state - 4 * (ptr - state);
            state += data.getInt(edge);
            index += data.getInt(edge + 4);
        }

        if ((state & 1) != 0) {
            state &= -2; // clear accept bit
            if (match != null) {
                match.length = cnt - offset;
                match.index = index;
                match.state = state;
            } else if (matches != null && out_cnt < matches.length) {
                matches[out_cnt] = new Take5Match(dict, cnt - offset, index, state);
            }
            out_cnt++;
        }

        return out_cnt;
    }

    @Override
    public Take5Match[] nextLettersInternal(Take5Dictionary dict, int state, int index, int length) throws Take5Exception {
        if (dict.fsaEngine != Take5Dictionary.ENGINE_SEARCH) {
            throw new UnsupportedOperationException("This Take5Dictionary is not a state machine.");
        }
        state &= -2;        // clear accept bit
        int edges = edgeCount(state);
        Take5Match[] rv = new Take5Match[edges];
        int edge = state;
        int ptr = state;
        Take5Match match;
        for (int i = 0; i < edges; ++i) {
            edge -= 8;
            ptr += 2;
            match = new Take5Match(dict, length,
                    index + data.getInt(edge + 4),
                    state + data.getInt(edge));
            match.c = charData[ptr >> 1];
            rv[i] = match;
        }
        return rv;
    }

    /**
     * Turn an index back into the word that would generate it.
     *
     * @param index the index of the desired word
     * @param buffer array of chars to contain the word
     * @return the length of the word
     * @throws UnsupportedOperationException if the dictionary is not reversable.
     */
    @Override
    public int reverseLookup(Take5Dictionary dict, int index, char[] buffer) throws Take5Exception {
        if (dict.fsaEngine != Take5Dictionary.ENGINE_SEARCH) {             // XXX: but this <EM>could</EM> work!
            throw new UnsupportedOperationException("This Take5Dictionary is not reversible.");
        }
        if (index < 0 || index >= dict.indexCount) {
            throw new Take5Exception(Take5Exception.IMPOSSIBLE_HASH);
        }
        if (buffer.length < dict.maxWordLength) {
            throw new Take5Exception(Take5Exception.BUFFER_TOO_SMALL);
        }
        // maxWordLength wasn't set until 5.2, so we wouldn't be on solid
        // ground before that version:
        if (dict.fileVersion < Take5Dictionary.VERSION_5_2) {
            throw new Take5Exception(Take5Exception.FILE_TOO_OLD);
        }
        int i = 0;              // index into buffer
        int state = dict.stateStart;
        /* delta is the difference between the index we're looking for and
           the index accumulated on the path through the FSA so far: */
        int delta = index + dict.indexOffset - dict.indexStart;
        while (delta > 0) {
            state &= -2;        // clear accept bit
            int edge = state - 8;
            int ptr = state;
            int xdelta;
            switch (data.getShort(ptr)) {

                default:
                    throw new Take5Exception(Take5Exception.UNSUPPORTED_STATE_TYPE);

                /* Encountering a state with <EM>no</EM> edges should be
                   impossible during a reverse lookup. */
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 0:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 0:
                    throw new Take5Exception(Take5Exception.BAD_DATA);

                /*
                 * CAUTION: Do not use '<' when you should be using
                 * 'Take5Dictionary.unsignedLess'!
                 */

                case Take5Dictionary.SEARCH_TYPE_LINEAR_MANY:
                /* Since no production release of the builder has ever
                   generated this state code, we can safely assume that if
                   we <EM>do</EM> encounter it, it will be properly
                   formatted with 0xFFFFFFFF in the final index_delta. */
                    while (!Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) {
                        edge -= 8;
                    }
                    break;

                case Take5Dictionary.SEARCH_TYPE_LINEAR + 16:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 15:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 14:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 13:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 12:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 11:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 10:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 9:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 8:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 7:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 6:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 6:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 5:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 5:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 4:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 4:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 3:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 3:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 2:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 2:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 1:
                    if (Take5Dictionary.unsignedLess(delta, data.getInt(edge - 8 + 4))) break;
                    edge -= 8;
                case Take5Dictionary.SEARCH_TYPE_LINEAR + 1:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 1:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 0:
                    break;

                case Take5Dictionary.SEARCH_TYPE_BINARY + 16:
                    if (charData[(ptr + (1 << 16)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 16) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 16);
                            edge -= (4 << 16);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 15:
                    if (charData[(ptr + (1 << 15)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 15) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 15);
                            edge -= (4 << 15);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 14:
                    if (charData[(ptr + (1 << 14)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 14) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 14);
                            edge -= (4 << 14);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 13:
                    if (charData[(ptr + (1 << 13)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 13) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 13);
                            edge -= (4 << 13);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 12:
                    if (charData[(ptr + (1 << 12)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 12) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 12);
                            edge -= (4 << 12);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 11:
                    if (charData[(ptr + (1 << 11)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 11) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 11);
                            edge -= (4 << 11);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 10:
                    if (charData[(ptr + (1 << 10)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 10) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 10);
                            edge -= (4 << 10);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 9:
                    if (charData[(ptr + (1 << 9)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 9) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 9);
                            edge -= (4 << 9);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 8:
                    if (charData[(ptr + (1 << 8)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 8) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 8);
                            edge -= (4 << 8);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 7:
                    if (charData[(ptr + (1 << 7)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 7) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 7);
                            edge -= (4 << 7);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 6:
                    if (charData[(ptr + (1 << 6)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 6) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 6);
                            edge -= (4 << 6);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 5:
                    if (charData[(ptr + (1 << 5)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 5) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 5);
                            edge -= (4 << 5);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 9:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 10:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 11:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 12:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 13:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 14:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 15:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 16:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 4:
                    if (charData[(ptr + (1 << 4)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 4) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 4);
                            edge -= (4 << 4);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 7:
                case Take5Dictionary.SEARCH_TYPE_CHOICE + 8:
                case Take5Dictionary.SEARCH_TYPE_BINARY + 3:
                    if  (charData[(ptr + (1 << 3)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 3) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 3);
                            edge -= (4 << 3);
                        }
                    }
                case Take5Dictionary.SEARCH_TYPE_BINARY + 2:
                    if (charData[(ptr + (1 << 2)) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - (4 << 2) + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            ptr += (1 << 2);
                            edge -= (4 << 2);
                        }
                    }
                    if (charData[(ptr + 2) >> 1] != '\uFFFF') {
                        xdelta = data.getInt(edge - 8 + 4);
                        if (!Take5Dictionary.unsignedLess(delta, xdelta) && xdelta != 0) {
                            edge -= 8;
                        }
                    }
                    break;
            }
            int stateDelta = data.getInt(edge);
            int indexDelta = data.getInt(edge + 4);
            assert(!Take5Dictionary.unsignedLess(delta, indexDelta));
            if (indexDelta == delta && (stateDelta & 1) == 0) {
                /* It is possible (and unavoidable) that we might overshoot
                   by just one edge when we get down to zero, but if the
                   accept bit isn't set, we should have taken the previous
                   edge. */
                edge += 8;
                stateDelta = data.getInt(edge);
                indexDelta = data.getInt(edge + 4);
            }
            assert(edge < state);
            buffer[i++] = charData[(state + (state - edge) / 4) >> 1];
            state += stateDelta;
            delta -= indexDelta;
        }
        assert((state & 1) != 0);
        return i;
    }

    @Override
    public void walkInternal(Take5Dictionary dict, Take5Walker w, Take5Match m, char[] buffer, int buflen,
                             int state, int index, int i)
            throws Take5Exception {
        if (i < buflen) {
            if ((state & 1) != 0) {
                state &= -2;          // clear accept bit
                m.state = state;
                m.index = index;
                m.length = i;
                w.foundAccept(m, buffer, buflen);
            }
            int nedges = edgeCount(state);
            for (int idx = 1; idx <= nedges; ++idx) {
                int edge = state - 8 * idx;
                buffer[i] = charData[(state + 2 * idx) >> 1];
                walkInternal(dict, w, m, buffer, buflen,
                        state + data.getInt(edge),
                        index + data.getInt(edge + 4),
                        i + 1);
            }
        } else {
            m.state = state & -2; // clear accept bit
            m.index = index;
            m.length = i;
            if (dict.terminalState(state)) {
                if ((state & 1) != 0) {
                    w.foundAccept(m, buffer, buflen);
                } else {
                    /* This shouldn't happen, given the current compiler,
                       but it is possible that some future compiler would
                       want to generate this case.  Thus it wouldn't be a
                       good idea to signal an error here -- instead we just
                       quietly do nothing. */
                }
            } else {
                if ((state & 1) != 0) {
                    w.foundBoth(m, buffer, buflen);
                } else {
                    w.foundLimit(m, buffer, buflen);
                }
            }
        }
    }
}
