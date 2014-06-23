/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2013 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.util;

import java.util.regex.Pattern;

import junit.framework.TestCase;

public class DataFileConfigurationTest extends TestCase {

    private static final String DIR = "src/test/resources/com/basistech/rosette/util/";

    private static DataFileConfiguration getCfg(String name) {
        return DataFileConfiguration.getConfiguration("./", DIR + name);
    }

    private static String getErr(String name) {
        try {
            getCfg(name);
        } catch (DataFileConfiguration.ConfigException e) {
            return e.getMessage();
        }
        fail("Test failed to throw a ConfigException: " + name);
        return "This can't happen...";
    }

    private void checkFailure(String path, String prefix) {
        String msg = getErr(path);
        if (msg.length() > prefix.length()) {
            msg = msg.substring(0, prefix.length());
        }
        assertEquals("Wrong Error:", prefix, msg);
    }

    public void testFailures() {
        checkFailure("This file should not exist!", "Problem opening:");
        checkFailure("problem-reading.datafiles", "Problem reading:");
        checkFailure("not-a-directory.datafiles", "Not a directory:");
        checkFailure("bad-wildcard.datafiles", "Wildcard name must contain exactly one '*':");
    }

    public void testConfig() {
        DataFileConfiguration cfg = getCfg("test.datafiles");
        assertNull("You found a VAX running Java?  Architecture check:", cfg.lookup("test"));
        assertEquals("Wrong file:", "overridden_eng", cfg.lookup("eng").getName());
        assertEquals("Wrong file:", "test_spa.txt", cfg.lookup("spa").getName());
        int check = 0;
        // This should return only "x_1", "x_2", and "x_3".  (Not "x_17", "eng", etc.)
        for (DataFileConfiguration.Result r : cfg.lookup(Pattern.compile("x_(.)"))) {
            if ("x_1".equals(r.getMatch().group(0))) {
                assertEquals("Bad match:", "1", r.getMatch().group(1));
                assertEquals("Wrong file:", "test_x_1.txt", r.getFile().getName());
                check += 101;
            } else if ("x_2".equals(r.getMatch().group(0))) {
                assertEquals("Bad match:", "2", r.getMatch().group(1));
                assertEquals("Wrong file:", "x_2", r.getFile().getName());
                check += 102;
            } else if ("x_3".equals(r.getMatch().group(0))) {
                assertEquals("Bad match:", "3", r.getMatch().group(1));
                assertEquals("Wrong file:", "x_3", r.getFile().getName());
                check += 104;
            } else {
                fail("Unexpected match: " + r.getMatch().group(0));
            }
        }
        // Make sure we passed through each arm exactly once:
        assertEquals("There were missing or extra keys:", 307, check);
    }
}
