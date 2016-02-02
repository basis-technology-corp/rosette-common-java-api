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

/**
 * Collection of linguistic properties of a text which are independent of its message; this currently includes
 * script, language, and transliteration scheme. Any text is in a domain (with zero or more components under
 * specified). Instances of this class are immutable.
 */
public class TextDomain implements Serializable, Comparable<TextDomain> {

    private ISO15924 theScript;
    private LanguageCode theLanguage;
    private TransliterationScheme theScheme;

    /**
     * Create a TextDomain object.
     * 
     * @param script the ISO 15924 numeric script id
     * @param language the ISO 639 numeric langauge id
     * @param scheme the TransliterationScheme
     */
    public TextDomain(ISO15924 script, LanguageCode language, TransliterationScheme scheme) {
        theScript = script;
        theLanguage = language;
        theScheme = scheme;
    }

    /**
     * Create a TextDomain object for the "native" domain of the language (see
     * LanguageCode.getDefaulatScript())
     * 
     * @param language the ISO 639 numeric langauge id
     */
    public TextDomain(LanguageCode language) {
        theScript = language.getDefaultScript();
        theLanguage = language;
        theScheme = TransliterationScheme.NATIVE;
    }
    /**
     * Create a TextDomain object 
     * 
     */
    TextDomain() {
        theScript = ISO15924.Zyyy;
        theLanguage = LanguageCode.UNKNOWN;
        theScheme = TransliterationScheme.UNKNOWN;
    }

    /**
     * Determine if the contents of this TextDomain object are equal those in the given object.
     * 
     * @param o object to compare
     * @return true if contents are equal.
     */
    public boolean equals(Object o) {
        if (!(o instanceof TextDomain)) {
            return false;
        }
        return this.getScript() == ((TextDomain)o).getScript()
               && this.getLanguage() == ((TextDomain)o).getLanguage()
               && this.getTransliterationScheme() == ((TextDomain)o).getTransliterationScheme();

    }

    /**
     * Returns the hash code for this TextDomain.
     * 
     * @return hash code
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getScript().hashCode();
        result = 37 * result + this.getLanguage().hashCode();
        result = 37 * result + this.getTransliterationScheme().hashCode();
        return result;
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
     * 
     */
    void setScript(ISO15924 script) {
        theScript = script;
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
     * 
     */
    void setLanguage(LanguageCode lang) {
        theLanguage = lang;
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
     * 
     */
    void setTransliterationScheme(TransliterationScheme scheme) {
        theScheme = scheme;
    }

    /**
     * Retrieve a string representation of the TextDomain.
     * 
     * @return String representation of the TextDomain.
     */
    public String toString() {
        return "[" + theScript.code4() + "/" + theLanguage.ISO639_3() + "/" + theScheme.getName() + "]";
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(TextDomain o) {
        if (theScript.numeric() > o.getScript().numeric()) {
            return 1;
        } else if (theScript.numeric() < o.getScript().numeric()) {
            return -1;
        }
        if (theLanguage.languageID() > o.getLanguage().languageID()) {
            return 1;
        } else if (theLanguage.languageID() < o.getLanguage().languageID()) {
            return -1;
        }
        if (theScheme.getNativeCode() > o.getTransliterationScheme().getNativeCode()) {
            return 1;
        } else if (theScheme.getNativeCode() < o.getTransliterationScheme().getNativeCode()) {
            return -1;
        }
 
        return 0;
    }
}
