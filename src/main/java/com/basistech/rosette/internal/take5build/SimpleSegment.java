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
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

class SimpleSegment extends Segment {
    byte[] rawbuf;
    ByteBuffer buffer;

    SimpleSegment(Take5Builder builder, String description,
                  int bufferSize, int bufferAlignment) {
        super(builder, description);
        size = bufferSize;
        alignment = bufferAlignment;
        rawbuf = new byte[size];
        buffer = ByteBuffer.wrap(rawbuf);
        buffer.order(builder.byteOrder);
        isClosed = true;
    }

    // The caller of any of the following set of getFrobBuffer() methods is
    // responsible for worrying about the sharing implied by the following
    // definitions.

    ByteBuffer getByteBuffer() {
        return buffer;
    }

    CharBuffer getCharBuffer() {
        return buffer.asCharBuffer();
    }

    ShortBuffer getShortBuffer() {
        return buffer.asShortBuffer();
    }

    IntBuffer getIntBuffer() {
        return buffer.asIntBuffer();
    }

    void writeSegment(ByteBuffer out) {
        assert size > 0 && haveAddress;
        out.limit(baseAddress + size);
        out.position(baseAddress);
        out.put(rawbuf);
    }

    void writeSegment(OutputStream out) throws IOException {
        assert size > 0 && haveAddress;
        out.write(rawbuf);
    }
}
