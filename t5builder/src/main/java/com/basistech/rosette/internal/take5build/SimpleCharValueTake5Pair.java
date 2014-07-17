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

import java.nio.ByteOrder;

/**
 * Take5 pair that stores a String.
 */
public class SimpleCharValueTake5Pair extends AbstractCharValueTake5Pair {

    private final char[] key;
    private final CharSequence data;

    public SimpleCharValueTake5Pair(String key, CharSequence data) {
        super(ByteOrder.nativeOrder());
        this.key = key.toCharArray();
        this.data = data;
    }

    public SimpleCharValueTake5Pair(ByteOrder order, String key, CharSequence data) {
        super(order);
        this.key = key.toCharArray();
        this.data = data;
    }

    @Override
    protected CharSequence getCharacterData() {
        return data;
    }

    @Override
    public char[] getKey() {
        return key;
    }

    @Override
    public int getKeyLength() {
        return key.length;
    }
}
