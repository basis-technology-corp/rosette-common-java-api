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

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.basistech.rosette.RosetteCorruptLicenseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LFileBuilderTest {

    @Test
    public void testLFileBuilder() throws Exception {
        InputStream is = getClass().getResourceAsStream("rlp-license.xml");
        LFile result = LFileBuilder.parse(is);
        assertEquals("ken", result.getGenerator());
        assertEquals("Basis Internal Development", result.getCustomer());
        assertEquals("Perpetual", result.getExpiration());
        List<Entry> entries = result.getEntries();
        assertEquals(4, entries.size());
        Entry e = entries.get(0);
        assertEquals("RLP", e.getProduct());
        assertEquals("RLI", e.getFeature());
        assertNull(e.getLanguage());
        assertEquals(
                     //CHECKSTYLE:OFF
                     "56AD6A856A4FA972509FD34AD3AB53F18297266B8B248CEB046FC5021E0A624E4BAC7BB37B76F95BDA74F1A62B43742F2BD308EE7DE5A40AFFCA02D550CC2645BA69AFF9E667E92E",
                     //CHECKSTYLE:ON
                     e.getFeatureString());
        e = entries.get(2);
        // spotcheck
        assertEquals("PLR", e.getProduct());
    }
    
    @Test
    public void testLFInvalid() throws Exception {
        String badLF = "Invalid License File";
        try {
            LFileBuilder.parse(badLF);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof RosetteCorruptLicenseException);
            return;
        }
        throw new Exception("License file should not have been parsed...");
    }
}
