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

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedInts;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 *
 */
public class PayloadParserTest extends Assert {

    //CHECKSTYLE:OFF
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON
    
    private ByteBuffer wrap(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.nativeOrder());
        return buffer;
    }

    @Test
    public void testHexdump() throws Exception {
        //                                                      .  .  . .  . . . .
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("[ 35 36 3435 34353637 ]");
        ByteBuffer buffer = wrap(payload.bytes);

        assertEquals(4, payload.alignment);
        assertEquals(8, buffer.limit());
        assertEquals((byte)0x35, buffer.get(0));
        assertEquals((byte)0x36, buffer.get(1));
        ShortBuffer shortView = buffer.asShortBuffer();
        assertEquals((short)0x3435, shortView.get(1));
        IntBuffer intView = buffer.asIntBuffer();
        assertEquals(0x34353637, intView.get(1));

        thrown.expect(PayloadParserException.class);
        thrown.expectMessage("alignment");
        PayloadParser.newParser(ByteOrder.nativeOrder()).parse("[ 35 3435 ]");
    }

    @Test
    public void testBigEndianOrder() throws Exception {
        // tests reversed order on LE, harmless on BE.
        Payload payload = PayloadParser.newParser(ByteOrder.BIG_ENDIAN).parse("[ 35 36 3435 34353637 ]");
        ByteBuffer buffer = wrap(payload.bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        assertEquals(4, payload.alignment);
        assertEquals(8, buffer.limit());
        assertEquals((byte)0x35, buffer.get(0));
        assertEquals((byte)0x36, buffer.get(1));
        ShortBuffer shortView = buffer.asShortBuffer();
        assertEquals((short)0x3435, shortView.get(1));
        IntBuffer intView = buffer.asIntBuffer();
        assertEquals(0x34353637, intView.get(1));
    }

    @Test
    public void testStrings() throws Exception {
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("\"hello\"");
        assertEquals(2, payload.alignment);
        ByteBuffer buffer = wrap(payload.bytes);
        buffer.order(ByteOrder.nativeOrder());
        assertEquals(6 * 2, buffer.limit());
        char[] retrieved = new char[6];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello\u0000".toCharArray(), retrieved);

        // try a mode, no terminating null

        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#!s \"hello\"");
        assertEquals(2, payload.alignment);
        buffer = wrap(payload.bytes);
        buffer.order(ByteOrder.nativeOrder());

        assertEquals(5 * 2, buffer.limit());
        retrieved = new char[5];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello".toCharArray(), retrieved);

        // byte string
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#1!s \"hello\"");
        assertEquals(1, payload.alignment);
        buffer = wrap(payload.bytes);
        assertEquals(5, buffer.limit());
        byte[] retrievedBytes = new byte[5];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello".getBytes(Charsets.US_ASCII), retrievedBytes);

        //terminated byte string
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#1s \"hello\"");
        buffer = wrap(payload.bytes);
        assertEquals(6, buffer.limit());
        retrievedBytes = new byte[6];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello\u0000".getBytes(Charsets.US_ASCII), retrievedBytes);
    }

    // see COMN-139. \\ was not reduced to one \ in the lex. The PU is a red herring.
    @Test
    public void slashPu() throws Exception {
        String payloadString = "3 \"\\\\\ue018\""; // a 3, an escaped \, and a PU char.
        PayloadParser parser = PayloadParser.newParser(ByteOrder.LITTLE_ENDIAN);
        parser.parseMode("#2!i", 0);
        Payload payload = parser.parse(payloadString);
        assertEquals(2, payload.alignment);
        // what's in the bytes ... three 2-byte items.
        assertEquals(8, payload.bytes.length); // 3 chars and a null at the end.
        ByteBuffer wrapped = ByteBuffer.wrap(payload.bytes);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer shorts = wrapped.asShortBuffer();
        assertEquals(3, shorts.get(0));
        assertEquals('\\', (char)shorts.get(1));
        assertEquals('\ue018', (char)shorts.get(2));
    }

    @Test
    public void testStringsTempModes() throws Exception {
        // turn off null termination
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#!: \"hello\"");
        ByteBuffer buffer = wrap(payload.bytes);
        assertEquals(5 * 2, buffer.limit());
        char[] retrieved = new char[5];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello".toCharArray(), retrieved);

        // byte string
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#1!: \"hello\"");
        buffer = wrap(payload.bytes);
        assertEquals(5, buffer.limit());
        byte[] retrievedBytes = new byte[5];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello".getBytes(Charsets.US_ASCII), retrievedBytes);

        //terminated byte string
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#1: \"hello\"");
        buffer = wrap(payload.bytes);
        assertEquals(6, buffer.limit());
        retrievedBytes = new byte[6];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello\u0000".getBytes(Charsets.US_ASCII), retrievedBytes);

        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#1!s #2*: \"hello\"");
        buffer = wrap(payload.bytes);
        assertEquals(6 * 2, buffer.limit());
        retrieved = new char[6];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello\u0000".toCharArray(), retrieved);

    }

    @Test
    public void testStringBadFlag() throws Exception {
        thrown.expect(PayloadParserException.class);
        thrown.expectMessage("Lexical error");
        PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#@s");
    }

    @Test
    public void testStringBadFlagColumn() throws Exception {
        try {
            PayloadParser.newParser(ByteOrder.nativeOrder()).parse("  #@s");
        } catch (PayloadParserException ppe) {
            assertEquals(2, ppe.getColumn());
        }
    }

    @Test
    public void testStringBadLength() throws Exception {
        thrown.expect(PayloadParserException.class);
        PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#9s"); // lexer is helpfully catching
    }

    @Test
    public void testInts() throws Exception {
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("42 -567 0x54 0b101 -0x45 -0b1011");
        ByteBuffer buffer = wrap(payload.bytes);
        assertEquals(24, buffer.limit());
        assertEquals(42, buffer.asIntBuffer().get(0));
        assertEquals(-567, buffer.asIntBuffer().get(1));
        assertEquals(0x54, buffer.asIntBuffer().get(2));
        assertEquals(5, buffer.asIntBuffer().get(3));
        assertEquals(- 0x45, buffer.asIntBuffer().get(4));
        assertEquals(- 11, buffer.asIntBuffer().get(5));
    }

    @Test
    public void testSizedIntegers() throws Exception {
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("42 #1: 43 #1: 44 #2: 0x2332 #4: 0x12345678");
        ByteBuffer buffer = wrap(payload.bytes);
        assertEquals(12, buffer.limit());
        IntBuffer ints = buffer.asIntBuffer();
        ShortBuffer shorts = buffer.asShortBuffer();
        assertEquals(42, ints.get(0));
        // 0123 are the 42
        assertEquals((byte)43, buffer.get(4));
        assertEquals((byte)44, buffer.get(5));
        assertEquals((short)0x2332, shorts.get(3));
        assertEquals(0x12345678, ints.get(2));
    }

    @Test
    public void testUnsignedIntegers() throws Exception {
        // a series of unsigned items
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#!i #1: 0xf0 #1: 0xf1 #2: 0xf000 #4: 0xf0000000");
        ByteBuffer buffer = wrap(payload.bytes);
        assertEquals(8, buffer.limit());
        assertEquals((byte)0xf0, buffer.get(0));
        assertEquals((byte)0xf1, buffer.get(1));
        assertEquals((char)0xf000, buffer.asCharBuffer().get(1));
        assertTrue(UnsignedInts.compare(0xf0000000, buffer.asIntBuffer().get(1)) == 0);
    }

    @Test
    public void testFloats() throws Exception {
        Float fval = 1.23e3f;
        Double dval = 1.23e-30;

        String floatTestCase = String.format("#4f %g 0 #8f %g inf NaN", fval, dval);
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(floatTestCase);
        assertEquals(8, payload.alignment);
        ByteBuffer buffer = wrap(payload.bytes);
        Float floatResult = buffer.getFloat(0);
        Double doubleResult = buffer.getDouble(8);
        assertEquals(Float.floatToRawIntBits(fval), Float.floatToRawIntBits(floatResult));
        assertEquals(Double.doubleToRawLongBits(dval), Double.doubleToRawLongBits(doubleResult));
        assertTrue(Double.isInfinite(buffer.getDouble(16)) && buffer.getDouble(16) > 0);
        assertTrue(Double.isNaN(buffer.getDouble(24)));

        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse("#4f 0.1");
        assertEquals(4, payload.alignment);
    }

    @Test
    public void veryLargePayload() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("#8i");
        int count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 3;
        for (int x = 0; x < count; x ++) {
            sb.append(" 42");
        }
        Payload payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(count * 8)));
        ByteBuffer payloadBuffer = wrap(payload.bytes);
        LongBuffer longPayloadBuffer = payloadBuffer.asLongBuffer();
        assertThat(longPayloadBuffer.get(101), is(equalTo((long)42)));

        sb = new StringBuilder();
        sb.append("#4i");
        count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 6;
        for (int x = 0; x < count; x ++) {
            sb.append(" 42");
        }
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(count * 4)));

        sb = new StringBuilder();
        sb.append("#2i");
        count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 9;
        for (int x = 0; x < count; x ++) {
            sb.append(" 42");
        }
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(count * 2)));

        sb = new StringBuilder();
        sb.append("#1i");
        count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 12;
        for (int x = 0; x < count; x ++) {
            sb.append(" 42");
        }
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(count)));

        sb = new StringBuilder();
        sb.append("#2s \"");
        count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 12;
        for (int x = 0; x < count; x ++) {
            sb.append("0");
        }
        sb.append("\"");
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(2 + (count * 2))));

        sb = new StringBuilder();
        sb.append("#1s \"");
        count = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES * 12;
        for (int x = 0; x < count; x ++) {
            sb.append("0");
        }
        sb.append("\"");
        payload = PayloadParser.newParser(ByteOrder.nativeOrder()).parse(sb.toString());
        assertThat(payload.bytes.length, is(equalTo(1 + count)));
    }
}
