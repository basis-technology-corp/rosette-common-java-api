/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.basistech.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TextDomainTest {

    private TextDomain td1;
    private TextDomain td2;
    private TextDomain td3;
    private TextDomain tdDefault = new TextDomain();

    @Before
    public void setUp() {
        td1 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
        td2 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
        td3 = new TextDomain(ISO15924.Zyyy, LanguageCode.UNKNOWN, TransliterationScheme.BASIS);
    }

    @Test
    public void testequals() {
        // reflexive
        assertEquals(td1, td1);
        // symmetric
        assertTrue(td1.equals(td2) && td2.equals(td1));
        // transitive
        assertTrue(td1.equals(td2) && td2.equals(td3) && td3.equals(td1));
        // consistent
        assertTrue(td1.equals(td2) && td1.equals(td2) && td1.equals(td2));
        // null is false
        assertNotNull(td1);
    }

    @Test
    public void testNativeDomainConstructor() {

        TextDomain tdArabicByHand = new TextDomain(ISO15924.Arab, LanguageCode.ARABIC,
                                                   TransliterationScheme.UNKNOWN);
        TextDomain tdArabicNative = new TextDomain(LanguageCode.ARABIC);
        assertTrue(tdArabicByHand.equals(tdArabicNative));

    }

    @Test
    public void testhashCode() {
        assertTrue(td1.hashCode() == td2.hashCode());
        assertTrue(td2.hashCode() == td3.hashCode());
        assertNotEquals(td1.hashCode(), tdDefault.hashCode());
    }

    @Test
    public void testToString() {
        // Make sure we're using 3-letter language codes so that we can tell these apart when
        // we're debugging:
        TextDomain prs = new TextDomain(LanguageCode.DARI);
        TextDomain pes = new TextDomain(LanguageCode.WESTERN_FARSI);
        assertFalse(prs.toString().equals(pes.toString()));
        // test that we don't crash on some null cases.
        TextDomain ntd = new TextDomain(null, null, null);
        // some silly asserts to try to shut up intelliJ.
        assertNotNull(ntd.toString());
        assertNotNull(ntd.hashCode());
        assertEquals(ntd, ntd);
        assertNotNull(ntd.equals(prs));
        assertNotNull(pes.equals(ntd));
    }
}
