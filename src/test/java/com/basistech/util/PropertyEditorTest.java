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
