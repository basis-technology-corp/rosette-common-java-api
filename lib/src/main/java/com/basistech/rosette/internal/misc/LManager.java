/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/
package com.basistech.rosette.internal.misc;

import com.basistech.rosette.RosetteCorruptLicenseException;
import com.basistech.rosette.RosetteExpiredLicenseException;
import com.basistech.rosette.RosetteNoLicenseException;
import com.basistech.util.LanguageCode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The license manager.
 */
public class LManager {

    // Magic strings used to allow license circumvention.
    private static String sk1 = "n   x"; // use substring(1, 3)
    private static String sk2 = "Jx^A@B1"; //^A^@
    private static String sk3 = "jmnx0exxp1IWab6b";
    private LFile licenseFile;
    private List<LEntry> languageEntries;
    private List<LEntry> featureEntries;
    private String token;

    /**
     * Create a license manager that reads license
     * data from the specified XML String.
     * @param xmlLicense License string in XML.
     * @throws RosetteCorruptLicenseException
     */
    public LManager(String xmlLicense) throws RosetteCorruptLicenseException {
        licenseFile = LFileBuilder.parse(xmlLicense);
        initialize();
    }

    /**
     * Create a license manager that reads license
     * data from the specified stream.
     * @param xmlStream Stream that contains the license in XML.
     * @throws RosetteCorruptLicenseException
     */
    public LManager(InputStream xmlStream) throws RosetteCorruptLicenseException {
        licenseFile = LFileBuilder.parse(xmlStream);
        initialize();
    }

    /*
     * L Language Code ISO-639 language code this license applies to. (Required, unless we have Q) Q Feature
     * The name of a licensed feature other than a language. This could be done by licencing against L=xx, but
     * that seems completely bizarre. E Expiration Date Unix epoch after which the license is no longer valid.
     * A value of (time_t)-1 indicates a perpetual license. (Required) F Functions Hexadecimal number
     * representing the set of functions licensed for the language, using the bits defined in the Type_Bits
     * enumeration in the BT_RLP_Language_Processor class declaration. (Required)
     */

    private void initialize() throws RosetteCorruptLicenseException {
        if (licenseFile.getToken() != null) {
            token = licenseFile.getToken();
        }
        languageEntries = new ArrayList<LEntry>();
        featureEntries = new ArrayList<LEntry>();
        List<Entry> entries = licenseFile.getEntries();
        for (Entry e : entries) {
            FeatureString fs = new FeatureString(e.getFeatureString());
            Map<String, Feature> fsFeatures = fs.getFeatures();
            Feature langFeature = fsFeatures.get("L");
            Feature featFeature = fsFeatures.get("Q");
            Feature expFeature = fsFeatures.get("E");
            Feature fcnFeature = fsFeatures.get("F");
            LEntry lentry = new LEntry();
            long exp;
            boolean expired;
            if (expFeature != null) {
                long epoch = System.currentTimeMillis() / 1000;
                exp = Integer.parseInt(expFeature.getValue());
                if (exp == -1) {
                    expired = false;
                } else {
                    expired = epoch > exp;
                }
            } else {
                exp = -1;
                expired = false;
            }
            lentry.setExpiration(exp);
            lentry.setExpired(expired);
            int fcns = 0;
            if (fcnFeature != null) {
                fcns = Integer.parseInt(fcnFeature.getValue(), 16);
            }
            lentry.setFunctions(fcns);
            if (langFeature != null) {
                lentry.setLanguage(langFeature.getValue());
                languageEntries.add(lentry);
            } else if (featFeature != null) {
                lentry.setFeature(featFeature.getValue());
                featureEntries.add(lentry);
            }
        }
    }

    private boolean checkInternal() {
        String sk = new StringBuilder()
                // "  ^A^@IWe   "
                .append(sk1.substring(1, 3)) // "  "
                .append(sk2.substring(2, 4)) // "^A"
                .append(sk2.substring(2, 3)) // "^"
                .append(sk2.substring(4, 5)) // "@"
                .append(sk3.substring(10, 12)) // "IW"
                .append(sk3.substring(5, 6)) // "e"
                .append(sk1.substring(1, 4)) // "   "
                .toString();
        return sk.equals(token);
    }

    public boolean checkLanguage(LanguageCode languageCode,
                                 int functions, boolean throwErrors) throws RosetteNoLicenseException,
            RosetteExpiredLicenseException {
        // license key "  ^A^@IWe   " avoids license checks
        if (token != null) {
            if (checkInternal()) {
                return true;
            }
            if (throwErrors) {
                throw new RosetteNoLicenseException("No license present for language " + languageCode.languageName());
            }
            return false;
        }

        // skipping license check for language unknown since it is not required
        if (languageCode == LanguageCode.UNKNOWN) {
            return true;
        }
        // treat Uppercase English language code as English since we do not need separation at this level
        if (languageCode == LanguageCode.ENGLISH_UPPERCASE) {
            languageCode = LanguageCode.ENGLISH;
        }
        for (LEntry entry : languageEntries) {
            if (LanguageCode.lookupByISO639(entry.getLanguage()) == languageCode
                    && (functions == 0 || 0 != (functions & entry.getFunctions()))) {
                /*
                 * Assume only one function bit at a time.
                 */
                return checkExpiration(entry, throwErrors, "language " + languageCode.languageName());
            }
        }
        if (throwErrors) {
            throw new RosetteNoLicenseException("No license present for language " + languageCode.languageName());
        } else {
            return false;
        }
    }

    public boolean checkFeature(String feature, int functions, boolean throwErrors)
            throws RosetteNoLicenseException, RosetteExpiredLicenseException {

        // license key "  ^A^@IWe   " avoids license checks
        if (token != null) {
            if (checkInternal()) {
                return true;
            }
            if (throwErrors) {
                throw new RosetteNoLicenseException("No license present for feature " + feature);
            }
            return false;
        }

        for (LEntry entry : featureEntries) {
            if (feature.equals(entry.getFeature())
                    && (0 == functions || 0 != (functions & entry.getFunctions()))) {
                /*
                 * Assume only one function bit at a time.
                 */
                return checkExpiration(entry, throwErrors, "feature " + feature);
            }
        }
        if (throwErrors) {
            throw new RosetteNoLicenseException("No license present for feature " + feature);
        } else {
            return false;
        }
    }

    private boolean checkExpiration(LEntry entry, boolean throwErrors, String explanation)
            throws RosetteExpiredLicenseException {
        if (entry.isExpired()) {
            if (throwErrors) {
                throw new RosetteExpiredLicenseException("License for " + explanation + " expired.");
            } else {
                return false;
            }
        }
        return true;
    }
}
