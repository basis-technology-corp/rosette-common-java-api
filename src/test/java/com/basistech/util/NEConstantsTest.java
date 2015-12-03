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
import static org.junit.Assert.assertTrue;

public class NEConstantsTest {
    @Test
    public void testParseBuiltInType() throws Exception {
        assertEquals(NEConstants.NE_TYPE_PERSON, NEConstants.parse("PERSON"));
    }

    @Test
    public void testParseBuiltInTypeWrongCase() throws Exception {
        // I wish we did not have to support this!
        assertEquals(NEConstants.NE_TYPE_PERSON, NEConstants.parse("pErSoN"));
    }

    @Test
    public void testParseCustomType() throws Exception {
        // I'd rather use @Test(expected=InvalidNamedEntityTypeNameException), but
        // we'd need to convert from junit.framework to org.junit.  Later...
        boolean gotIt = false;
        try {
            NEConstants.parse("CUSTOM_TYPE");
        } catch (InvalidNamedEntityTypeNameException e) {
            gotIt = true;
        }
        assertTrue(gotIt);
    }
}
