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

import java.io.IOException;

import static com.basistech.rosette.internal.take5build.Take5Format.SEARCH_TYPE_BINARY_0;
import static com.basistech.rosette.internal.take5build.Take5Format.SEARCH_TYPE_CHOICE_0;
import static com.basistech.rosette.internal.take5build.Take5Format.SEARCH_TYPE_LINEAR_0;
import static com.basistech.rosette.internal.take5build.Take5Format.SEARCH_TYPE_LINEAR_MANY;
import static com.basistech.rosette.internal.take5build.Take5Format.SEARCH_TYPE_MAX_PARAMETER;

class StateSegment extends BufferedSegment {

    // Note that the constructor has the side effect of initializing the
    // address, size, binarySearch & needGuard slot in all of the states.
    StateSegment(Take5Builder builder, String description) {
        super(builder, description);
        State s = builder.lastState;
        while (s != null) {
            /* The magic number `5' below was chosen using a combination of
               intuition and observing the effect on the size of the
               binary.  No runtime measurements were taken into account.
               It certainly should be at least 3. */
            int nedges = s.edgeChar.length;
            if (nedges > 5) {
                searchBinary(s, nedges);
            } else {
                searchLinear(s, nedges);
            }
            s = s.previous;
        }
    }

    private void searchBinary(State s, int nedges) {
        assert nedges != 0;
        s.binarySearch = true;
        s.needGuard = false;
        int nentries = nedges;
        int len = 1 << Utils.msb(nentries);
        if (nentries > len) {
            if (s.edgeChar[nedges - 1] != 0xFFFF) {
                s.needGuard = true;
                nentries++;
            }
            len <<= 1;
            if (nentries < len) {
                len -= 1 << Utils.msb(len - nentries);
            }
        }
        // So there are nentries * 8 bytes before the opcode, 2 bytes of
        // opcode and len * 2 bytes after the opcode.  (The first nedges *
        // 2 bytes after the opcode contain characters, and the rest must
        // be filled with 0xFF.)
        s.size = nentries * 8 + (1 + len) * 2;
        s.address = reserveChunk(s.size, 4) + nentries * 8;
    }

    private void searchLinear(State s, int nedges) {
        s.binarySearch = false;
        s.needGuard = false;
        int nentries = nedges;
        if (nedges > SEARCH_TYPE_MAX_PARAMETER && s.edgeChar[nedges - 1] != 0xFFFF) {
            s.needGuard = true;
            nentries++;
        }
        // So there are nentries * 8 bytes before the opcode, 2 bytes of
        // opcode and nentries * 2 bytes after the opcode.  (The first
        // nedges * 2 bytes after the opcode contain characters, and the
        // rest must be filled with 0xFF.)
        s.size = nentries * 8 + (1 + nentries) * 2;
        s.address = reserveChunk(s.size, 4) + nentries * 8;
    }

    void writeData() throws IOException {
        final int terminalAddress = builder.terminalState.address;
        State s = builder.lastState;
        while (s != null) {
            final int nedges = s.edgeChar.length;
            final int saddress = s.address;
            final int nentries = s.needGuard ? nedges + 1 : nedges;
            allocateChunk(s.size, 4);
            assert saddress == address + nentries * 8;
            assert saddress % 2 == 0; // Make sure we can set the accept bit,
            assert offset % 4 == 0;   // and store ints.
            final int intPtr = offset / 4 + nentries * 2;
            final int shortPtr = offset / 2 + nentries * 4;

            // Fill in the edges:
            final int stateDope = intPtr - 2;
            final int indexDope = intPtr - 1;
            int ix = 0;
            for (int i = 0; i < nedges; i++) {
                int delta = s.edgeState[i].address - saddress;
                if (s.edgeIsAccept[i]) {
                    delta |= 1;
                    ix++;
                }
                intBuffer.put(stateDope - i * 2, delta);
                intBuffer.put(indexDope - i * 2, ix);
                ix += s.edgeState[i].keyCount;
            }
            // Plus a guard edge if called for:
            if (s.needGuard) {
                intBuffer.put(stateDope - nedges * 2, terminalAddress - saddress);
                intBuffer.put(indexDope - nedges * 2, -1);
            }

            // Fill in the opcode:
            int opcode = -1;
            if (nedges > SEARCH_TYPE_MAX_PARAMETER) {
                if (s.binarySearch) {
                    opcode = SEARCH_TYPE_BINARY_0 + (Utils.msb(nedges - 1) + 1);
                } else {
                    // The way the first pass is written (the code in the
                    // constructor), we never actually come here and so we
                    // never generate this opcode.  But that option is
                    // still open to us someday...  (Unlike the
                    // SEARCH_TYPE_DISPATCH family, which we have promised
                    // to never generate.)
                    opcode = SEARCH_TYPE_LINEAR_MANY;
                }
            } else {
                if (s.binarySearch) {
                    opcode = SEARCH_TYPE_CHOICE_0 + nedges;
                } else {
                    assert !s.needGuard;
                    opcode = SEARCH_TYPE_LINEAR_0 + nedges;
                }
            }
            // The runtime depends on the fact that a state with no edges
            // always has the SEARCH_TYPE_LINEAR_0 opcode:
            assert nedges > 0 || opcode == SEARCH_TYPE_LINEAR_0;
            assert 0 <= opcode && opcode <= 0xFFFF;
            shortBuffer.put(shortPtr, (short)opcode);

            // Fill in the characters:
            charBuffer.position(shortPtr + 1);
            charBuffer.put(s.edgeChar);

            // Fill in the padding:
            int npad = (s.size - nentries * 8) / 2 - (1 + nedges);
            while (npad-- > 0) {
                charBuffer.put((char)0xFFFF);
            }

            s = s.previous;
        }
        flushChunk();
    }
}
