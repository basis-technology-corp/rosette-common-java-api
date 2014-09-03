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
package com.basistech.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Useful functions related to ISO15924 script codes.
 */
public class ISO15924Utils {

    static Map<Character.UnicodeBlock, ISO15924> blockToScript;
    static Map<Character.UnicodeBlock, Integer> blockToScriptNumeric;
    private int histogram[];

    static {
        blockToScript = new HashMap<Character.UnicodeBlock, ISO15924>();
        blockToScript.put(Character.UnicodeBlock.ARABIC, ISO15924.Arab);
        blockToScript.put(Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_A, ISO15924.Arab);
        blockToScript.put(Character.UnicodeBlock.ARABIC_PRESENTATION_FORMS_B, ISO15924.Arab);
        blockToScript.put(Character.UnicodeBlock.ARMENIAN, ISO15924.Armn);
        blockToScript.put(Character.UnicodeBlock.BASIC_LATIN, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.BENGALI, ISO15924.Beng);
        blockToScript.put(Character.UnicodeBlock.BOPOMOFO, ISO15924.Bopo); // map to Chinese, eventually.
        blockToScript.put(Character.UnicodeBlock.BOPOMOFO_EXTENDED, ISO15924.Bopo); // map to Chinese,
        // eventually.
        blockToScript.put(Character.UnicodeBlock.CHEROKEE, ISO15924.Cher);
        blockToScript.put(Character.UnicodeBlock.CJK_COMPATIBILITY, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION, ISO15924.Hani);
        // what about T versus S? Not our problem at this level.
        blockToScript.put(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.CYRILLIC, ISO15924.Cyrl);
        blockToScript.put(Character.UnicodeBlock.DEVANAGARI, ISO15924.Deva);
        blockToScript.put(Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.ETHIOPIC, ISO15924.Ethi);
        // this is a convention. It might not be what we want.
        blockToScript.put(Character.UnicodeBlock.GENERAL_PUNCTUATION, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.GREEK, ISO15924.Grek);
        blockToScript.put(Character.UnicodeBlock.GREEK_EXTENDED, ISO15924.Grek);
        blockToScript.put(Character.UnicodeBlock.GUJARATI, ISO15924.Gujr);
        blockToScript.put(Character.UnicodeBlock.GURMUKHI, ISO15924.Guru);
        blockToScript.put(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO, ISO15924.Hang);
        blockToScript.put(Character.UnicodeBlock.HANGUL_JAMO, ISO15924.Hang);
        blockToScript.put(Character.UnicodeBlock.HANGUL_SYLLABLES, ISO15924.Hang);
        blockToScript.put(Character.UnicodeBlock.HEBREW, ISO15924.Hebr);
        blockToScript.put(Character.UnicodeBlock.HIRAGANA, ISO15924.Hira);
        blockToScript.put(Character.UnicodeBlock.IDEOGRAPHIC_DESCRIPTION_CHARACTERS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.IPA_EXTENSIONS, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.KANBUN, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.KANGXI_RADICALS, ISO15924.Hani);
        blockToScript.put(Character.UnicodeBlock.KANNADA, ISO15924.Knda);
        blockToScript.put(Character.UnicodeBlock.KATAKANA, ISO15924.Kana);
        blockToScript.put(Character.UnicodeBlock.KHMER, ISO15924.Khmr);
        blockToScript.put(Character.UnicodeBlock.LAO, ISO15924.Laoo);
        blockToScript.put(Character.UnicodeBlock.LETTERLIKE_SYMBOLS, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.LATIN_1_SUPPLEMENT, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.LATIN_EXTENDED_A, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.LATIN_EXTENDED_ADDITIONAL, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.LATIN_EXTENDED_B, ISO15924.Latn);
        blockToScript.put(Character.UnicodeBlock.MALAYALAM, ISO15924.Mlym);
        blockToScript.put(Character.UnicodeBlock.MONGOLIAN, ISO15924.Mong);
        blockToScript.put(Character.UnicodeBlock.MYANMAR, ISO15924.Mymr);
        blockToScript.put(Character.UnicodeBlock.OGHAM, ISO15924.Ogam);
        blockToScript.put(Character.UnicodeBlock.ORIYA, ISO15924.Orya);
        blockToScript.put(Character.UnicodeBlock.RUNIC, ISO15924.Runr);
        blockToScript.put(Character.UnicodeBlock.SINHALA, ISO15924.Sinh);
        blockToScript.put(Character.UnicodeBlock.SYRIAC, ISO15924.Syrc);
        blockToScript.put(Character.UnicodeBlock.TAMIL, ISO15924.Taml);
        blockToScript.put(Character.UnicodeBlock.TELUGU, ISO15924.Telu);
        blockToScript.put(Character.UnicodeBlock.THAANA, ISO15924.Thaa);
        blockToScript.put(Character.UnicodeBlock.THAI, ISO15924.Thai);
        blockToScript.put(Character.UnicodeBlock.TIBETAN, ISO15924.Tibt);
        blockToScript.put(Character.UnicodeBlock.UNIFIED_CANADIAN_ABORIGINAL_SYLLABICS, ISO15924.Cans);
        blockToScript.put(Character.UnicodeBlock.YI_RADICALS, ISO15924.Yiii);
        blockToScript.put(Character.UnicodeBlock.YI_SYLLABLES, ISO15924.Yiii);
        
        //Customer complained that an inordinate amount of time is spent calling numeric() in scriptForString
        //This helps scriptForString run about twice as fast
        blockToScriptNumeric = new HashMap<Character.UnicodeBlock, Integer>();
        for (Entry<Character.UnicodeBlock, ISO15924> entry : blockToScript.entrySet()) {
            blockToScriptNumeric.put(entry.getKey(), entry.getValue().numeric());
        }
    }

    /**
     * Callers can create objects of this type to avoid heap churn for memory inside of them.
     */
    public ISO15924Utils() {
        histogram = new int[1000];
    }

    /**
     * Examines a string, and returns an ISO15924 code corresponding to the contents. In the case of a
     * mixture, returns 'unknown'.
     * 
     * @param input The input string.
     * @return The resulting code.
     */
    public static ISO15924 scriptForString(String input) {
        // we want a little histogram
        int[] histogram = new int[1000];
        return extractFromStringInternal(input, histogram);
    }
    
    /**
     * Return an ISO15924 script code for a single character.
     * @param c
     * @return the script for this character.
     */
    public static ISO15924 scriptForChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        if (block == null) {
            return ISO15924.Zyyy;
        }
        ISO15924 script = blockToScript.get(block);
        if (script == null) {
            return ISO15924.Zyyy;
        }
        return script;
    }
    
    /**
     * Return an ISO15924 script code for a single code point.
     * @param codePoint
     * @return the script for this code point.
     */
    public static ISO15924 scriptForCodePoint(int codePoint) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);
        if (block == null) {
            return ISO15924.Zyyy;
        }
        ISO15924 script = blockToScript.get(block);
        if (script == null) {
            return ISO15924.Zyyy;
        }
        return script;
    }

    private static ISO15924 extractFromStringInternal(String input, int[] histogram) {
        for (int x = 0; x < input.length(); x++) {
            char c = input.charAt(x);
            if (c == '\u30fb' || c == '\u30a0') {
                continue;
            }
            Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
            Integer scr = blockToScriptNumeric.get(block);
            if (null != scr) {
                histogram[scr]++;
            }
        }
        // OK, what's the plan? Look for the biggest number and return based on that?
        // and what's the convention? One AR in 20 Latn? I think that Latn likes to lose,
        // and otherwise majority wins.
        int bestNonLatinCount = 0;
        int bestNonLatinCode = 998; //Zyyy
        int latinCount = 0;
        for (int x = 0; x < histogram.length; x++) {
            if (x == ISO15924.Latn.numeric()) {
                latinCount = histogram[x];
            } else if (histogram[x] > bestNonLatinCount) {
                bestNonLatinCount = histogram[x];
                bestNonLatinCode = x;
            }
        }

        if (bestNonLatinCount > 0) {
            ISO15924 bestScript = ISO15924.lookupByNumeric(bestNonLatinCode);
            int hiraCount = histogram[ISO15924.Hira.numeric()];
            int kanaCount = histogram[ISO15924.Kana.numeric()];
            int haniCount = histogram[ISO15924.Hani.numeric()];
            if ((hiraCount > 0 || kanaCount > 0) && haniCount > 0) {
                return ISO15924.Jpan;
            } else if (hiraCount > 0 && kanaCount > 0) {
                return ISO15924.Hrkt;
            } else {
                return bestScript;
            }
        } else if (latinCount > 0) {
            return ISO15924.Latn;
        } else {
            return ISO15924.Zzzz;
        }
    }

    /**
     * Examines a string, and returns an ISO15924 code corresponding to the contents. In the case of a
     * mixture, returns 'unknown'.
     * 
     * @param input The input string.
     * @return The resulting code.
     */
    public ISO15924 forString(String input) {
        for (int x = 0; x < histogram.length; x++) {
            histogram[x] = 0;
        }
        return extractFromStringInternal(input, histogram);
    }
}
