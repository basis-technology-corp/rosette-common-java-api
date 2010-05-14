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


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.basistech.rosette.RosetteExpiredLicenseException;
import com.basistech.rosette.RosetteNoLicenseException;
import com.basistech.util.LanguageCode;

public class LManagerTest {
    private LManager manager;
    private LManager managerWithExpired;
    
    @Before
    public void before() throws Exception {
        manager = new LManager(getClass().getResourceAsStream("rlp-license.xml"));
        managerWithExpired = 
            new LManager(getClass().getResourceAsStream("expired-license.xml"));
    }

    @Test
    public void testLicenseLookups() throws Exception {
        manager.checkFeature("RLI", 0, true);
        manager.checkLanguage(LanguageCode.ARABIC, 1, false);
    }
    
    @Test
    public void testLicenseUnknownLanguage() throws Exception {
        Assert.assertTrue(manager.checkLanguage(LanguageCode.UNKNOWN, 1, false));
    }
    
    @Test
    public void testLicenseEnUCLanguage() throws Exception {
        Assert.assertTrue(manager.checkLanguage(LanguageCode.ENGLISH_UPPERCASE, 1, false));
    }
    
    @Test(expected = RosetteNoLicenseException.class)
    public void checkThrowNoFeature() throws Exception {
        manager.checkFeature("ILR", 0, true);
    }
    
    @Test(expected = RosetteNoLicenseException.class)
    public void checkThrowNoFeatureFunction() throws Exception {
        manager.checkFeature("RLI", 2, true);
    }
    
    @Test(expected = RosetteNoLicenseException.class)
    public void checkThrowNoLanguage() throws Exception {
        manager.checkLanguage(LanguageCode.TAMIL, 0, true);
    }
    
    @Test(expected = RosetteNoLicenseException.class)
    public void checkThrowNoLanguageFunction() throws Exception {
        manager.checkFeature("ar", 0x10000000, true);
    }
    
    @org.junit.Ignore
    @Test(expected = RosetteExpiredLicenseException.class)
    public void checkThrowExpiredLicense() throws Exception {
        managerWithExpired.checkLanguage(LanguageCode.ARABIC, 0x2, true);
    }

}
