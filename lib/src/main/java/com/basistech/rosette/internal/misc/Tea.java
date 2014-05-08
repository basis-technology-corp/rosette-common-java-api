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

public final class Tea {
    private static final int DELTA = 0x9E3779B9;

    private Tea() {
    }

    /**
     * encrypt in place
     * 
     * @param keyArray key (4)
     * @param numArray value (2)
     */
    public static void encryptBlock(int[] keyArray, int[] numArray) {
        int y = numArray[0];
        int z = numArray[1];
        int sum = 0;
        int n = 32;     /* a key schedule constant */

        while (n-- > 0) { /* basic cycle start */
            y += (z << 4 ^ z >>> 5) + z ^ sum + keyArray[sum & 3];
            sum += DELTA;
            z += (y << 4 ^ y >>> 5) + y ^ sum + keyArray[(sum >>> 11) & 3];

        }
        numArray[0] = y;
        numArray[1] = z;
    }

    /**
     * decrypt block in place.
     * 
     * @param keyArray key
     * @param numArray value
     */
    public static void decryptBlock(int[] keyArray, int[] numArray) {
        int n = 32;
        int sum = 0xC6EF3720; // ?
        int y = numArray[0];
        int z = numArray[1];

        sum = DELTA << 5;
        /* start cycle */
        while (n-- > 0) {
            z -= (y << 4 ^ y >>> 5) + y ^ sum + keyArray[sum >>> 11 & 3];
            sum -= DELTA;
            y -= (z << 4 ^ z >>> 5) + z ^ sum + keyArray[sum & 3];
        }
        /* end cycle */
        numArray[0] = y;
        numArray[1] = z;
    }

    public static byte[] encrypt(int[] key, byte[] data) {

        int[] r = new int[2];
        int inputLength = data.length;
        byte[] result = new byte[data.length];
        int datax = 0;
        int resultx = 0;
        while (inputLength > 0) {
            r[0] = data[datax + 0] << 24 | (data[datax + 1] & 0xff) << 16 | (data[datax + 2] & 0xff) << 8
                   | data[datax + 3] & 0xff;
            r[1] = data[datax + 4] << 24 | (data[datax + 5] & 0xff) << 16 | (data[datax + 6] & 0xff) << 8
                   | data[datax + 7] & 0xff;
            encryptBlock(key, r);
            result[resultx + 0] = (byte)(r[0] >> 24);
            result[resultx + 1] = (byte)(r[0] >> 16 & 0xff);
            result[resultx + 2] = (byte)(r[0] >> 8 & 0xff);
            result[resultx + 3] = (byte)(r[0] & 0xff);

            result[resultx + 4] = (byte)(r[1] >> 24);
            result[resultx + 5] = (byte)(r[1] >> 16 & 0xff);
            result[resultx + 6] = (byte)(r[1] >> 8 & 0xff);
            result[resultx + 7] = (byte)(r[1] & 0xff);
            inputLength -= 8;
            resultx += 8;
            datax += 8;
        }
        return result;
    }

    public static byte[] decrypt(int[] key, byte[] data) {
        int[] r = new int[2];
        int inputLength = data.length;
        byte[] result = new byte[data.length];
        int resultx = 0;
        int datax = 0;
        while (inputLength > 0) {
            r[0] = data[datax + 0] << 24 | (data[datax + 1] & 0xff) << 16 | (data[datax + 2] & 0xff) << 8
                   | data[datax + 3] & 0xff;
            r[1] = data[datax + 4] << 24 | (data[datax + 5] & 0xff) << 16 | (data[datax + 6] & 0xff) << 8
                   | data[datax + 7] & 0xff;
            decryptBlock(key, r);
            result[resultx + 0] = (byte)(r[0] >> 24);
            result[resultx + 1] = (byte)(r[0] >> 16 & 0xff);
            result[resultx + 2] = (byte)(r[0] >> 8 & 0xff);
            result[resultx + 3] = (byte)(r[0] & 0xff);

            result[resultx + 4] = (byte)(r[1] >> 24);
            result[resultx + 5] = (byte)(r[1] >> 16 & 0xff);
            result[resultx + 6] = (byte)(r[1] >> 8 & 0xff);
            result[resultx + 7] = (byte)(r[1] & 0xff);
            inputLength -= 8;
            resultx += 8;
            datax += 8;
        }
        return result;
    }
}
