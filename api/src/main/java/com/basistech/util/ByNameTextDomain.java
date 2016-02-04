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
