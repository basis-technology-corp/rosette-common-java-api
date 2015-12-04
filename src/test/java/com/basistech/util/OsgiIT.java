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
