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
