/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010-2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

/**
 * Value blob.
 */
class Value {
    static final short VALUE = 2;
    static final short KEY = 1;

    final byte[] data;
    int alignment; // NOT FINAL. The registry will lcm this as it merges in.
    final int offset;                 // offset into data
    final int length;
    final int hash;                   // for valueRegistry
    final Value next;                 // for valueRegistry
    final short flags;
    int address;

    Value(byte[] data, int offset, int length, int alignment, short flags, int hash, Value next) {
        assert !(flags == KEY && ((length & 1) != 0)) : "Odd byte length of key";
        this.data = data;
        this.alignment = alignment;
        this.offset = offset;
        this.length = length;
        this.hash = hash;
        this.next = next;
        this.flags = flags;
    }

    boolean isValue() {
        return 0 != (flags & 2);
    }

    boolean isKey() {
        return 0 != (flags & 1);
    }

    int getKeyHash(int hashFun) {
        assert isKey();
        return FnvHash.fnvhash(hashFun, data, 0, length);
    }
}
