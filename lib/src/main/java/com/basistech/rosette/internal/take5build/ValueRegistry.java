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
     * @param data
     * @param start
     * @param end
     * @param alignment
     * @return
     */
    Value intern(byte[] data, int start, int end, int alignment, short flags) {

        if (data == null) {
            data = new byte[0];
            start = 0;
            end = 0;
            alignment = 1;
        }

        commonAlign = Utils.lcm(commonAlign, alignment);

        int length = end - start;

        if (length > maxValueSize) { maxValueSize = length; }
        int hash = Utils.hashBytes(data, start, length);
        int index = (hash & 0x7FFFFFFF) % values.length;
        Value firstValue = values[index];
        Value v;

        for (v = firstValue; v != null; v = v.next) {
            if (v.hash == hash && v.length == length
                    && Utils.equalBytes(v.data, v.offset, data, start, length)) {
                v.alignment = Utils.lcm(alignment, v.alignment);

                return v;
            }
        }
        v = new Value(data, start, length, alignment, flags, hash, firstValue);
        size++;
        values[index] = v;
        return v;
    }
}
