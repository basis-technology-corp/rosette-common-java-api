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
package com.basistech.util;

import junit.framework.TestCase;

public class TestISO15924 extends TestCase {

    public void testNumeric() {
        assertEquals(550, ISO15924.Blis.numeric());
    }

    public void testEnglishName() {
        assertEquals("Syriac (Eastern variant)", ISO15924.Syrn.englishName());
    }

    public void testLookupByNumeric() {
        assertEquals(ISO15924.Brah, ISO15924.lookupByNumeric(300));
    }

    public void testLookupByCode4() {
        assertEquals(ISO15924.Dsrt, ISO15924.lookupByCode4("Dsrt"));
    }

}
