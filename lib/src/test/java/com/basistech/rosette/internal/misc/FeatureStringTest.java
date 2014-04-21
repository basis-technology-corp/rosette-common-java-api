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

import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FeatureStringTest {

    @Test
    public void testFeatureString() throws Exception {
        String hex = "98F8FB41174E3AAA9827486C8C7EE0FC55906FB42983F96C4823FA706CEE1382";
        FeatureString fs = new FeatureString(hex);
        Map<String, Feature> feats = fs.getFeatures();
        assertEquals(2, feats.size());
        Feature fooF = feats.get("Foo");
        assertEquals("Foo", fooF.getName());
        assertEquals("1", fooF.getValue());
        Feature barF = feats.get("Bar");
        assertEquals("Bar", barF.getName());
        assertEquals("2", barF.getValue());

        hex = "662ABAB9FA7928D1AEE962641C60591655906FB42983F96CD364B96AA5B5D64A";
        fs = new FeatureString(hex);
        feats = fs.getFeatures();
        assertEquals(2, feats.size());
        fooF = feats.get("Foo");
        assertEquals("Foo", fooF.getName());
        assertEquals("1", fooF.getValue());
        barF = feats.get("Bar");
        assertEquals("Bar", barF.getName());
        assertNull(barF.getValue());
    }

}
