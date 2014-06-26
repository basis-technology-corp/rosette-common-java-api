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

package com.basistech.rosette.internal.take5build;

import java.nio.ByteBuffer;

/**
 * This is essentially a Map&lt;byte[], Value&gt;, focussed on an 'intern' scheme.
 */
class ValueRegistry {
    Value[] values;
    int size;
    int maxValueSize;
    int commonAlign = 1;

    ValueRegistry(int size) {
        values = new Value[size];
    }

    /**
     * Add a value, or return an existing one with the same data/alignment.
     * @param data data to store
     * @param alignment alignment
     * @return interned value
     */
    Value intern(ByteBuffer data, int alignment, short flags) {

        if (data == null) {
            data = ByteBuffer.allocate(0);
            alignment = 1;
        }

        commonAlign = Utils.lcm(commonAlign, alignment);

        data.limit(data.capacity());
        data.position(0);
        int length = data.capacity();

        if (length > maxValueSize) { maxValueSize = length; }
        int hash = Utils.hashBytes(data);
        int index = (hash & 0x7FFFFFFF) % values.length;
        Value firstValue = values[index];
        Value v;

        for (v = firstValue; v != null; v = v.next) {
            v.data.position(0);
            v.data.limit(v.data.capacity());
            if (v.hash == hash && v.data.capacity() == length && v.data.equals(data)) {
                v.alignment = Utils.lcm(alignment, v.alignment);

                return v;
            }
        }
        v = new Value(data, alignment, flags, hash, firstValue);
        size++;
        values[index] = v;
        return v;
    }
}
