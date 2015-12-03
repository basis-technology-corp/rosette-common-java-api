/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2013 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.util;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NEConstantsTest {
    @Test
    public void testParseBuiltInType() throws Exception {
        assertEquals(NEConstants.NE_TYPE_PERSON, NEConstants.parse("PERSON"));
    }

    @Test
    public void testParseBuiltInTypeWrongCase() throws Exception {
        // I wish we did not have to support this!
        assertEquals(NEConstants.NE_TYPE_PERSON, NEConstants.parse("pErSoN"));
    }

    @Test
    public void testParseCustomType() throws Exception {
        // I'd rather use @Test(expected=InvalidNamedEntityTypeNameException), but
        // we'd need to convert from junit.framework to org.junit.  Later...
        boolean gotIt = false;
        try {
            NEConstants.parse("CUSTOM_TYPE");
        } catch (InvalidNamedEntityTypeNameException e) {
            gotIt = true;
        }
        assertTrue(gotIt);
    }
}
