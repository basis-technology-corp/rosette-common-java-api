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

import org.junit.Assert;
import org.junit.Test;

public class ISO15924UtilsTest extends Assert {

    @Test
    public void testScriptForString() {
        ISO15924 script = ISO15924Utils.scriptForString("This is just latin");
        assertEquals(ISO15924.Latn, script);
        script = ISO15924Utils.scriptForString("\u59d0\u59d1\u59d2");
        assertEquals(ISO15924.Hani, script);
    }

    @Test
    public void testForString() {
        ISO15924Utils utils = new ISO15924Utils(); 
        ISO15924 script = utils.forString("This is just latin");
        assertEquals(ISO15924.Latn, script);
        script = utils.forString("\u59d0\u59d1\u59d2");
        assertEquals(ISO15924.Hani, script);

    }

}
