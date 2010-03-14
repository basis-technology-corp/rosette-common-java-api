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

public class TextDomainTest extends TestCase {

    private TextDomain td1;
    private TextDomain td2;
    private TextDomain td3;

    public void setUp() {
        // have to ref script/language by int directly because the respective classes haven't yet moved from
        // rlp
        td1 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
        td2 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
        td3 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
    }

    public void testequals() {
        // reflexive
        assertTrue(td1.equals(td1));
        // symmetric
        assertTrue(td1.equals(td2) && td2.equals(td1));
        // transitive
        assertTrue(td1.equals(td2) && td2.equals(td3) && td3.equals(td1));
        // consistent
        assertTrue(td1.equals(td2) && td1.equals(td2) && td1.equals(td2));
        // null is false
        assertNotNull(td1);
    }

    public void testNativeDomainConstructor() {

        TextDomain tdArabicByHand = new TextDomain(ISO15924.Arab, LanguageCode.ARABIC,
                                                   TransliterationScheme.NATIVE);
        TextDomain tdArabicNative = new TextDomain(LanguageCode.ARABIC);
        assertTrue(tdArabicByHand.equals(tdArabicNative));

    }

    public void testhashCode() {
        assertTrue(td1.hashCode() == td2.hashCode());
        assertTrue(td2.hashCode() == td3.hashCode());
    }
}
