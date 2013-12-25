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
 * <P>
 * The key in a Take5Pair is an arrays of chars.  For convenience, there
 * are constructors that accept strings and convert them to arrays of
 * chars.
 * <P>
 * The value in a Take5Pair is an array of bytes plus a required alignment
 * for those bytes in memory.  (The Java runtime doesn't care about
 * alignment issues, but the C runtime definitely does!)
 * <P>
 * The value in a Take5Pair can be null.  For convenience, there are
 * constructors that build Take5Pairs with a null value.
 * <P>
 * Because it is safe to re-use the same Take5Pair (and even the key)
 * repeatedly, we also provide methods to set the key and value.
 */
public class Take5Pair {
    char[] key;
    int keyLength;
    byte[] value;
    int alignment;
    int offset;
    int length;

    /**
     * Create a Take5Pair with the given key and the null value.
     */
    public Take5Pair(char[] key) {
        this.key = key;
        this.keyLength = key.length;
        this.value = null;
    }

    /**
     * Create a Take5Pair with the given key and the given value.  The
     * given array of bytes may not be modified after being passed to this
     * constructor, as the contents of the value will not be read until the
     * output is actually built!
     */
    public Take5Pair(char[] key, byte[] value, int alignment) {
        this.key = key;
        this.keyLength = key.length;
        setValue(value, alignment);
    }

    /**
     * Create a Take5Pair with the given key and the null value.
     */
    public Take5Pair(String key) {
        this(key.toCharArray());
    }

    /**
     * Create a Take5Pair with the given key and the given value.  The
     * given array of bytes may not be modified after being passed to this
     * constructor, as the contents of the value will not be read until the
     * output is actually built!
     */
    public Take5Pair(String key, byte[] value, int alignment) {
        this(key.toCharArray(), value, alignment);
    }

    /**
     * Set the key in a Take5Pair.
     */
    public final void setKey(char[] newKey) {
        key = newKey;
        keyLength = key.length;
    }

    /**
     * Set the key in a Take5Pair.
     */
    public final void setKey(String newKey) {
        key = newKey.toCharArray();
        keyLength = key.length;
    }

    /**
     * Set the length of the key in a Take5Pair.
     */
    public final void setKeyLength(int newKeyLength) {
        if (newKeyLength >= key.length) {
            throw new Take5BuilderException("Bad key length");
        }
        keyLength = newKeyLength;
    }

    /**
     * Set the value in a Take5Pair.  The given array of bytes may not be
     * modified after being passed to this method, as the contents of the
     * value will not be read until the output is actually built!
     */
    public final void setValue(byte[] newValue, int newAlignment) {
        value = newValue;
        if (newValue != null) {
            if (newAlignment <= 0) {
                throw new Take5BuilderException("Bad alignment");
            }
            alignment = newAlignment;
            offset = 0;
            length = newValue.length;
        }
    }

    /**
     * Set the value in a Take5Pair to the given subsequence.  The given
     * array of bytes may not be modified after being passed to this
     * method, as the contents of the value will not be read until the
     * output is actually built!
     */
    public final void setValue(byte[] newValue, int newAlignment, int newOffset, int newLength) {
        value = newValue;
        if (newValue != null) {
            if (newAlignment <= 0) {
                throw new Take5BuilderException("Bad alignment");
            }
            if (!(0 <= newOffset && 0 <= newLength && newOffset + newLength <= newValue.length)) {
                throw new Take5BuilderException("Bad offset or length");
            }
            alignment = newAlignment;
            offset = newOffset;
            length = newLength;
        }
    }

    public char[] getKey() {
        return key;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public byte[] getValue() {
        return value;
    }

    public int getAlignment() {
        return alignment;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }
}
