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
package com.basistech.util.internal;

//CHECKSTYLE:OFF
public final class NumericalPrecision {

    public static final int DEFAULT_ULPS = 2;
    
    private NumericalPrecision() {
    }

    /**
     * Compare to floats using default precision (defaultUlps)
     * 
     * @param a first float
     * @param b second float
     * @return true or false
     */
    public static boolean AlmostEqual(double a, double b) {
        // TODO: someday we'll do something better for doubles
        return AlmostEqual(new Double(a).floatValue(), new Double(b).floatValue());
    }

    /**
     * Compare to floats using default precision (defaultUlps)
     * 
     * @param A first float
     * @param B second float
     * @return true or false
     */
    public static boolean AlmostEqual(float A, float B) {
        return AlmostEqual2sComplement(A, B, DEFAULT_ULPS);
    }

    // TODO: should move to com.basistech.util
    // Float.compare and float == float differ in canoncial ordering, neither seems to give precision options
    // adapted from http://www.cygnus-software.com/papers/comparingfloats/comparingfloats.htm
    /**
     * Compare to floats using maximum precision
     * 
     * @param A first float
     * @param B second float
     * @param maxUlps maximum precision
     * @return true or false
     */
    public static boolean AlmostEqual2sComplement(float A, float B, int maxUlps) {

        // Make sure maxUlps is non-negative and small enough that the
        // default NAN won't compare as equal to anything.
        if (!(maxUlps > 0 && maxUlps < 4 * 1024 * 1024)) {
            throw new IllegalArgumentException("!(" + maxUlps + " > 0 && " + maxUlps + " < 4 * 1024 * 1024");
        }
        int aInt = Float.floatToIntBits(A);
        // Make aInt lexicographically ordered as a twos-complement int
        if (aInt < 0) {
            aInt = 0x80000000 - aInt;
        }
        // Make bInt lexicographically ordered as a twos-complement int
        int bInt = Float.floatToIntBits(B);
        if (bInt < 0) {
            bInt = 0x80000000 - bInt;
        }
        int intDiff = Math.abs(aInt - bInt);
        if (intDiff <= maxUlps) {
            return true;
        }
        return false;

    }
}
