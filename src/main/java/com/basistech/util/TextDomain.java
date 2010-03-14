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
        return "[" + theScript.code4() + "/" + theLanguage.ISO639_1() + "/" + theScheme.getName() + "]";
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(TextDomain o) {
        int cv = theScript.code4().compareTo(o.getScript().code4());
        if (cv != 0) {
            return cv;
        }
        cv = theLanguage.ISO639_1().compareTo(o.getLanguage().ISO639_1());
        if (cv != 0) {
            return cv;
        }

        return theScheme.getName().compareTo(o.getTransliterationScheme().getName());
    }
}
