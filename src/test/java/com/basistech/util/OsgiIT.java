/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2015 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.util;

import com.basistech.rosette.RosetteException;
import com.basistech.rosette.util.EncodingCode;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.url;

/**
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class OsgiIT {

    private static String projectVersion;
    private static String buildDir;

    public static void beforeClass() throws Exception {
        URL configPropUrl = Resources.getResource("test-config.properties");
        Properties props = new Properties();
        try (InputStream propStream = configPropUrl.openStream()) {
            props.load(propStream);
        }
        projectVersion = props.getProperty("project.version");
        buildDir = props.getProperty("project.build.directory");
    }


    @Configuration
    public static Option[] configuration() throws Exception {
        beforeClass();
        return options(
                url(String.format("file:%s/common-api-%s.jar", buildDir, projectVersion)),
                junitBundles());
    }

    @Test
    public void osgiExportsWorked() {
        new RosetteException();
        new BasisInternalException();
        System.out.println(EncodingCode.Ascii);
        System.out.println(LanguageCode.ENGLISH);

    }
}
