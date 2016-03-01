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

import java.io.Serializable;
import java.util.Objects;

/**
 * Collection of linguistic properties of a text which are independent of its message; this currently includes
 * script, language, and transliteration scheme. Any text is in a domain (with zero or more components under
 * specified).
 */
public class TextDomain implements Serializable, Comparable<TextDomain> {

    private ISO15924 theScript;
    private LanguageCode theLanguage;
    private TransliterationScheme theScheme;

    /**
     * Create a TextDomain object.
     * {@code null} values supplied here are mapped to their corresponding defaults.
     * 
     * @param script the ISO 15924 code. {@code null} is mapped to {@link ISO15924#Zyyy}.
     * @param language the language code. {@code null} is mapped to {@link LanguageCode#UNKNOWN}.
     * @param scheme the TransliterationScheme. {@code null} is mapped to {@link TransliterationScheme#UNKNOWN}.
     */
    public TextDomain(ISO15924 script, LanguageCode language, TransliterationScheme scheme) {
        theScript = script == null ? ISO15924.Zyyy : script;
        theLanguage = language == null ? LanguageCode.UNKNOWN : language;
        theScheme = scheme == null ? TransliterationScheme.UNKNOWN : scheme;
    }

    /**
     * Create a TextDomain object for the "native" domain of the language (see
     * LanguageCode.getDefaultScript())
     * 
     * @param language the language.
     *                 {@code null} is mapped to {@link LanguageCode#UNKNOWN}.
     */
    public TextDomain(LanguageCode language) {
        if (language == null) {
            language = LanguageCode.UNKNOWN;
        }
        theScript = language.getDefaultScript();
        theLanguage = language;
        theScheme = TransliterationScheme.NATIVE;
    }
    /**
     * Create a TextDomain object with
     * unspecified values.
     * 
     */
    TextDomain() {
        theScript = ISO15924.Zyyy;
        theLanguage = LanguageCode.UNKNOWN;
        theScheme = TransliterationScheme.UNKNOWN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TextDomain that = (TextDomain) o;
        return theScript == that.theScript
                && theLanguage == that.theLanguage
                && theScheme == that.theScheme;
    }

    @Override
    public int hashCode() {
        return Objects.hash(theScript, theLanguage, theScheme);
    }

    /**
     * Get the script of this domain.
     * 
     * @return script of this domain
     */
    public ISO15924 getScript() {
        return theScript;
    }

    /**
     * Set the script of this domain.
     * @param script the new script value.
     *     {@code null} is mapped to {@link ISO15924#Zyyy}.
     * 
     */
    @Deprecated
    void setScript(ISO15924 script) {
        theScript = script == null ? ISO15924.Zyyy : script;
    }

    /**
     * Get the language of this domain.
     * 
     * @return language of this domain
     */
    public LanguageCode getLanguage() {
        return theLanguage;
    }

    /**
     * Set the language of this domain.
     * @param lang the new language value.
     *             {@code null} is mapped to {@link LanguageCode#UNKNOWN}.
     * 
     */
    @Deprecated
    void setLanguage(LanguageCode lang) {
        theLanguage = lang == null ? LanguageCode.UNKNOWN : lang;
    }
 
    /**
     * Get the transliteration scheme of this domain.
     * 
     * @return transliteration scheme of this domain
     */
    public TransliterationScheme getTransliterationScheme() {
        return theScheme;
    }

    /**
     * set the transliteration scheme of this domain.
     * @param scheme the new scheme value.
     *               {@code null} is mapped to {@link TransliterationScheme#UNKNOWN}.
     * 
     */
    @Deprecated
    void setTransliterationScheme(TransliterationScheme scheme) {
        theScheme = scheme == null ? TransliterationScheme.UNKNOWN : scheme;
    }

    /**
     * Retrieve a string representation of the TextDomain.
     * 
     * @return String representation of the TextDomain.
     */
    public String toString() {
        return "["
                + theScript.code4()
                + "/"
                + theLanguage.ISO639_3()
                + "/"
                + theScheme.getName()
                + "]";
    }

    public int compareTo(TextDomain o) {
        int n = theScript.numeric();
        int otherN = o.getScript().numeric();

        if (n > otherN) {
            return 1;
        } else if (n < otherN) {
            return -1;
        }

        n = theLanguage.languageID();
        otherN = o.getLanguage().languageID();

        if (n > otherN) {
            return 1;
        } else if (n < otherN) {
            return -1;
        }

        n = theScheme.getNativeCode();
        otherN = o.getTransliterationScheme().getNativeCode();

        if (n > otherN) {
            return 1;
        } else if (n < otherN) {
            return -1;
        }

        return 0;
    }
}
