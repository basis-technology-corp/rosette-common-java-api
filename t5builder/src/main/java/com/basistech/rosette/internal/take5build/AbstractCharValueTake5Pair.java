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
import java.nio.ByteOrder;

/**
 * This class makes it easier to build Take5's with values that are null-terminated UTF-16 values.
 * This abstract class allows for a case in which the strings are sitting in some other data
 * structure.
 */
public abstract class AbstractCharValueTake5Pair implements Take5Pair {
    private final ByteOrder byteOrder;

    protected AbstractCharValueTake5Pair(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    protected abstract CharSequence getCharacterData();

    @Override
    public byte[] getValue() {
        CharSequence data = getCharacterData();
        byte[] array = new byte[2 * (data.length() + 1)];
        ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.order(byteOrder);
        for (int x = 0; x < data.length(); x++) {
            buffer.putChar(data.charAt(x));
        }
        return array;
    }

    @Override
    public int getAlignment() {
        return 2;
    }

    @Override
    public int getOffset() {
        return 0;
    }

    @Override
    public int getLength() {
        return 2 * (getCharacterData().length() + 1);
    }
}
