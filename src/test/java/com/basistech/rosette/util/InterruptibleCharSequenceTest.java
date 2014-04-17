/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.util;

import com.basistech.rosette.RosetteInterruptedException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link com.basistech.rosette.util.InterruptibleCharSequence}.
 */
public class InterruptibleCharSequenceTest extends Assert {

    private static final String DATA = "This is the cereal shot from guns.";

    @Test
    public void testOffsetChecking() {
        InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
        assertEquals(DATA.length(), seq.length());
        IndexOutOfBoundsException e = null;
        try {
            seq.charAt(DATA.length());
        } catch (IndexOutOfBoundsException iobe) {
            e = iobe;
        }
        assertNotNull(e);

        seq = new InterruptibleCharSequence(DATA.toCharArray(), 5, 10);
        assertEquals(5, seq.length());

        e = null;
        try {
            seq.subSequence(4, 6);
        } catch (IndexOutOfBoundsException iobe) {
            e = iobe;
        }
        assertNotNull(e);
        assertTrue(e.getMessage().contains("Invalid end offset"));

        e = null;
        try {
            seq.subSequence(6, 2);
        } catch (IndexOutOfBoundsException iobe) {
            e = iobe;
        }
        assertNotNull(e);
        assertTrue(e.getMessage().contains("Invalid start offset"));
    }

    @Test
    public void testSubSequence() {
        // this also tests toString(), while we are at it.
        InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
        CharSequence sub = seq.subSequence(1, 4);
        assertEquals("his ", sub.toString());
    }

    private static class SelfInterruptingThread extends Thread {

        private final Runnable testCase;
        private Throwable throwable;

        private SelfInterruptingThread(Runnable testCase) {
            this.testCase = testCase;
        }

        @Override
        public void run() {
            // pend an interrupt
            Thread.currentThread().interrupt();

            try {
                testCase.run();
            } catch (Throwable rie) {
                throwable = rie;
            }
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }

    private void checker(Runnable testCase) throws InterruptedException {
        SelfInterruptingThread thread = new SelfInterruptingThread(testCase);
        thread.start();
        thread.join();
        assertTrue(thread.getThrowable() instanceof RosetteInterruptedException);
    }

    @Test
    public void testLengthCheck() throws Exception {
        checker(new Runnable() {
            @Override
            public void run() {
                InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
                seq.length(); // get an exception.
            }
        });
    }

    @Test
    public void testCharAtCheck() throws Exception {
        checker(new Runnable() {
            @Override
            public void run() {
                InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
                seq.charAt(3); // get an exception.
            }
        });
    }

    @Test
    public void testSubSequenceCheck() throws Exception {
        checker(new Runnable() {
            @Override
            public void run() {
                InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
                seq.subSequence(1, 4); // get an exception.
            }
        });
    }

    @Test
    public void testtoStringCheck() throws Exception {
        checker(new Runnable() {
            @Override
            public void run() {
                InterruptibleCharSequence seq = new InterruptibleCharSequence(DATA.toCharArray(), 0, DATA.length());
                seq.toString(); // get an exception.
            }
        });
    }
}
