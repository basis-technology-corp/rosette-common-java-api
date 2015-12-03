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

import org.junit.Assert;
import org.junit.Test;

/**
 * Exercises the code in the various Exceptions in the com.basistech.rlp
 * package.
 * 
 * Classes covered:
 *    RosetteException
 *    RosetteRuntimeException
 *    RosetteNoLicenseException
 *    RosetteCorruptLicenseException
 *    RosetteExpiredLicenseException
 *    RosetteUnsupportedLanguageException
 */
public class RLPExceptionsTest extends Assert {

    @Test
    public void testRosetteException() throws Exception {
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
    public void testRosetteRuntimeException() throws Exception {
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
    public void testRosetteNoLicenseException() throws Exception {
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
    public void testRosetteCorruptLicenseException() throws Exception {
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
    public void testRosetteExpiredLicenseException() throws Exception {
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
    public void testRosetteUnsupportedLanguageException() throws Exception {
        String message = "language not supported";
        
        // Use constructors
        RosetteUnsupportedLanguageException e = new RosetteUnsupportedLanguageException();
        e = new RosetteUnsupportedLanguageException(message);
        e = new RosetteUnsupportedLanguageException(new RuntimeException());
        e = new RosetteUnsupportedLanguageException(message, new RuntimeException());
        
        // test the message
        Assert.assertEquals(message, e.getMessage());
    }
}
