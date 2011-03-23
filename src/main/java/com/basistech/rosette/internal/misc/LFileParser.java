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
    
    private StringBuilder buffer;
    
    public void characters(char[] charbuffer, int start, int length) {
        buffer.append(charbuffer, start, length);
    }

    public void endDocument() throws SAXException {
        result = new LFile(generator, customer, expiration, elist);
    }

    public void endElement(String uri, String name, String qname) {
        
       
        if (tag != null) {
            switch (tag) {
            case CUSTOMER:
                this.customer = buffer.toString();
                break;
            case EXPIRATION:
                this.expiration = buffer.toString();
                break;
            case GENERATOR:
                this.generator = buffer.toString();
                break;
            case LICENSE:
                if ("product".equals(name)) {
                    this.product = buffer.toString();
                } else if ("feature".equals(name)) {
                    this.feature = "".equals(buffer.toString()) ? null : buffer.toString();
                } else if ("license_key".equals(name)) {
                    this.licenseKey = buffer.toString();
                } else if ("language".equals(name)) {
                    this.language = "".equals(buffer.toString()) ? null : buffer.toString();
                } else if ("function".equals(name)) {
                    // DO NOTHING FOR NOW
                }
                break;
            default:
            }
        }
        
        
        if ("license".equals(name)) {
            elist.add(new Entry(product, feature, language, licenseKey));
            product = null;
            feature = null;
            language = null;
            licenseKey = null;
            tag = null;
        } else if ("generator".equals(name)
                || "customer".equals(name)
                || "expiration".equals(name)) {
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

    public void startElement(String uri, String name, String qname,
            Attributes arg3) {
        
        // we are in a license tag
        
        if ("generator".equals(name)) {
            tag = Tag.GENERATOR;
        } else if ("customer".equals(name)) {
            tag = Tag.CUSTOMER;
        } else if ("expiration".equals(name)) {
            tag = Tag.EXPIRATION;
        } else if ("license".equals(name)) {
            tag = Tag.LICENSE;
        }
        buffer = new StringBuilder();

    }

    public void startPrefixMapping(String arg0, String arg1) {
    }

    public LFile getResult() {
        return result;
    }

}
