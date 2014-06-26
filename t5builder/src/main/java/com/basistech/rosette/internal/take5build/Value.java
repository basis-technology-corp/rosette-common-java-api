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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Value blob.
 */
class Value {
    static final short VALUE = 2;
    static final short KEY = 1;

    final ByteBuffer data;
    int alignment; // NOT FINAL. The registry will lcm this as it merges in.

    final int hash;                   // for valueRegistry
    final Value next;                 // for valueRegistry
    final short flags;
    int address;

    Value(ByteBuffer data, int alignment, short flags, int hash, Value next) {
        assert !(flags == KEY && ((data.capacity() & 1) != 0)) : "Odd byte length of key";
        this.data = data;
        this.alignment = alignment;
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
        CharBuffer dataAsChars = data.asCharBuffer();
        try {
            // -1 to take away the null termination.
            dataAsChars.limit(dataAsChars.capacity() - 1);
            return FnvHash.fnvhash(hashFun, dataAsChars);
        } finally {
            // don't leave a side-effect on the limit.
            dataAsChars.limit(dataAsChars.capacity());
        }
    }
}
