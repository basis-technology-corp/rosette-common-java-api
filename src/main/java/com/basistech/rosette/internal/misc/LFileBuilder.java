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
package com.basistech.rosette.internal.misc;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parse a License file. Class name inspired by DocumentBuilder.
 */
public final class LFileBuilder {
    private LFileBuilder() {
    }

    public static LFile parse(String content) {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(content));
            return domToLFile(db.parse(inputSource));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static LFile parse(InputStream content) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {

                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                public void warning(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            return domToLFile(db.parse(content));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static LFile domToLFile(Document doc) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            String generator = xpath.evaluate("/BTLicense/generator", doc);
            String customer = xpath.evaluate("/BTLicense/customer", doc);
            String expiration = xpath.evaluate("/BTLicense/expiration", doc);
            NodeList licenseNodes = (NodeList)xpath.evaluate("/BTLicense/license", doc,
                                                             XPathConstants.NODESET);
            List<Entry> elist = new ArrayList<Entry>();
            for (int x = 0; x < licenseNodes.getLength(); x++) {
                Element licElement = (Element)licenseNodes.item(x);
                String product = xpath.evaluate("product", licElement);
                String feature = xpath.evaluate("feature", licElement);
                if ("".equals(feature)) {
                    feature = null;
                }
                String licenseKey = xpath.evaluate("license_key", licElement);
                String language = xpath.evaluate("language", licElement);
                if ("".equals(language)) {
                    language = null;
                }
                Entry e = new Entry(product, feature, language, licenseKey);
                elist.add(e);
            }
            return new LFile(generator, customer, expiration, elist);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
