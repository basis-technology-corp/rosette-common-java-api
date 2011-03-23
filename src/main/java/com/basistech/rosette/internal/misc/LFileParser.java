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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class LFileParser implements ContentHandler {
    
    private enum Tag { GENERATOR, CUSTOMER, EXPIRATION, LICENSE };
    private enum License { PRODUCT, FEATURE, KEY, LANGUAGE, FUNCTION };
    
    private LFile result;
    
    private Tag tag; // Top level layer we're in
    
    private String generator;
    private String customer;
    private String expiration;
    
    private List<Entry> elist = new ArrayList<Entry>();
    
    // local entry store
    private String product;
    private String feature;
    private String licenseKey;
    private String language;
    
    private String buffer;
    
    public void characters(char[] charbuffer, int start, int length) {
        String input = new String(charbuffer, start, length);
        buffer = buffer.concat(input);
    }

    public void endDocument() throws SAXException {
        result = new LFile(generator, customer, expiration, elist);
    }

    public void endElement(String arg0, String arg1, String arg2) {
        
       
        if (tag != null) {
            switch (tag) {
            case CUSTOMER:
                this.customer = buffer;
                break;
            case EXPIRATION:
                this.expiration = buffer;
                break;
            case GENERATOR:
                this.generator = buffer;
                break;
            case LICENSE:
                if ("product".equals(arg1)) {
                    this.product = buffer;
                } else if ("feature".equals(arg1)) {
                    this.feature = "".equals(buffer) ? null : buffer;
                } else if ("license_key".equals(arg1)) {
                    this.licenseKey = buffer;
                } else if ("language".equals(arg1)) {
                    this.language = "".equals(buffer) ? null : buffer;
                } else if ("function".equals(arg1)) {
                    // DO NOTHING FOR NOW
                }
                break;
            default:
            }
        }
        
        
        if ("license".equals(arg1)) {
            elist.add(new Entry(product, feature, language, licenseKey));
            product = null;
            feature = null;
            language = null;
            licenseKey = null;
            tag = null;
        } else if ("generator".equals(arg1)
                || "customer".equals(arg1)
                || "expiration".equals(arg1)) {
            tag = null;
        }
    }

    public void endPrefixMapping(String arg0) {
    }

    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) {
    }

    public void processingInstruction(String arg0, String arg1) {
    }

    public void setDocumentLocator(Locator arg0) {
    }

    public void skippedEntity(String arg0) {
    }

    public void startDocument() {
    }

    public void startElement(String arg0, String arg1, String arg2,
            Attributes arg3) {
        
        // we are in a license tag
        
        if ("generator".equals(arg1)) {
            tag = Tag.GENERATOR;
        } else if ("customer".equals(arg1)) {
            tag = Tag.CUSTOMER;
        } else if ("expiration".equals(arg1)) {
            tag = Tag.EXPIRATION;
        } else if ("license".equals(arg1)) {
            tag = Tag.LICENSE;
        }
        buffer = "";

    }

    public void startPrefixMapping(String arg0, String arg1) {
    }

    public LFile getResult() {
        return result;
    }

}
