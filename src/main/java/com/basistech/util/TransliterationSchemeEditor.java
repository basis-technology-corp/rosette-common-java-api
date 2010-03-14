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

import java.beans.PropertyEditorSupport;

/**
 * This class provides notational convenience for users of the Spring IoC container, or any other facility
 * that participates in the java.beans property editor discipline. JavaBeans automatically discovers
 * PropertyEditor classes if they are in the same package as the class they handle, and have the same name but
 * with an 'Editor' suffix. This class turns a String value into a valid value for a property of type
 * {@link TransliterationScheme}. Case is not significant; the string is mapped to upper case and then
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
