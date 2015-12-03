/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/
package com.basistech.rosette.internal.misc;

import com.basistech.internal.util.Base64;
import com.basistech.rosette.RosetteCorruptLicenseException;
import com.basistech.rosette.RosetteExpiredLicenseException;
import com.basistech.rosette.RosetteNoLicenseException;
import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * The license manager.
 */
public class LManager {

    // Magic strings used to allow license circumvention.
    private static final String SK1 = "n   x"; // use substring(1, 3)
    private static final String SK2 = "Jx^A@B1"; //^A^@
    private static final String SK3 = "jmnx0exxp1IWab6b";
    private static boolean amzChecked;
    private static boolean amz;
    private LFile licenseFile;
    private List<LEntry> languageEntries;
    private List<LEntry> featureEntries;
    private String token;

    /**
     * Create a license manager that reads license
     * data from the specified XML String.
     * @param xmlLicense License string in XML.
     * @throws RosetteCorruptLicenseException license corrupt or invalid
     */
    public LManager(String xmlLicense) throws RosetteCorruptLicenseException {
        licenseFile = LFileBuilder.parse(xmlLicense);
        initialize();
    }

    /**
     * Create a license manager that reads license
     * data from the specified stream.
     * @param xmlStream Stream that contains the license in XML.
     * @throws RosetteCorruptLicenseException license corrupt or invalid.
     */
    public LManager(InputStream xmlStream) throws RosetteCorruptLicenseException {
        licenseFile = LFileBuilder.parse(xmlStream);
        initialize();
    }

    /*
     * L Language Code ISO-639 language code this license applies to. (Required, unless we have Q) Q Feature
     * The name of a licensed feature other than a language. This could be done by licencing against L=xx, but
     * that seems completely bizarre. E Expiration Date Unix epoch after which the license is no longer valid.
     * A value of (time_t)-1 indicates a perpetual license. (Required) F Functions Hexadecimal number
     * representing the set of functions licensed for the language, using the bits defined in the Type_Bits
     * enumeration in the BT_RLP_Language_Processor class declaration. (Required)
     */
    private void initialize() throws RosetteCorruptLicenseException {

        if (licenseFile.isAmz()) {
            if (amzChecked) {
                return;
            }
            validateViaInstanceDocument();
            amzChecked = true;
            amz = true;
            return;
        }

        if (licenseFile.getToken() != null) {
            token = licenseFile.getToken();
        }
        languageEntries = new ArrayList<LEntry>();
        featureEntries = new ArrayList<LEntry>();
        List<Entry> entries = licenseFile.getEntries();
        for (Entry e : entries) {
            FeatureString fs = new FeatureString(e.getFeatureString());
            Map<String, Feature> fsFeatures = fs.getFeatures();
            Feature langFeature = fsFeatures.get("L");
            Feature featFeature = fsFeatures.get("Q");
            Feature expFeature = fsFeatures.get("E");
            Feature fcnFeature = fsFeatures.get("F");
            LEntry lentry = new LEntry();
            long exp;
            boolean expired;
            if (expFeature != null) {
                long epoch = System.currentTimeMillis() / 1000;
                exp = Integer.parseInt(expFeature.getValue());
                if (exp == -1) {
                    expired = false;
                } else {
                    expired = epoch > exp;
                }
            } else {
                exp = -1;
                expired = false;
            }
            lentry.setExpiration(exp);
            lentry.setExpired(expired);
            int fcns = 0;
            if (fcnFeature != null) {
                fcns = Integer.parseInt(fcnFeature.getValue(), 16);
            }
            lentry.setFunctions(fcns);
            if (langFeature != null) {
                lentry.setLanguage(langFeature.getValue());
                languageEntries.add(lentry);
            } else if (featFeature != null) {
                lentry.setFeature(featFeature.getValue());
                featureEntries.add(lentry);
            }
        }
    }

    private boolean checkInternal() {
        String sk = SK1.substring(1, 3)
                + SK2.substring(2, 4)
                + SK2.substring(2, 3)
                + SK2.substring(4, 5)
                + SK3.substring(10, 12)
                + SK3.substring(5, 6)
                + SK1.substring(1, 4);
        return sk.equals(token);
    }

    public boolean checkLanguage(LanguageCode languageCode,
                                 int functions, boolean throwErrors) throws RosetteNoLicenseException,
            RosetteExpiredLicenseException {
        if (amz) {
            return true;
        }
        // license key "  ^A^@IWe   " avoids license checks
        if (token != null) {
            if (checkInternal()) {
                return true;
            }
            if (throwErrors) {
                throw new RosetteNoLicenseException("No license present for language " + languageCode.languageName());
            }
            return false;
        }

        // skipping license check for language unknown since it is not required
        if (languageCode == LanguageCode.UNKNOWN) {
            return true;
        }
        // treat Uppercase English language code as English since we do not need separation at this level
        if (languageCode == LanguageCode.ENGLISH_UPPERCASE) {
            languageCode = LanguageCode.ENGLISH;
        }
        for (LEntry entry : languageEntries) {
            if (LanguageCode.lookupByISO639(entry.getLanguage()) == languageCode
                    && (functions == 0 || 0 != (functions & entry.getFunctions()))) {
                /*
                 * Assume only one function bit at a time.
                 */
                return checkExpiration(entry, throwErrors, "language " + languageCode.languageName());
            }
        }
        if (throwErrors) {
            throw new RosetteNoLicenseException("No license present for language " + languageCode.languageName());
        } else {
            return false;
        }
    }

    public boolean checkFeature(String feature, int functions, boolean throwErrors)
            throws RosetteNoLicenseException, RosetteExpiredLicenseException {
        if (amz) {
            return true;
        }
        // license key "  ^A^@IWe   " avoids license checks
        if (token != null) {
            if (checkInternal()) {
                return true;
            }
            if (throwErrors) {
                throw new RosetteNoLicenseException("No license present for feature " + feature);
            }
            return false;
        }

        for (LEntry entry : featureEntries) {
            if (feature.equals(entry.getFeature())
                    && (0 == functions || 0 != (functions & entry.getFunctions()))) {
                /*
                 * Assume only one function bit at a time.
                 */
                return checkExpiration(entry, throwErrors, "feature " + feature);
            }
        }
        if (throwErrors) {
            throw new RosetteNoLicenseException("No license present for feature " + feature);
        } else {
            return false;
        }
    }

    private boolean checkExpiration(LEntry entry, boolean throwErrors, String explanation)
            throws RosetteExpiredLicenseException {
        if (entry.isExpired()) {
            if (throwErrors) {
                throw new RosetteExpiredLicenseException("License for " + explanation + " expired.");
            } else {
                return false;
            }
        }
        return true;
    }

    static PublicKey getCertKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CertificateException {
        URL keyUrl = Resources.getResource(LManager.class, "iid.der");
        ByteSource source = Resources.asByteSource(keyUrl);
        Certificate cert;
        try (InputStream is = source.openStream()) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = cf.generateCertificate(is);
        }
        return cert.getPublicKey();
    }

    private boolean validateSignature(byte[] content, byte[] signature, PublicKey key) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        byte[] sigBytes = Base64.decode(signature, 0, signature.length);
        Signature dsaSig = Signature.getInstance("SHA256withRSA");
        dsaSig.initVerify(key);
        dsaSig.update(content);
        return dsaSig.verify(sigBytes);
    }

    private byte[] downloadMetadata(String mdPath) throws IOException {
        //http://169.254.169.254/latest/dynamic/instance-identity/document
        URL url = new URL(String.format("http://169.254.169.254/latest/dynamic/instance-identity/%s", mdPath));
        return getSomeMetadata(url);
    }

    private byte[] getSomeMetadata(URL url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            if (connection.usingProxy()) {
                throw new RosetteNoLicenseException("Invalid environment 0.1");
            }
            try (InputStream content = connection.getInputStream()) {
                return ByteStreams.toByteArray(content);
            }
        } finally {
            connection.disconnect();
        }
    }

    private String getProductCodes() throws IOException {
        byte[] pcBytes = getSomeMetadata(new URL("http://169.254.169.254/latest/meta-data/product-codes"));
        return new String(pcBytes, Charset.defaultCharset());
    }

    private void validateViaInstanceDocument() throws RosetteNoLicenseException {

        if (System.getProperty("http.proxyHost") != null
                || System.getProperty("http.proxyPort") != null) {
            throw new RosetteNoLicenseException("Invalid environment 0.1.1");
        }

        byte[] documentBytes;
        byte[] signature;
        try {
            documentBytes = downloadMetadata("document");
            signature = downloadMetadata("signature");
        } catch (IOException e) {
            throw new RosetteNoLicenseException("Invalid environment 1");
        }

        PublicKey publicKey;
        try {
            publicKey = getCertKey();
        } catch (IOException e) {
            throw new RosetteNoLicenseException("Invalid environment 2.1");
        } catch (NoSuchAlgorithmException e) {
            throw new RosetteNoLicenseException("Invalid environment 2.2");
        } catch (CertificateException e) {
            throw new RosetteNoLicenseException("Invalid environment 2.3");
        } catch (InvalidKeySpecException e) {
            throw new RosetteNoLicenseException("Invalid environment 2.4");
        }

        try {
            if (!validateSignature(documentBytes, signature, publicKey)) {
                throw new RosetteNoLicenseException("Invalid environment 3");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RosetteNoLicenseException("Invalid environment 3.1");
        } catch (SignatureException e) {
            throw new RosetteNoLicenseException("Invalid environment 3.2");
        } catch (InvalidKeyException e) {
            throw new RosetteNoLicenseException("Invalid environment 3.3");
        }

        Enumeration<NetworkInterface> interfaceEnum;
        try {
            interfaceEnum = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RosetteNoLicenseException("Invalid environment 4");

        }

        List<InterfaceAddress> addresses = Lists.newArrayList();
        while (interfaceEnum.hasMoreElements()) {
            NetworkInterface networkInterface = interfaceEnum.nextElement();
            addresses.addAll(networkInterface.getInterfaceAddresses());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode document;
        try {
            document = mapper.readTree(documentBytes);
        } catch (IOException e) {
            throw new RosetteNoLicenseException("Invalid environment 5");
        }
        String docPrivateIP = document.get("privateIp").asText();
        if (docPrivateIP == null) {
            throw new RosetteNoLicenseException("Invalid environment 6");
        }

        boolean foundIp = false;
        for (InterfaceAddress ia : addresses) {
            if (ia.getAddress().getHostAddress().equals(docPrivateIP)) {
                foundIp = true;
                break;
            }
        }

        if (!foundIp) {
            throw new RosetteNoLicenseException("Invalid environment 7");
        }

        try {
            // we aren't checking for _OUR_ codes yet, just for _some_ code.
            getProductCodes();
        } catch (IOException e) {
            // we will get a 404 when non-marketplace.
            throw new RosetteNoLicenseException("Invalid environment 8");
        }
    }
}
