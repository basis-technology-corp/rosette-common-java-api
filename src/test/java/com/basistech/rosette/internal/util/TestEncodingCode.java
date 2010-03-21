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
package com.basistech.rosette.internal.util;

import org.junit.Assert;
import org.junit.Test;

import com.basistech.rosette.util.EncodingCode;

public class TestEncodingCode {

    @Test
    public void testGetMimeName() {
        Assert.assertEquals("IBM866", EncodingCode.Cp866.getMimeName());
    }

    @Test
    public void testGetTypeName() {
        Assert.assertEquals("Cp866", EncodingCode.Cp866.getTypeName());
    }

    @Test
    public void testGetDemotionTypeName() {
        Assert.assertEquals("Ascii", EncodingCode.Cp866.getDemotionTypeName());
    }

    @Test
    public void testLookupByMimeName() {
        Assert.assertEquals(6, EncodingCode.lookupByMimeName("IBM866").ordinal());
        try {
            Assert.assertEquals(6, EncodingCode.lookupByMimeName("bugus").ordinal());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testLookupByTypeName() {
        Assert.assertEquals(6, EncodingCode.lookupByTypeName("Cp866").ordinal());
        try {
            Assert.assertEquals(6, EncodingCode.lookupByTypeName("bugus").ordinal());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

}
