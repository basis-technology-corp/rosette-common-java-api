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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.List;

/**
 * Test cases for the payload lexer.
 */
public class PayloadLexerTest extends Assert {

    @Test
    public void runCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        URL url = Resources.getResource(PayloadLexerTest.class, "payload-lex-test-cases.txt");
        CharSource casesSource = Resources.asCharSource(url, Charsets.UTF_8);
        ImmutableList<String> lines = casesSource.readLines();
        for (String line : lines) {
            String[] pieces = line.split("\t");
            List<List<String>> items = mapper.readValue(pieces[0], new TypeReference<List<List<String>>>() {
            });
            List<PayloadToken> correctTokens = convertCase(items);
            List<PayloadToken> tokens = lexTokens(pieces[1]);
            assertEquals("Error on " + line, correctTokens, tokens);
        }
    }

    @Test
    public void testBadStuffAtTop() {
        PayloadLexer lexer = new PayloadLexer("#1f {");

        PayloadToken token;
        PayloadLexerException ple = null;
        try {
            do {
                token = lexer.lex();
            } while (token != null);
        } catch (PayloadLexerException e) {
            ple = e;
        }
        assertNotNull(ple);
        assertEquals(4, ple.getColumn());
    }

    @Test
    public void testColumns() throws Exception {
        List<PayloadToken> tokens = lexTokens("#1f #1s");
        assertEquals(0, tokens.get(0).column);
        assertEquals(4, tokens.get(1).column);
    }

    private List<PayloadToken> lexTokens(String text) throws PayloadLexerException {
        PayloadLexer lexer = new PayloadLexer(text);
        List<PayloadToken> result = Lists.newArrayList();

        PayloadToken token;
        do {
            try {
                token = lexer.lex();
            } catch (PayloadLexerException e) {
                System.err.println("Lexer error on input " + text);
                throw e;
            }
            if (token != null) {
                result.add(token);
            }
        } while (token != null);
        return result;
    }

    private List<PayloadToken> convertCase(List<List<String>> itemObject) {
        List<PayloadToken> results = Lists.newArrayList();
        for (List<String> item : itemObject) {
            PayloadTokenType type = PayloadTokenType.valueOf(item.get(0));
            String text = item.get(1);
            results.add(new PayloadToken(type, text, -1)); // the column is not compared
        }
        return results;
    }


}
