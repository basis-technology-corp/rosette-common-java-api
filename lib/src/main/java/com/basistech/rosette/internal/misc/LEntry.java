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

public class LEntry {
    private String language;
    private String feature;
    private long expiration;
    private int functions;
    private boolean expired;

    public String getLanguage() {
        return language;
    }

    void setLanguage(String language) {
        this.language = language;
    }

    public String getFeature() {
        return feature;
    }

    void setFeature(String feature) {
        this.feature = feature;
    }

    public long getExpiration() {
        return expiration;
    }

    void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public int getFunctions() {
        return functions;
    }

    void setFunctions(int functions) {
        this.functions = functions;
    }

    public boolean isExpired() {
        return expired;
    }

    void setExpired(boolean expired) {
        this.expired = expired;
    }
}
