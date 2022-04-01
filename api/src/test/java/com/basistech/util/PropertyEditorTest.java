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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:com/basistech/util/test-util-enums.xml"})
public class PropertyEditorTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testProperties() throws Exception {
        UtilEnumPropertiesBean bean = (UtilEnumPropertiesBean)applicationContext.getBean("test-util-enum");
        assertEquals(bean.getLanguage(), LanguageCode.ARABIC);
        assertEquals(bean.getScript(), ISO15924.Arab);
        assertEquals(bean.getScheme(), TransliterationScheme.IC);
    }
}
