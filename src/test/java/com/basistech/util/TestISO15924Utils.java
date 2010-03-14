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

public class TestISO15924Utils extends TestCase {

    public void testArab() {
        String barackObamaInArabic = 
                                     "\u0628\u0627\u0631\u0627\u0643 \u0627\u0648\u0628\u0627\u0645\u0627";
        assertEquals(ISO15924.Arab, ISO15924Utils.scriptForString(barackObamaInArabic));
    }

    public void testHani() {
        String barackObamaInChinese = 
            "\u5df4\u62c9\u514b \u5965\u5df4\u9a6c";
        assertEquals(ISO15924.Hani, ISO15924Utils.scriptForString(barackObamaInChinese));
    }

}
