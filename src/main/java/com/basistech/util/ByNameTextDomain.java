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

/**
 * When using TextDomain in a Spring environment, this class allows the XML file to create TextDomain values.
 * A Spring XML file can create a series of these, in particular to make it easier to initialize a rule-based
 * name translator. The three parameters of the text domain are specified at construction, since TextDomain
 * objects themselves are immutable.
 * 
 * @since 2.0
 */
public class ByNameTextDomain extends TextDomain {

    /**
     * Initialize the text domain object.
     * 
     * @param script The 'code4' representation of the script (e.g. Arab).
     * @param language The ISO649 code for the language. (e.g. FA).
     * @param scheme The transliteration scheme. (e.g. BGN).
     */
    public ByNameTextDomain(String script, String language, String scheme) {
        super(ISO15924.lookupByCode4(script), LanguageCode.lookupByISO639(language), TransliterationScheme
            .getObjectByName(scheme));
    }
}
