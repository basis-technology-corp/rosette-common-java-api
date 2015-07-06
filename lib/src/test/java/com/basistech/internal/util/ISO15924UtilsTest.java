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
package com.basistech.internal.util;

import com.basistech.util.ISO15924;
import org.junit.Assert;
import org.junit.Test;

public class ISO15924UtilsTest extends Assert {

    @Test
    public void testScriptForString() {
        ISO15924 script = ISO15924Utils.scriptForString("This is just latin");
        assertEquals(ISO15924.Latn, script);
        script = ISO15924Utils.scriptForString("\u59d0\u59d1\u59d2");
        assertEquals(ISO15924.Hani, script);
    }

    @Test
    public void testForString() {
        ISO15924Utils utils = new ISO15924Utils(); 
        ISO15924 script = utils.forString("This is just latin");
        assertEquals(ISO15924.Latn, script);
        script = utils.forString("\u59d0\u59d1\u59d2");
        assertEquals(ISO15924.Hani, script);
    }

    @Test
    public void halfwidthKatakana() throws Exception {
        assertSame(ISO15924.Kana, ISO15924Utils.scriptForChar('ﾆ'));
    }

    @Test
    public void fullwidthPunct() throws Exception {
        assertSame(ISO15924.Zyyy, ISO15924Utils.scriptForChar('（'));
    }

    @Test
    public void fullwidthLatinLetter() throws Exception {
        // U+FF21 FULLWIDTH LATIN CAPITAL LETTER A
        assertSame(ISO15924.Latn, ISO15924Utils.scriptForChar('Ａ'));
    }

    @Test
    public void fullwidthDigit() throws Exception {
        // U+FF10 FULLWIDTH DIGIT ZERO
        assertSame(ISO15924.Zyyy, ISO15924Utils.scriptForChar('０'));
    }

    @Test
    public void bom() throws Exception {
        assertSame(ISO15924.Zyyy, ISO15924Utils.scriptForChar('\ufeff'));
    }

    @Test
    public void testArab() {
        String barackObamaInArabic =
                "\u0628\u0627\u0631\u0627\u0643 \u0627\u0648\u0628\u0627\u0645\u0627";
        assertEquals(ISO15924.Arab, ISO15924Utils.scriptForString(barackObamaInArabic));
    }

    @Test
    public void testHani() {
        String barackObamaInChinese =
                "\u5df4\u62c9\u514b \u5965\u5df4\u9a6c";
        assertEquals(ISO15924.Hani, ISO15924Utils.scriptForString(barackObamaInChinese));
    }

    @Test
    public void testJpananeseScripts() {
        String mixedHaniAndHira = "\u4e09\u6761\u306e\u65b9"; //3 hani characters 1 hira
        assertEquals(ISO15924.Jpan, ISO15924Utils.scriptForString(mixedHaniAndHira));

        String mixedHaniAndKana = "\u5442\u6bd4\u9808\u30ef"; //3 hani characters 1 kana
        assertEquals(ISO15924.Jpan, ISO15924Utils.scriptForString(mixedHaniAndKana));

        String mixedHiraAndKana = "\u30e4\u20f3\u30b0 \u306e"; //3 kana 1 hira
        assertEquals(ISO15924.Hrkt, ISO15924Utils.scriptForString(mixedHiraAndKana));

        //RLPNC-3516
        String jpan = "\u8449\u540D\u30C1\u30F3"; //equal number of hani and hrkt
        assertEquals(ISO15924.Jpan, ISO15924Utils.scriptForString(jpan));
    }


}
