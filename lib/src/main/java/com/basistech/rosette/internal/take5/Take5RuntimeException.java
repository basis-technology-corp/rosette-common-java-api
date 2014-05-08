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

package com.basistech.rosette.internal.take5;

import com.basistech.rosette.RosetteRuntimeException;

/**
 * <code>Take5RuntimeException</code> is thrown for unrecoverable take5 errors
 * which occur after the take5 is loaded.  For example, UNSUPPORTED_STATE_TYPE
 * might be thrown during a take5 search, but only if the take5 file was corrupt.
 */
public class Take5RuntimeException extends RosetteRuntimeException {
    private int type;

    Take5RuntimeException(int type) {
        super();
        this.type = type;
    }
    
    Take5RuntimeException(int type, String additionalMessage) {
        super(additionalMessage);
        this.type = type;
    }

    @Override
    public String getMessage() {
        String message = Take5Exception.getTypeString(type);
        String additional = super.getMessage();
        if (additional != null) {
            return message + " " + super.getMessage();
        } else {
            return message;
        }
    }

    public int getType() {
        return type;
    }

}
