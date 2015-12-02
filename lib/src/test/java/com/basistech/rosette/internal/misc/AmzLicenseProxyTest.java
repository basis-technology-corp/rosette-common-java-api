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

package com.basistech.rosette.internal.misc;

import com.basistech.rosette.RosetteNoLicenseException;
import com.basistech.util.LanguageCode;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.proxy.Proxy;
import org.mockserver.proxy.ProxyBuilder;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AmzLicenseProxyTest {

    //CHECKSTYLE:OFF
    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    private MockServerClient mockServerClient;
    //CHECKSTYLE:ON

    @Test
    public void licenseWithProxy() throws Exception {
        mockServerClient
                .reset()
                .when(request())
                .respond(response()
                                .withStatusCode(200)
                                .withHeader("x-test", "wolverine")
                                .withBody("Hello there")
                );
        Proxy proxy = new ProxyBuilder().withLocalPort(1080).withDirect("localhost", mockServerRule.getHttpPort()).build();
        try {
            System.setProperty("http.proxyHost", "localhost");
            System.setProperty("http.proxyPort", "1080");
            // this should fail, but the proxy should be tickled.
            LManager manager = new LManager("<BTLicense><amz/></BTLicense>");
            manager.checkLanguage(LanguageCode.ENGLISH_UPPERCASE, 1, true);
            manager.checkFeature("RLI", 0, true);
        } catch (RosetteNoLicenseException rnle) {
            assertTrue(rnle.getMessage().contains("0.1.1"));
        } finally {
            proxy.stop();
        }
        fail("No exception on proxy");
    }

}
