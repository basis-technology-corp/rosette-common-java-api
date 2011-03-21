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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.basistech.rosette.RosetteCorruptLicenseException;

/**
 * Parse a License file. Class name inspired by DocumentBuilder.
 */
public final class LFileBuilder {
    private LFileBuilder() {
    }

    public static LFile parse(String content) {
        return domToLFile(new ByteArrayInputStream(content.getBytes()));
    }

    public static LFile parse(InputStream content) {
        return domToLFile(content);
    }

    private static LFile domToLFile(InputStream content) {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            LFileParser handler = new LFileParser();
            xr.setContentHandler(handler);
            xr.setErrorHandler(new ErrorHandler() {
                
                public void warning(SAXParseException exception) throws SAXException {
                }
                
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw new RosetteCorruptLicenseException(exception);
                }
                
                public void error(SAXParseException exception) throws SAXException {
                    throw new RosetteCorruptLicenseException(exception);
                }
            });
            xr.parse(new InputSource(content));
            
            return handler.getResult();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return new LFile(null, null, null, null); 
    }
}
