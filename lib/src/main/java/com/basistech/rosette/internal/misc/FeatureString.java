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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.basistech.rosette.RosetteCorruptLicenseException;

public class FeatureString {

    private static final int[] TK = {
        0x404B6922, 0x13E035F9, 0x23EB0754, 0xCFC8926E
    };

    private Map<String, Feature> features;

    public FeatureString(String hex) throws RosetteCorruptLicenseException {
        byte[] data = Hex.hexToBytes(hex);
        byte[] dec = Tea.decrypt(TK, data);
        /*
         * The first 16 bytes are the MD5.
         */
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // should never happen.
            throw new RuntimeException(e);
        }
        algorithm.reset();
        algorithm.update(dec, 16, dec.length - 16);
        byte messageDigest[] = algorithm.digest();
        byte[] deliveredDigest = new byte[16];
        System.arraycopy(dec, 0, deliveredDigest, 0, 16);
        if (!Arrays.equals(messageDigest, deliveredDigest)) {
            throw new RosetteCorruptLicenseException();
        }

        String decAsString;
        try {
            decAsString = new String(dec, 16, dec.length - 16, "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        /*
         * Remove padding nulls at the end.
         */
        int zindex = decAsString.indexOf(0);
        if (zindex != -1) {
            decAsString = decAsString.substring(0, zindex);
        }
        String[] pieces = decAsString.split(";");
        features = new HashMap<String, Feature>();
        for (String piece : pieces) {
            int eqindex = piece.indexOf('=');
            String name;
            String value;
            if (eqindex == -1) {
                name = piece;
                value = null;
            } else {
                name = piece.substring(0, eqindex);
                value = piece.substring(eqindex + 1);
            }
            features.put(name, new Feature(name, value));
        }
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

}
