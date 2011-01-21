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

class TableSegment extends BufferedSegment {
    Segment valueSegment;

    TableSegment(Take5Builder builder, String description, Segment valueSegment) {
        super(builder, description);
        size = 4 * builder.totalKeyCount;
        alignment = 4;
        this.valueSegment = valueSegment;
    }

    void writeData() throws IOException {
        final int buflim = bufsize - 4;
        final int baseAddr = valueSegment.getAddress();
        assert offset == 0;
        intBuffer.position(0);
        for (Value v : builder.valueTable) {
            if (offset > buflim) {
                flushData();
                assert offset % 4 == 0;
                intBuffer.position(offset / 4);
            }
            if (v == null) {
                intBuffer.put(0);
            } else {
                intBuffer.put(baseAddr + v.address);
            }
            offset += 4;
        }
    }
}
