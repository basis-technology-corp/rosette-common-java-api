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

/**
 * Data capture from license file.
 */
public class Entry {
    private String product;
    private String feature;
    private String featureString;
    private String language;

    public Entry(String product, String feature, String language, String featureString) {
        this.product = product;
        this.feature = feature;
        this.language = language;
        this.featureString = featureString;
    }

    public String getProduct() {
        return product;
    }

    public String getFeature() {
        return feature;
    }

    public String getFeatureString() {
        return featureString;
    }

    public String getLanguage() {
        return language;
    }

}
