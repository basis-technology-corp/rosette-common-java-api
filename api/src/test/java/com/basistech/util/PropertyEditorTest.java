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

import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public class PropertyEditorTest extends AbstractDependencyInjectionSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[] {
            "classpath:com/basistech/util/test-util-enums.xml"
        };
    }

    @Test
    public void testProperties() throws Exception {
        UtilEnumPropertiesBean bean = (UtilEnumPropertiesBean)applicationContext.getBean("test-util-enum");
        assertEquals(bean.getLanguage(), LanguageCode.ARABIC);
        assertEquals(bean.getScript(), ISO15924.Arab);
        assertEquals(bean.getScheme(), TransliterationScheme.IC);
    }
}
