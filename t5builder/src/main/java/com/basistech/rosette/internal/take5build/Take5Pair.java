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

/**
 * A key-value pair for insertion into a {@link Take5EntryPoint}.
 * <P/>
 * The key in a Take5Pair is an arrays of chars.
 * <P/>
 * The value in a Take5Pair is an array of bytes plus a required alignment for those bytes in
 * memory.  (The Java runtime doesn't care about alignment issues, but the C runtime definitely
 * does!)
 * <P/>
 * The value in a Take5Pair can also be null.
 * <P/>
 * Note that {@link Take5EntryPoint#loadContent} does not save Take5Pairs between iterations.
 * Instead it immediately calls the accessors defined by this interface, saves the returned values, and
 * discards the Take5Pair.  Thus it is guaranteed to be safe to return the same
 * Take5Pair every time -- see {@link ReusableTake5Pair}.
 * <p/>
 * Note that the application is responsible for the byte order of the value.
 */
public interface Take5Pair {

    /**
     * Return the key for this pair.  Note that the length of the key is determined by calling
     * {@link #getKeyLength}, not by consulting the length of this array, so the actual key is
     * a prefix of this array.
     * <P>
     * Unlike the value, the returned array of bytes will be immediately read and discarded.
     * Thus it is guaranteed to be safe to return the same array every time.
     */
    char[] getKey();

    /**
     * Return the length of the key for this pair.
     */
    int getKeyLength();

    /**
     * Return the value for this pair.  Note that the actual value is a subsequence of this
     * array determined by calling {@link #getOffset} and {@link #getLength}.
     * <P>
     * The value can also be {@code null}.  
     * <P>
     * Unlike the key, the returned array of bytes must not be modified after being returned by
     * this accessor, as it will not be read until the output is actually built!
     */
    byte[] getValue();

    /**
     * Return the alignment of the value for this pair.  Probably a small power of 2...
     * <P>
     * Not called if the value is {@code null}.
     */
    int getAlignment();

    /**
     * Return the offset of the value for this pair within the array returned by {@link #getValue}.
     * <P>
     * Not called if the value is {@code null}.
     */
    int getOffset();

    /**
     * Return the length of the value for this pair within the array returned by {@link #getValue}.
     * <P>
     * Not called if the value is {@code null}.
     */
    int getLength();
}
