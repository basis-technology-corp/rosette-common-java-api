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

abstract class BufferedSegment extends Segment {
    OutputStream out;           // if any
    byte [] rawbuf;             // if needed
    int maxChunkSize;           // initially 0
    int currentChunkSize;       // initially 0
    int remaining;

    // for subclasses to use:
    int address;                // segment address of current chunk
    int bufsize;                // size of byteBuffer
    int offset;                 // offset into byteBuffer
    ByteBuffer byteBuffer;
    CharBuffer charBuffer;
    ShortBuffer shortBuffer;
    IntBuffer intBuffer;
    
    // XXX Should be a way to vary this:
    int bufferSize = 8 * 1024;
    //int bufferSize = -1;        // for testing...

    // We really only need 4 because we don't use LongBuffer, but what the
    // heck...
    int bufferAlignment = 8;

    BufferedSegment(Take5Builder builder, String description) {
        super(builder, description);
    }

    /*
     * How the various counters and lengths used here are related:
     *
     *    |<-------------------- size -------------------->|
     *    |              |<---------- remaining ---------->|
     *    |              |<--------------- bufsize --------------->|
     *    |              |<----- offset ----->|       (room)       |
     *    |<------------ address ------------>|
     */

    private void setBuffers(ByteBuffer buffer) {
        buffer.order(builder.byteOrder);
        byteBuffer = buffer;
        charBuffer = byteBuffer.asCharBuffer();
        shortBuffer = byteBuffer.asShortBuffer();        
        intBuffer = byteBuffer.asIntBuffer();
        remaining = size;
        address = 0;
        offset = 0;
    }

    void writeSegment(ByteBuffer outBuffer) {
        assert isClosed && haveAddress;
        assert size > 0;
        out = null;
        bufsize = size;
        outBuffer.limit(baseAddress + size);
        outBuffer.position(baseAddress);
        setBuffers(outBuffer.slice());
        try {
            writeData();
        } catch (IOException e) {
            // This is only possible if flushData() was called, which it
            // shouldn't be since out == null!
            throw new Take5BuilderException("Unexpected IOException", e);
        }
        if (offset != size) {
            throw new Take5BuilderException("Output error: missing data");
        }
    }

    void writeSegment(OutputStream outStream) throws IOException {
        assert isClosed && haveAddress;
        assert size > 0;
        out = outStream;
        // Leave enough room so that we can flush the buffer in multiples
        // of bufferAlignment, and we'll still have room to pad up to the
        // max chunk alignment and then buffer up the longest chunk.  Also
        // allocate at least bufferSize -- unless that would be larger than
        // output.
        bufsize = Math.max(maxChunkSize + alignment + bufferAlignment,
                           Math.min(size, bufferSize));
        rawbuf = new byte[bufsize];
        setBuffers(ByteBuffer.wrap(rawbuf));
        writeData();
        // There <EM>must</EM> be data waiting!
        if (offset <= 0 || remaining != offset) {
            throw new Take5BuilderException("Output error: missing data");
        }
        out.write(rawbuf, 0, offset);
    }

    // Subclasses must implement this method.  Subclasses should either:
    //
    // (1) Call reserveChunk in their constructor and allocateChunk (and flushChunk)
    //     from their writeData method.
    //
    // (2) Initialise size and alignment in their constructor and directly
    //     call flushData from their writeData method while maintaining
    //     "offset" themselves.
    //
    abstract void writeData() throws IOException;

    // Returns a relative address within the segment.
    int reserveChunk(int chunkSize, int chunkAlignment) {
        if (isClosed) {
            throw new Take5BuilderException("Segment is closed");
        }
        maxChunkSize = Math.max(maxChunkSize, chunkSize);
        alignment = Utils.lcm(alignment, chunkAlignment);
        int addr = Utils.alignUp(size, chunkAlignment);
        size = addr + chunkSize;
        return addr;
    }

    void allocateChunk(int chunkSize, int chunkAlignment) throws IOException {
        address += currentChunkSize;
        offset += currentChunkSize;
        int newaddr = Utils.alignUp(address, chunkAlignment);
        assert offset <= bufsize;
        if (newaddr - address + chunkSize > bufsize - offset) {
            // There isn't room for the padding plus the whole chunk.  So
            // flush out some data.  Fortunately we sized the buffer so
            // that this is guaranteed to leave enough room!
            flushData();
            assert newaddr - address + chunkSize <= bufsize - offset;
        }
        while (address < newaddr) {
            byteBuffer.put(offset, (byte)0xBB); // XXX what does the C builder use?
            address++;
            offset++;
        }
        currentChunkSize = chunkSize;
    }

    void flushChunk() throws IOException {
        address += currentChunkSize;
        offset += currentChunkSize;
        assert offset <= bufsize;
        currentChunkSize = 0;
    }

    void flushData() throws IOException {
        if (!(0 < offset && offset <= remaining)) {
            throw new Take5BuilderException("Output error: bad offset");
        }
        assert out != null;
        int count = Utils.alignDown(offset, bufferAlignment);
        out.write(rawbuf, 0, count);
        for (int i = count; i < offset; i++) {
            rawbuf[i - count] = rawbuf[i];
        }
        remaining -= count;
        offset -= count;
    }
}
