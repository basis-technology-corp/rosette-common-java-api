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

package com.basistech.rosette.internal.take5build;

/**
 * A runtime exception for things that can go wrong while building a Take5
 * binary.
 */
public class Take5BuilderException extends RuntimeException {

    public Take5BuilderException() {
        super();
    }

    public Take5BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public Take5BuilderException(String message) {
        super(message);
    }

    public Take5BuilderException(Throwable cause) {
        super(cause);
    }
}
