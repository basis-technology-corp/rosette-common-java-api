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

package com.basistech.rosette.internal.take5build;

import java.io.IOException;

/**
 * Some utility functions for the generated lexer.
 */
abstract class PayloadLexerBase {

    protected PayloadLexerBase() {
        //
    }

    public abstract PayloadToken yylex() throws java.io.IOException, PayloadLexerException;

    PayloadToken lex() throws PayloadLexerException {
        try {
            return yylex();
        } catch (IOException e) {
            // since we are always reading strings, we don't want to bother.
            throw new RuntimeException(e);
        }
    }

    protected static char decodeBmpEscape(String text) {
        return (char)Integer.parseInt(text.substring(2), 16);
    }

    protected static int decodeCodepointEscape(String text) {
        return Integer.parseInt(text.substring(2), 16);
    }
}
