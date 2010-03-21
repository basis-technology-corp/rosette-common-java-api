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

/**
 * Utility class to convert hex strings to byte arrays. We don't need the opposite direction.
 */
public final class Hex {
    private Hex() {
    }

    public static byte[] hexToBytes(String hex) {
        byte[] result = new byte[hex.length() / 2];
        for (int x = 0; x < hex.length(); x = x + 2) {
            String charHex = hex.substring(x, x + 2);
            result[x / 2] = (byte)Integer.parseInt(charHex, 16);
        }
        return result;
    }

}
