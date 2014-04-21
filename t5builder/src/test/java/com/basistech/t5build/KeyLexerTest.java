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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class KeyLexerTest extends Assert {

    @Test
    public void testLexer() throws Exception {
        String input = "abc\\t\\r\\n\\z\\q\\u2e00\\U0001F601";
        //0xD83D 0xDE01 is the UTF-16 for U+1F601
        KeyLexer lexer = new KeyLexer(input);
        String key = lexer.yylex();
        assertEquals("abc\t\r\n\u0000\\q\u2e00\ud83d\ude01", key);
    }
}
