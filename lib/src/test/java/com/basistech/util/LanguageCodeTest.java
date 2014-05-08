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

public class LanguageCodeTest extends TestCase {
    static final int SIZE_OF_ENUM = 83;
    static final int DONT_TEST_ID = -1;
    /**
     * Checks that different lookup paths are consistent with each other.
     */
    public void testValidLookups() {
        for (LanguageCode code : LanguageCode.values()) {
            if (!code.ISO639_1().equals(LanguageCode.UNCODED_ISO639_1)) {
                assertTrue(LanguageCode.ISO639IsValid(code.ISO639_1()));
                assertEquals(code, LanguageCode.lookupByISO639(code.ISO639_1()));
            }

            assertTrue(LanguageCode.ISO639IsValid(code.ISO639_3()));
            assertEquals(code, LanguageCode.lookupByISO639(code.ISO639_3()));

            assertTrue(LanguageCode.LanguageIDIsValid(code.languageID()));
            assertEquals(code, LanguageCode.lookupByLanguageID(code.languageID()));

            // Check lookup by code identifier (valueOf()). When we
            // generated this Enum we derived the code identifier from
            // the language name. This is white-box, but that's OK I
            // guess.
            String languageName = code.languageName();
            if (code.languageName().indexOf(',') != -1) { // reverse e.g. "Chinese, Simplified"
                String[] twoParts = code.languageName().split("[, ]+");
                languageName = twoParts[1] + "_" + twoParts[0];
            }
            languageName = languageName.toUpperCase().replaceAll("[ ]", "_").replaceAll("[^A-Z_]", "");
            assertEquals(code, LanguageCode.valueOf(languageName));
        }
    }

    @SuppressWarnings("unused")
    public void testInvalidLookups() {
        LanguageCode code;

        String[] invalidSymbols = {
            null, "", "Foobar"
        };
        int[] invalidIDs = {
            -42, 666
        };
        String[] invalidCodes = {
            null, "", "XX" /* regression tests UTILI-177 */, "@#", "FOO84R", "None",
            LanguageCode.UNCODED_ISO639_1
        };

        for (int invalidID : invalidIDs) {
            assertFalse(LanguageCode.LanguageIDIsValid(invalidID));

            try {
                code = LanguageCode.lookupByLanguageID(invalidID);
                fail("invalid argument");
            } catch (IllegalArgumentException e) {
            //
            }
        }

        for (String invalidCode : invalidCodes) {
            if (invalidCode == null) {
                try {
                    LanguageCode.ISO639IsValid(invalidCode);
                    fail("null argument");
                } catch (NullPointerException e) {
                    //
                }
            } else {
                assertFalse(LanguageCode.ISO639IsValid(invalidCode));
            }

            try {
                code = LanguageCode.lookupByISO639(invalidCode);
                fail("invalid argument");
            } catch (IllegalArgumentException e) {
                //
            } catch (NullPointerException e) {
                assertEquals(invalidCode, null);
            }
        }
    }

    /**
     * Spot-checks the actual data. Alternatively it would be reasonable to duplicate _all_ of the data in a
     * different format and check all the data instead of just a sample, but this way is cheaper.
     */
    public void testDataSpotCheck() {
        // Spot-check the first, the last, 3 other important ones, and
        // the total size. This is meant to catch errors where the
        // source file got corrupted, and to ensure that if a language
        // gets added, the test data gets updated.  Also test UNKNOWN,
        // since it has gets special mention in the spec.

        assertEquals(LanguageCode.values().length, SIZE_OF_ENUM);

        LanguageCode[] testSet = {
            LanguageCode.UNKNOWN, LanguageCode.ALBANIAN, LanguageCode.ENGLISH, LanguageCode.ARABIC,
            LanguageCode.JAPANESE, LanguageCode.TSONGA
        };
        String[][] stringResults = {
            {
                "Unknown", "xx", "xxx"
            }, {
                "Albanian", "sq", "sqi"
            }, {
                "English", "en", "eng"
            }, {
                "Arabic", "ar", "ara"
            }, {
                "Japanese", "ja", "jpn"
            }, {
                "Tsonga", "ts", "tso"
            },
        };
        int[] idResults = {
            0, 1, DONT_TEST_ID, DONT_TEST_ID, DONT_TEST_ID, LanguageCode.values().length - 1,
        };
        // This part is white box. We could also test the IDs of the
        // middle ones, but I'm not sure what that would add over
        // testing the IDs of the first and last.
        ISO15924[] scriptResults = {
            ISO15924.Zyyy, ISO15924.Latn, ISO15924.Latn, ISO15924.Arab, ISO15924.Hani, ISO15924.Latn,
        };

        for (int i = 0; i < testSet.length; ++i) {
            if (idResults[i] != DONT_TEST_ID) {
                assertEquals(testSet[i].languageID(), idResults[i]);
            }
            assertEquals(testSet[i].languageName(), stringResults[i][0]);
            assertEquals(testSet[i].ISO639_3(), stringResults[i][2]);
            if (!testSet[i].ISO639_1().equals(LanguageCode.UNCODED_ISO639_1)) {
                assertEquals(testSet[i].ISO639_1(), stringResults[i][1]);
            }
            assertEquals(testSet[i].getDefaultScript(), scriptResults[i]);
        }
    }

}
