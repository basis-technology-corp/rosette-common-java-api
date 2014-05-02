/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;


/** Fowler/Noll/Vo hashing.
 *
 * See: <http://tools.ietf.org/html/draft-eastlake-fnv-07> for info on the
 * FNV family of hash functions.  (Replace the link above when/if it
 * becomes an RFC.)
 */
final class FnvHash {
    static final int FNV32_PRIME = 0x01000193;
    static final int FNV32_BASE = 0x811C9DC5;

    /*
     * This returns a 31-bit hash by folding the full 32-bit FNV value in
     * manner recommended by the referenced documentation.  This hashes 16-bit
     * bytes while FNV32 was originally designed for 8-bit bytes -- but it
     * still works.
     */
    static int fnvhash(int fun, final byte[] data, final int start, final int end) {
        int rv;

        rv = (fun + 1) * FNV32_BASE;
        for (int idx = start; idx < end; idx++) {
            rv ^= data[idx]; // data types?
            rv *= FNV32_PRIME;
        }
        /* Fold the high bit in to the low bit: */
        return (rv ^ (rv >> 31)) & 0x7FFFFFFF;
    }
}
