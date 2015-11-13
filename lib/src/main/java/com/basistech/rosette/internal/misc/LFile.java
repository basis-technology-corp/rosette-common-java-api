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

import java.util.List;

/**
 * License file.
 */
final class LFile {
    private String generator;
    private String customer;
    private String expiration;
    private List<Entry> entries;
    private String token;
    private boolean amz;

    LFile(String generator, String customer, String expiration, List<Entry> entries) {
        this.generator = generator;
        this.customer = customer;
        this.expiration = expiration;
        this.entries = entries;
    }

    LFile(String generator, String customer, String expiration, List<Entry> entries, String token) {
        this(generator, customer, expiration, entries);
        this.token = token;

    }

    private LFile() {
        amz = true;
    }

    static LFile getAmz() {
        return new LFile();
    }

    String getGenerator() {
        return generator;
    }

    String getCustomer() {
        return customer;
    }

    String getExpiration() {
        return expiration;
    }

    List<Entry> getEntries() {
        return entries;
    }

    String getToken() {
        return token;
    }

    boolean isAmz() {
        return amz;
    }

}
