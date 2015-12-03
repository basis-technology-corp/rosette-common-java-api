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

import java.beans.PropertyEditorSupport;

/**
 * This class provides notational convenience for users of the Spring IoC container, or any other facility
 * that participates in the java.beans property editor discipline. JavaBeans automatically discovers
 * PropertyEditor classes if they are in the same package as the class they handle, and have the same name but
 * with an 'Editor' suffix. This class turns a String value into a valid value for a property of type
 * {@link com.basistech.util.TransliterationScheme}. Case is not significant; the string is mapped to upper case and then
 * converted.
 */
public class TransliterationSchemeEditor extends PropertyEditorSupport {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Enum.valueOf(TransliterationScheme.class, text.toUpperCase()));
    }

}
