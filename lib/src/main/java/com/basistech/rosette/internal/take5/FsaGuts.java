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

package com.basistech.rosette.internal.take5;

/**
 * Deal with copying versus non-copying FSA engines.
 */
interface FsaGuts {
    int edgeCount(int state);
    int take5SearchInternal(Take5Dictionary dict,
                            char[] input, int offset, int length,
                            Take5Match match, Take5Match[] matches,
                            int startState, int startIndex);
    Take5Match[] nextLettersInternal(Take5Dictionary dict, int state, int index, int length) throws Take5Exception;
    int reverseLookup(Take5Dictionary dict, int index, char[] buffer) throws Take5Exception;
    void walkInternal(Take5Dictionary dict, Take5Walker w, Take5Match m, char[] buffer, int buflen,
                      int state, int index, int i) throws Take5Exception;
}
