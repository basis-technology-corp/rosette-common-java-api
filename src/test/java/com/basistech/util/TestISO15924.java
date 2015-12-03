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

import static org.junit.Assert.assertEquals;

public class TestISO15924 {

    @Test
    public void testNumeric() {
        assertEquals(550, ISO15924.Blis.numeric());
    }

    @Test
    public void testEnglishName() {
        assertEquals("Syriac (Eastern variant)", ISO15924.Syrn.englishName());
    }

    @Test
    public void testLookupByNumeric() {
        assertEquals(ISO15924.Brah, ISO15924.lookupByNumeric(300));
    }

    @Test
    public void testLookupByCode4() {
        assertEquals(ISO15924.Dsrt, ISO15924.lookupByCode4("Dsrt"));
    }

}
