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

package com.basistech.test;

import com.basistech.rosette.internal.misc.LManager;
import com.basistech.util.LanguageCode;

/**
 * Try out the Amazon license checking code.
 */
public final class CheckAmazonLicense {
    private CheckAmazonLicense() {
        //
    }

    public static void main(String[] args) {
        LManager lm = new LManager("<BTLicense><amz/></BTLicense>");
        lm.checkLanguage(LanguageCode.ENGLISH_UPPERCASE, 1, true);
        lm.checkFeature("RLI", 0, true);
    }

}
