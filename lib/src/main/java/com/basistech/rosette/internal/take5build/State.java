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

class State {
    int keyCount;               // How many keys accepted by this state
    int maxMatches;             // largest possible count of matches
    char[] edgeChar;
    boolean[] edgeIsAccept;
    State[] edgeState;
    int id;                     // Sequence number
    State previous;             // previously created state
    int hash;                   // for stateRegistry
    State next;                 // for stateRegistry
    Take5EntryPoint mark;       // for countStuff

    // For code generation:
    int address;                // address of the "nucleus" relative to the
                                // start of the state segment
    int size;                   // size of state in bytes
    boolean binarySearch;       // initially false
    boolean needGuard;          // initially false

    public String toString() {
        return String.format("#<State %d L=%d N=%d>", id, edgeChar.length, keyCount);
    }
}
