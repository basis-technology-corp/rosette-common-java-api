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

package com.basistech.rosette.internal.take5;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for exceptions generated mostly when reading binary format.
 */
public class Take5ExceptionTest extends Assert {

    /**
     * JUnit test that checks that returned messages and type numbers
     * are consistent with the meaning of their constants. 
     */
    @Test
    public void testMessagesAndTypes() throws Exception {    
        // record the messages
        List<String> messages = new ArrayList<String>();
        messages.add("File too short.");
        messages.add("Bad data.");
        messages.add("Wrong byte order.");
        messages.add("File is too new.");
        messages.add("File is too old.");
        messages.add("Unsupported engine.");
        messages.add("Unsupported value format.");
        messages.add("Value size mismatch.");
        messages.add("No numbers here.");
        messages.add("No pointers here.");
        messages.add("Unsupported state type.");
        messages.add("Entry point not found.");
        messages.add("Impossible hash.");
        messages.add("Buffer too small.");
        messages.add("Unsupported key check format.");
        messages.add("Unknown error type.");

        // create a Take5Exception of every flavor
        List<Take5Exception> exceptions = new ArrayList<Take5Exception>();
        exceptions.add(new Take5Exception(Take5Exception.FILE_TOO_SHORT));
        exceptions.add(new Take5Exception(Take5Exception.BAD_DATA));
        exceptions.add(new Take5Exception(Take5Exception.WRONG_BYTE_ORDER));
        exceptions.add(new Take5Exception(Take5Exception.FILE_TOO_NEW));
        exceptions.add(new Take5Exception(Take5Exception.FILE_TOO_OLD));
        exceptions.add(new Take5Exception(Take5Exception.UNSUPPORTED_ENGINE));
        exceptions.add(new Take5Exception(Take5Exception.UNSUPPORTED_VALUE_FORMAT));
        exceptions.add(new Take5Exception(Take5Exception.VALUE_SIZE_MISMATCH));
        exceptions.add(new Take5Exception(Take5Exception.NO_NUMBERS_HERE));
        exceptions.add(new Take5Exception(Take5Exception.NO_POINTERS_HERE));
        exceptions.add(new Take5Exception(Take5Exception.UNSUPPORTED_STATE_TYPE));
        exceptions.add(new Take5Exception(Take5Exception.ENTRY_POINT_NOT_FOUND));
        exceptions.add(new Take5Exception(Take5Exception.IMPOSSIBLE_HASH));
        exceptions.add(new Take5Exception(Take5Exception.BUFFER_TOO_SMALL));
        exceptions.add(new Take5Exception(Take5Exception.UNSUPPORTED_KEYCHECK_FORMAT));
        exceptions.add(new Take5Exception(259));

        // assert that the returned messages are correct
        for (int i = 1; i < 17; i++) {
            Assert.assertEquals(messages.get(i - 1), exceptions.get(i - 1).getMessage());
        }

        // assert that the type returned by getType() is correct
        for (int i = 1; i < 16; i++) {
            Assert.assertEquals(i, exceptions.get(i - 1).getType());
        }

        // the getType for the last exception should be 259 (from above)
        Assert.assertEquals(259, exceptions.get(15).getType()); 
    }

}
