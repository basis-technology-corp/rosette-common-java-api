/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2012 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import com.basistech.internal.util.TabReader;
import junit.framework.TestCase;

import com.basistech.internal.util.TabReader.Line;

public class TabReaderTest extends TestCase {

    /** Get a File handle for a path from the resource directory. 
     * @throws FileNotFoundException 
     * @throws URISyntaxException */
    File getFile(String fname) throws FileNotFoundException, URISyntaxException {
        URL resource = getClass().getResource(fname);
        if (resource == null) {
            throw new FileNotFoundException();
        }
        return new File(resource.toURI());
    }
    
    public void testMissing() throws Exception {
        boolean fnf = false;
        try {
            TabReader.getAll(getFile("does-not-exist.txt"), 5, "#");
        } catch (FileNotFoundException e) {
            fnf = true;
        }
        assertTrue("Should have failed to find file.", fnf);
        
        for (Line x : TabReader.iterate((Reader) null, 5, "#")) {
            fail("Should not find any lines.");
            x.getData(); //avoid warning
        }
    }

    public void testLoading() throws Exception {
        List<Line> actual = TabReader.getAll(getFile("3-token.txt"), 3, "#");
        assertEquals(2, actual.size());
        
        Line l1 = actual.get(0);
        assertEquals(1, l1.getLineNumber());
        assertEquals(3, l1.getData().length);
        assertEquals("a -  - c", String.format("%s - %s - %s", (Object[]) l1.getData()));
        
        Line l4 = actual.get(1);
        assertEquals(4, l4.getLineNumber());
        assertEquals(3, l4.getData().length);
        assertEquals("d - e - fff ff", String.format("%s - %s - %s", (Object[]) l4.getData()));
    }
    
    public void testReader() throws IOException {
        Reader r = new StringReader(" a \t b  \n  c \t d ");
        assertEquals(2, TabReader.getAll(r, 2, "#").size());
        r.reset();
        
        int count = 0;
        for (Line x : TabReader.iterate(r, 2, "#")) {
            count++;
            x.getData(); //avoid warning
        }
        assertEquals(2, count);
        r.reset();
    }
    
    public void testNoComments() throws Exception {
        List<Line> actual = TabReader.getAll(getFile("3-token.txt"), 3, null);
        assertEquals(3, actual.size());
        assertNotSame(-1, actual.get(0).getData()[2].indexOf("#"));
    }
    
    public void testVariableColumns() throws Exception {
        List<Line> actual = TabReader.getAll(getFile("variable-column.txt"), -1, "#");
        assertEquals(3, actual.size());
        
        Line l3 = actual.get(0);
        assertEquals(3, l3.getLineNumber());
        assertEquals(2, l3.getData().length);
        assertEquals("_", String.format("%s_%s", (Object[]) l3.getData()));
        
        Line l4 = actual.get(1);
        assertEquals(4, l4.getLineNumber());
        assertEquals(3, l4.getData().length);
        assertEquals("__f", String.format("%s_%s_%s", (Object[]) l4.getData()));
        
        Line l5 = actual.get(2);
        assertEquals(5, l5.getLineNumber());
        assertEquals(1, l5.getData().length);
        assertEquals("g", l5.getData()[0]);
    }
    
    /*=============*
     * Error cases *
     *=============*/
    
    private void expectError(String description, int cols, String commentStart, String msgMatch) {
        try {
            TabReader.getAll(getFile("3-token.txt"), cols, commentStart);
            fail("Failed to catch an exception");
        } catch (Exception e) {
            String msg = e.getClass().getSimpleName() + ": " + e.getMessage();
            assertTrue(String.format("Caught wrong exception for: %s\n%s", description, msg),
                       msg.matches(msgMatch));
        }
    }
    
    public void testBadFormat() {
        expectError("too few tokens", 5, "#", "ReadExc.*xpected 5.*");
        expectError("too many tokens", 2, "#", "ReadExc.*xpected 2.*");
    }
}

