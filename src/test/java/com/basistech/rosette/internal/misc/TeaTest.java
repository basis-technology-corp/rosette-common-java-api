/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/
package com.basistech.rosette.internal.misc;

import java.security.MessageDigest;
import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TeaTest {

    private static final int[] TK = {
        0x404B6922, 0x13E035F9, 0x23EB0754, 0xCFC8926E
    };
    // must be 0 mod 8 in length.
    private static final String TEST_TEXT = "This is the cereal shot from guns.______";

    @Test
    public void testRoundTrip() throws Exception {
        assertEquals(0, TEST_TEXT.length() % 8);
        byte[] testData = TEST_TEXT.getBytes("ASCII");
        assertEquals(TEST_TEXT.length(), testData.length);
        byte[] enc = Tea.encrypt(TK, testData);
        byte[] dec = Tea.decrypt(TK, enc);
        assertFalse(Arrays.equals(enc, dec));
        assertArrayEquals(testData, dec);
    }

    @Test
    public void testInteropCases() throws Exception {
        String hex = "98F8FB41174E3AAA9827486C8C7EE0FC55906FB42983F96C4823FA706CEE1382";
        byte[] data = Hex.hexToBytes(hex);
        byte[] dec = Tea.decrypt(TK, data);

        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(dec, 16, dec.length - 16);
        byte messageDigest[] = algorithm.digest();
        byte[] deliveredDigest = new byte[16];
        System.arraycopy(dec, 0, deliveredDigest, 0, 16);
        assertArrayEquals(messageDigest, deliveredDigest);

        String decAsString = new String(dec, 16, dec.length - 16, "ASCII");
        int zindex = decAsString.indexOf(0);
        if (zindex != 0) {
            decAsString = decAsString.substring(0, zindex);
        }
        assertEquals("Foo=1;Bar=2;", decAsString);
    }

}
