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

import com.basistech.util.ISO15924;
import com.basistech.util.LanguageCode;

public class LanguageCodeTest {

    @Test
    public void testLanguageID() {
        LanguageCode lc = LanguageCode.ENGLISH;
        Assert.assertEquals(11, lc.languageID());
    }

    @Test
    public void testISO639() {
        LanguageCode lc = LanguageCode.ENGLISH;
        int rc = lc.ISO639_1().compareToIgnoreCase("En");
        Assert.assertEquals(0, rc);
    }

    @Test
    public void testGetDefaultScript() {
        LanguageCode lc = LanguageCode.ENGLISH;
        ISO15924 iso = lc.getDefaultScript();
        Assert.assertEquals(215, iso.numeric());
    }

    @Test
    public void testLanguageName() {
        LanguageCode lc = LanguageCode.ENGLISH;
        int rc = lc.name().compareToIgnoreCase("English");
        Assert.assertEquals(0, rc);
    }

    @Test
    public void testGetNativeCode() {
        LanguageCode lc = LanguageCode.ENGLISH;
        Assert.assertEquals(11, lc.languageID());
    }

    @Test
    public void testLookupByLanguageID() {
        LanguageCode lc = LanguageCode.lookupByLanguageID(11);
        int rc = lc.name().compareToIgnoreCase("English");
        Assert.assertEquals(0, rc);
    }

    @Test
    public void testLookupByISO639() {
        int language = LanguageCode.lookupByISO639("en").languageID();
        Assert.assertEquals(11, language);
    }

}
