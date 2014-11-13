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

package com.basistech.rosette.internal.take5;

import com.basistech.rosette.RosetteRuntimeException;

import java.nio.ByteBuffer;

/**
 * Fluent builder for Take5 Dictionaries.
 */
public class Take5DictionaryBuilder {
    // data is in a buffer.
    private ByteBuffer data;
    // Entrypoint names.
    private String entrypoint = "main";
    // If the dictionary has the FSA engine (and is at least 5.6), copy it.
    private boolean copyFsaToHeap;

    /**
     * Start up a builder from data in a byte buffer.
     * If you want to use less than the full length, 'slice' the buffer.
     * @param data the data.
     */
    public Take5DictionaryBuilder(ByteBuffer data) {
        this.data = data;
    }

    /**
     * Construct a new dictionary by opening an entrypoint of an existing dictionary.
     * This is only useful if copyFsaToHeap is enabled.
     * @param existing an existing dictionary
     * @param entrypointName the new entrypoint to open
     * @return the new dictionary.
     * @throws Take5Exception
     */
    public static Take5Dictionary cloneAdditionalEntrypoint(Take5Dictionary existing, String entrypointName) throws Take5Exception {
        try {
            Take5Dictionary clone = existing.clone();
            clone.setEntryPoint(entrypointName);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RosetteRuntimeException(e);
        }
    }

    /**
     * Specify an entrypoint to use.
     * @param entrypoint the name of the entrypoint.
     * @return this.
     */
    public Take5DictionaryBuilder entrypoint(String entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    /**
     * Specify whether to copy the FSA into memory for a slight speed improvement.
     * @param copyFsaToHeap whether to copy.
     * @return this.
     */
    public Take5DictionaryBuilder copyFsaToHeap(boolean copyFsaToHeap) {
        this.copyFsaToHeap = copyFsaToHeap;
        return this;
    }

    /**
     * Build the dictionary.
     * @return the new dictionary.
     * @throws Take5Exception
     */
    public Take5Dictionary build() throws Take5Exception {
        //noinspection deprecation
        return new Take5Dictionary(data, data.capacity(), entrypoint, copyFsaToHeap);
    }


}
