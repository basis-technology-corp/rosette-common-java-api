/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.t5build;

/**
 *
 */
class PayloadLexerException extends Exception {
    private int column = -1;

    PayloadLexerException() {
    }

    PayloadLexerException(String message) {
        super(message);
    }

    PayloadLexerException(String message, int column) {
        super(message);
        this.column = column;

    }

    PayloadLexerException(String message, Throwable cause) {
        super(message, cause);
    }

    PayloadLexerException(Throwable cause) {
        super(cause);
    }

    int getColumn() {
        return column;
    }
}
