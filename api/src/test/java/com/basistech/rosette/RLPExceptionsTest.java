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
package com.basistech.rosette;

import com.basistech.util.LanguageCode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Exercises the code in the various Exceptions in the com.basistech.rlp
 * package.
 */
@SuppressWarnings("deprecation")
public class RLPExceptionsTest {

    @Test
    public void testRosetteException() {
        String message = "generic exception";
        
        // Use constructors
        RosetteException e = new RosetteException();
        e = new RosetteException(message);
        e = new RosetteException(new RuntimeException());
        e = new RosetteException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());        
    }
    
    @Test
    public void testRosetteRuntimeException() {
        String message = "runtime exception";
        
        // Use constructors
        RosetteRuntimeException e = new RosetteRuntimeException();
        e = new RosetteRuntimeException(message);
        e = new RosetteRuntimeException(new RuntimeException());
        e = new RosetteRuntimeException(message, new RuntimeException());
    
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }

    @Test
    public void testRosetteNoLicenseException() {
        String message = "no license found";
        
        // Use constructors
        RosetteNoLicenseException e = new RosetteNoLicenseException();
        e = new RosetteNoLicenseException(message);
        e = new RosetteNoLicenseException(new RuntimeException());
        e = new RosetteNoLicenseException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }

    @Test
    public void testRosetteCorruptLicenseException() {
        String message = "license has been corrupted";
        
        // Use constructors
        RosetteCorruptLicenseException e = new RosetteCorruptLicenseException();
        e = new RosetteCorruptLicenseException(message);
        e = new RosetteCorruptLicenseException(new RuntimeException());
        e = new RosetteCorruptLicenseException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }

    @Test
    public void testRosetteExpiredLicenseException() {
        String message = "license expired";
        
        // Use constructors
        RosetteExpiredLicenseException e = new RosetteExpiredLicenseException();
        e = new RosetteExpiredLicenseException(message);
        e = new RosetteExpiredLicenseException(new RuntimeException());
        e = new RosetteExpiredLicenseException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }

    @Test
    public void testRosetteUnsupportedLanguageExceptionDeprecated() {
        String message = "language not supported";
        
        // Use constructors
        RosetteUnsupportedLanguageException e = new RosetteUnsupportedLanguageException();
        e = new RosetteUnsupportedLanguageException(message);
        e = new RosetteUnsupportedLanguageException(new RuntimeException());
        e = new RosetteUnsupportedLanguageException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }

    @Test
    public void testRosetteUnsupportedLanguageException() {
        RosetteUnsupportedLanguageException e;
        e = new RosetteUnsupportedLanguageException(LanguageCode.AFRIKAANS);
        Assert.assertEquals(LanguageCode.AFRIKAANS, e.getLanguage());
        Assert.assertEquals("AFRIKAANS (afr)", e.getMessage());
        Assert.assertNull(e.getCause());
        e = new RosetteUnsupportedLanguageException(LanguageCode.AFRIKAANS, "foo");
        Assert.assertEquals(LanguageCode.AFRIKAANS, e.getLanguage());
        Assert.assertEquals("AFRIKAANS (afr): foo", e.getMessage());
        Assert.assertNull(e.getCause());
        e = new RosetteUnsupportedLanguageException(LanguageCode.AFRIKAANS, new RuntimeException());
        Assert.assertEquals(LanguageCode.AFRIKAANS, e.getLanguage());
        Assert.assertEquals("AFRIKAANS (afr)", e.getMessage());
        Assert.assertNotNull(e.getCause());
        e = new RosetteUnsupportedLanguageException(LanguageCode.AFRIKAANS, "foo", new RuntimeException());
        Assert.assertEquals(LanguageCode.AFRIKAANS, e.getLanguage());
        Assert.assertEquals("AFRIKAANS (afr): foo", e.getMessage());
        Assert.assertNotNull(e.getCause());
    }

    @Test
    public void testRosetteIllegalArgumentException() {
        RosetteIllegalArgumentException e;
        e = new RosetteIllegalArgumentException();
        Assert.assertNull(e.getMessage());
        Assert.assertNull(e.getCause());
        e = new RosetteIllegalArgumentException("foo");
        Assert.assertEquals("foo", e.getMessage());
        Assert.assertNull(e.getCause());
        e = new RosetteIllegalArgumentException(new RuntimeException());
        Assert.assertNotNull(e.getMessage());  // "java.lang.RuntimeException"
        Assert.assertNotNull(e.getCause());
        e = new RosetteIllegalArgumentException("foo", new RuntimeException());
        Assert.assertEquals("foo", e.getMessage());
        Assert.assertNotNull(e.getCause());
    }
}
