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


public class UtilEnumPropertiesBean {
    private ISO15924 script;
    private LanguageCode language;
    private TransliterationScheme scheme;

    public ISO15924 getScript() {
        return script;
    }

    public void setScript(ISO15924 script) {
        this.script = script;
    }

    public LanguageCode getLanguage() {
        return language;
    }

    public void setLanguage(LanguageCode language) {
        this.language = language;
    }

    public TransliterationScheme getScheme() {
        return scheme;
    }

    public void setScheme(TransliterationScheme scheme) {
        this.scheme = scheme;
    }
}
