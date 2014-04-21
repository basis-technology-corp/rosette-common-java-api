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

package com.basistech.t5build;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedInts;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 *
 */
public class PayloadParserTest extends Assert {

    //CHECKSTYLE:OFF
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    //CHECKSTYLE:ON

    @Test
    public void testHexdump() throws Exception {
        //                                                      .  .  . .  . . . .
        Payload payload = PayloadParser.newParser().parse("[ 35 36 3435 34353637 ]");
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
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

        PayloadParser.newParser().parse("[ 35 3435 ]");
    }

    @Test
    public void testStrings() throws Exception {
        Payload payload = PayloadParser.newParser().parse("\"hello\"");
        assertEquals(2, payload.alignment);
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(6 * 2, buffer.limit());
        char[] retrieved = new char[6];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello\u0000".toCharArray(), retrieved);

        // try a mode, no terminating null

        payload = PayloadParser.newParser().parse("#!s \"hello\"");
        assertEquals(2, payload.alignment);
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(5 * 2, buffer.limit());
        retrieved = new char[5];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello".toCharArray(), retrieved);

        // byte string
        payload = PayloadParser.newParser().parse("#1!s \"hello\"");
        assertEquals(1, payload.alignment);
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(5, buffer.limit());
        byte[] retrievedBytes = new byte[5];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello".getBytes(Charsets.US_ASCII), retrievedBytes);

        //terminated byte string
        payload = PayloadParser.newParser().parse("#1s \"hello\"");
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(6, buffer.limit());
        retrievedBytes = new byte[6];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello\u0000".getBytes(Charsets.US_ASCII), retrievedBytes);
    }

    @Test
    public void testStringsTempModes() throws Exception {
        // turn off null termination
        Payload payload = PayloadParser.newParser().parse("#!: \"hello\"");
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(5 * 2, buffer.limit());
        char[] retrieved = new char[5];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello".toCharArray(), retrieved);

        // byte string
        payload = PayloadParser.newParser().parse("#1!: \"hello\"");
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(5, buffer.limit());
        byte[] retrievedBytes = new byte[5];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello".getBytes(Charsets.US_ASCII), retrievedBytes);

        //terminated byte string
        payload = PayloadParser.newParser().parse("#1: \"hello\"");
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(6, buffer.limit());
        retrievedBytes = new byte[6];
        buffer.get(retrievedBytes);
        assertArrayEquals("hello\u0000".getBytes(Charsets.US_ASCII), retrievedBytes);

        payload = PayloadParser.newParser().parse("#1!s #2*: \"hello\"");
        buffer = ByteBuffer.wrap(payload.bytes);
        assertEquals(6 * 2, buffer.limit());
        retrieved = new char[6];
        buffer.asCharBuffer().get(retrieved);
        assertArrayEquals("hello\u0000".toCharArray(), retrieved);

    }

    @Test
    public void testStringBadFlag() throws Exception {
        thrown.expect(PayloadParserException.class);
        thrown.expectMessage("Lexical error");
        PayloadParser.newParser().parse("#@s");
    }

    @Test
    public void testStringBadFlagColumn() throws Exception {
        try {
            PayloadParser.newParser().parse("  #@s");
        } catch (PayloadParserException ppe) {
            assertEquals(2, ppe.getColumn());
        }
    }

    @Test
    public void testStringBadLength() throws Exception {
        thrown.expect(PayloadParserException.class);
        PayloadParser.newParser().parse("#9s"); // lexer is helpfully catching
    }

    @Test
    public void testInts() throws Exception {
        Payload payload = PayloadParser.newParser().parse("42 -567 0x54 0b101 -0x45 -0b1011");
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
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
        Payload payload = PayloadParser.newParser().parse("42 #1: 43 #1: 44 #2: 0x2332 #4: 0x12345678");
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
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
        Payload payload = PayloadParser.newParser().parse("#!i #1: 0xf0 #1: 0xf1 #2: 0xf000 #4: 0xf0000000");
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
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
        Payload payload = PayloadParser.newParser().parse(floatTestCase);
        assertEquals(8, payload.alignment);
        ByteBuffer buffer = ByteBuffer.wrap(payload.bytes);
        Float floatResult = buffer.getFloat(0);
        Double doubleResult = buffer.getDouble(8);
        assertEquals(Float.floatToRawIntBits(fval), Float.floatToRawIntBits(floatResult));
        assertEquals(Double.doubleToRawLongBits(dval), Double.doubleToRawLongBits(doubleResult));
        assertTrue(Double.isInfinite(buffer.getDouble(16)) && buffer.getDouble(16) > 0);
        assertTrue(Double.isNaN(buffer.getDouble(24)));

        payload = PayloadParser.newParser().parse("#4f 0.1");
        assertEquals(4, payload.alignment);
    }
}
