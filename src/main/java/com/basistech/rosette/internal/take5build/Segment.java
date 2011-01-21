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
import java.io.OutputStream;
import java.nio.ByteBuffer;

abstract class Segment {
    Take5Builder builder;
    int baseAddress;
    int size;
    int alignment;
    boolean haveAddress;
    boolean isClosed;           // size and alignment are frozen
    String description;         // for debugging

    Segment(Take5Builder builder, String description) {
        this.builder = builder;
        this.description = description;
        haveAddress = false;
        isClosed = false;
        size = 0;
        alignment = 1;
        builder.segments.add(this);
    }

    // Subclasses must implement both of the following methods.  In both
    // cases the method is responsible for generating <EM>exactly</EM> the
    // number of bytes that getSize() would return.

    // Method is responsible for depositing the bytes at the correct address:
    abstract void writeSegment(ByteBuffer out);

    // Caller is responsible for padding us up to the right address first:
    abstract void writeSegment(OutputStream out) throws IOException;

    int getSize() {
        isClosed = true;
        return size;
    }

    int getLimit() {
        isClosed = true;        // so that getAddress knows we're closed
        return getAddress() + size;
    }

    int getAddress() {
        if (!haveAddress) {
            if (isClosed && size == 0) {
                // if we already know the segment will be empty, we can use
                // 0 as its address and output nothing!
                baseAddress = 0;
            } else {
                int n = builder.outputList.size();
                if (n == 0) {
                    baseAddress = 0;
                } else {
                    Segment prev = builder.outputList.get(n - 1);
                    baseAddress = Utils.alignUp(prev.getLimit(), alignment);
                }
                builder.outputList.add(this);
            }
            haveAddress = true;
        }
        return baseAddress;
    }
}
