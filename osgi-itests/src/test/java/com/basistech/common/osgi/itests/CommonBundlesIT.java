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

package com.basistech.common.osgi.itests;

import com.basistech.internal.util.InterruptibleCharSequence;
import com.basistech.internal.util.bitvector.BitVector;
import com.basistech.rosette.RosetteException;
import com.basistech.rosette.internal.take5.Take5DictionaryBuilder;
import com.basistech.rosette.util.EncodingCode;
import com.basistech.util.LanguageCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.nio.ByteBuffer;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemPackages;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

/**
 * Test that the bundles work, at least to some minimal extent.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class CommonBundlesIT {
    @Configuration
    public Option[] config() {
        return options(
                // we use the bundle to get Take5 and the other stuff that we don't want to treat as platform.
                mavenBundle("com.basistech", "common-api").versionAsInProject(),
                mavenBundle("com.basistech", "common-lib").versionAsInProject(),
                mavenBundle("com.basistech", "common-java-t5builder").versionAsInProject(),
                mavenBundle("com.ibm.icu", "icu4j").versionAsInProject(),
                mavenBundle("com.google.guava", "guava").versionAsInProject(),
                junitBundles(),
                systemPackages(
                        // This are needed for guava.
                        "sun.misc",
                        "javax.annotation"),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN")
        );
    }

    @Test
    public void api() throws Exception {
        LanguageCode.lookupByISO639("eng"); // well, see if it explodes
        EncodingCode.lookupByMimeName("UTF-8");
        //noinspection ThrowableInstanceNeverThrown
        new RosetteException();
    }

    @Test
    public void lib() throws Exception {
        ByteBuffer dummy = ByteBuffer.allocate(0);
        // just see if we can construct the object.
        new Take5DictionaryBuilder(dummy);
        new InterruptibleCharSequence(new char[0], 0, 0);
        new BitVector(100);
    }
}
