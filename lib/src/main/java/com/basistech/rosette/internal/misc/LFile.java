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

import java.util.List;

/**
 * License file.
 */
public class LFile {
    private String generator;
    private String customer;
    private String expiration;
    private List<Entry> entries;
    private String token;

    public LFile(String generator, String customer, String expiration, List<Entry> entries) {
        this.generator = generator;
        this.customer = customer;
        this.expiration = expiration;
        this.entries = entries;
    }

    public LFile(String generator, String customer, String expiration, List<Entry> entries, String token) {
        this(generator, customer, expiration, entries);
        this.token = token;

    }


    public String getGenerator() {
        return generator;
    }

    public String getCustomer() {
        return customer;
    }

    public String getExpiration() {
        return expiration;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public String getToken() { return token; }

}
