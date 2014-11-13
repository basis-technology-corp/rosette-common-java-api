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

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.nio.MappedByteBuffer;

import static org.junit.Assert.assertTrue;

/**
 * Test of the new builder and the feature of controlling copies.
 */
public class Take5DictionaryCopyControlTest {

    @Test
    public void copyControl() throws Exception {
        MappedByteBuffer buffer = Files.map(new File("test_dicts/days-56.bin"));
        Take5Dictionary dict = new Take5DictionaryBuilder(buffer).copyFsaToHeap(false).build();
        assertTrue(dict.getFsaGuts() instanceof NoncopyingFsaEngine);

        dict = new Take5DictionaryBuilder(buffer).copyFsaToHeap(true).build();
        assertTrue(dict.getFsaGuts() instanceof CopyingFsaEngine);
    }
}
