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
import com.ibm.icu.lang.UScript;

/**
 * Useful functions related to ISO15924 script codes.
 */
public class ISO15924Utils {
    private static final int[] ICU_TO_NUMERIC;
    private static final ISO15924[] ICU_TO_ENUM;
    private int[] histogram;

    //CHECKSTYLE:OFF
    static {
        ICU_TO_NUMERIC = new int[UScript.CODE_LIMIT];
        ICU_TO_ENUM = new ISO15924[UScript.CODE_LIMIT];

        setPair(UScript.COMMON, ISO15924.Zyyy); // COMMON maps to UNKNOWN. It's the convention.
        setPair(UScript.INHERITED, ISO15924.Zinh);
        setPair(UScript.ARABIC, ISO15924.Arab);
        setPair(UScript.ARMENIAN, ISO15924.Armn);
        setPair(UScript.BENGALI, ISO15924.Beng);
        setPair(UScript.BOPOMOFO, ISO15924.Bopo);
        setPair(UScript.CHEROKEE, ISO15924.Cher);
        setPair(UScript.COPTIC, ISO15924.Copt);
        setPair(UScript.CYRILLIC, ISO15924.Cyrl);
        setPair(UScript.DESERET, ISO15924.Dsrt);
        setPair(UScript.ETHIOPIC, ISO15924.Ethi);
        setPair(UScript.GEORGIAN, ISO15924.Geor);
        setPair(UScript.GOTHIC, ISO15924.Goth);
        setPair(UScript.GREEK, ISO15924.Grek);
        setPair(UScript.GUJARATI, ISO15924.Gujr);
        setPair(UScript.GURMUKHI, ISO15924.Guru);
        setPair(UScript.HAN, ISO15924.Hani);
        setPair(UScript.HANGUL, ISO15924.Hang);
        setPair(UScript.HEBREW, ISO15924.Hebr);
        setPair(UScript.HIRAGANA, ISO15924.Hira);
        setPair(UScript.KANNADA, ISO15924.Knda);
        setPair(UScript.KATAKANA, ISO15924.Kana);
        setPair(UScript.KHMER, ISO15924.Khmr);
        setPair(UScript.LAO, ISO15924.Laoo);
        setPair(UScript.LATIN, ISO15924.Latn);
        setPair(UScript.MALAYALAM, ISO15924.Mlym);
        setPair(UScript.MONGOLIAN, ISO15924.Mong);
        setPair(UScript.MYANMAR, ISO15924.Mymr);
        setPair(UScript.OGHAM, ISO15924.Ogam);
        setPair(UScript.OLD_ITALIC, ISO15924.Ital);
        setPair(UScript.ORIYA, ISO15924.Orya);
        setPair(UScript.RUNIC, ISO15924.Runr);
        setPair(UScript.SINHALA, ISO15924.Sinh);
        setPair(UScript.SYRIAC, ISO15924.Syrc);
        setPair(UScript.TAMIL, ISO15924.Taml);
        setPair(UScript.TELUGU, ISO15924.Telu);
        setPair(UScript.THAANA, ISO15924.Thaa);
        setPair(UScript.THAI, ISO15924.Thai);
        setPair(UScript.TIBETAN, ISO15924.Tibt);
        setPair(UScript.CANADIAN_ABORIGINAL, ISO15924.Cans);
        setPair(UScript.UCAS, ISO15924.Cans);
        setPair(UScript.YI, ISO15924.Yiii);
        setPair(UScript.TAGALOG, ISO15924.Tglg);
        setPair(UScript.HANUNOO, ISO15924.Hano);
        setPair(UScript.BUHID, ISO15924.Buhd);
        setPair(UScript.TAGBANWA, ISO15924.Tagb);
        setPair(UScript.BRAILLE, ISO15924.Brai);
        setPair(UScript.CYPRIOT, ISO15924.Cprt);
        setPair(UScript.LIMBU, ISO15924.Limb);
        setPair(UScript.LINEAR_B, ISO15924.Linb);
        setPair(UScript.OSMANYA, ISO15924.Osma);
        setPair(UScript.SHAVIAN, ISO15924.Shaw);
        setPair(UScript.TAI_LE, ISO15924.Tale);
        setPair(UScript.UGARITIC, ISO15924.Ugar);
        setPair(UScript.KATAKANA_OR_HIRAGANA, ISO15924.Hrkt);
        setPair(UScript.BUGINESE, ISO15924.Bugi);
        setPair(UScript.GLAGOLITIC, ISO15924.Glag);
        setPair(UScript.KHAROSHTHI, ISO15924.Khar);
        setPair(UScript.SYLOTI_NAGRI, ISO15924.Sylo);
        setPair(UScript.NEW_TAI_LUE, ISO15924.Talu);
        setPair(UScript.TIFINAGH, ISO15924.Tfng);
        setPair(UScript.OLD_PERSIAN, ISO15924.Xpeo);
        setPair(UScript.BALINESE, ISO15924.Bali);
        setPair(UScript.BATAK, ISO15924.Batk);
        setPair(UScript.BLISSYMBOLS, ISO15924.Blis);
        setPair(UScript.BRAHMI, ISO15924.Brah);
        setPair(UScript.CHAM, ISO15924.Cham);
        setPair(UScript.CIRTH, ISO15924.Cirt);
        setPair(UScript.OLD_CHURCH_SLAVONIC_CYRILLIC, ISO15924.Cyrs);
        setPair(UScript.DEMOTIC_EGYPTIAN, ISO15924.Egyd);
        setPair(UScript.HIERATIC_EGYPTIAN, ISO15924.Egyh);
        setPair(UScript.EGYPTIAN_HIEROGLYPHS, ISO15924.Egyp);
        setPair(UScript.KHUTSURI, ISO15924.Geok);
        setPair(UScript.SIMPLIFIED_HAN, ISO15924.Hans);
        setPair(UScript.TRADITIONAL_HAN, ISO15924.Hant);
        setPair(UScript.PAHAWH_HMONG, ISO15924.Hmng);
        setPair(UScript.OLD_HUNGARIAN, ISO15924.Hung);
        setPair(UScript.HARAPPAN_INDUS, ISO15924.Inds);
        setPair(UScript.JAVANESE, ISO15924.Java);
        setPair(UScript.KAYAH_LI, ISO15924.Kali);
        setPair(UScript.LATIN_FRAKTUR, ISO15924.Latf);
        setPair(UScript.LATIN_GAELIC, ISO15924.Latg);
        setPair(UScript.LEPCHA, ISO15924.Lepc);
        setPair(UScript.LINEAR_A, ISO15924.Lina);
        setPair(UScript.MANDAIC, ISO15924.Mand);
        setPair(UScript.MANDAEAN, ISO15924.Mand);
        setPair(UScript.MAYAN_HIEROGLYPHS, ISO15924.Maya);
        setPair(UScript.MEROITIC_HIEROGLYPHS, ISO15924.Mero);
        setPair(UScript.MEROITIC, ISO15924.Mero);
        setPair(UScript.NKO, ISO15924.Nkoo);
        setPair(UScript.ORKHON, ISO15924.Orkh);
        setPair(UScript.OLD_PERMIC, ISO15924.Perm);
        setPair(UScript.PHAGS_PA, ISO15924.Phag);
        setPair(UScript.PHOENICIAN, ISO15924.Phnx);
        setPair(UScript.MIAO, ISO15924.Plrd);
        setPair(UScript.PHONETIC_POLLARD, ISO15924.Plrd);
        setPair(UScript.RONGORONGO, ISO15924.Roro);
        setPair(UScript.SARATI, ISO15924.Sara);
        setPair(UScript.ESTRANGELO_SYRIAC, ISO15924.Syre);
        setPair(UScript.WESTERN_SYRIAC, ISO15924.Syrj);
        setPair(UScript.EASTERN_SYRIAC, ISO15924.Syrn);
        setPair(UScript.TENGWAR, ISO15924.Teng);
        setPair(UScript.VAI, ISO15924.Vaii);
        setPair(UScript.VISIBLE_SPEECH, ISO15924.Visp);
        setPair(UScript.CUNEIFORM, ISO15924.Xsux);
        setPair(UScript.UNWRITTEN_LANGUAGES, ISO15924.Zxxx);
        setPair(UScript.UNKNOWN, ISO15924.Zzzz);
        setPair(UScript.CARIAN, ISO15924.Cari);
        setPair(UScript.JAPANESE, ISO15924.Jpan);
        setPair(UScript.LANNA, ISO15924.Lana);
        setPair(UScript.LYCIAN, ISO15924.Lyci);
        setPair(UScript.LYDIAN, ISO15924.Lydi);
        setPair(UScript.OL_CHIKI, ISO15924.Olck);
        setPair(UScript.REJANG, ISO15924.Rjng);
        setPair(UScript.SAURASHTRA, ISO15924.Saur);
        setPair(UScript.SIGN_WRITING, ISO15924.Sgnw);
        setPair(UScript.SUNDANESE, ISO15924.Sund);
        setPair(UScript.MOON, ISO15924.Moon);
        setPair(UScript.MEITEI_MAYEK, ISO15924.Mtei);
        setPair(UScript.IMPERIAL_ARAMAIC, ISO15924.Armi);
        setPair(UScript.AVESTAN, ISO15924.Avst);
        setPair(UScript.CHAKMA, ISO15924.Cakm);
        setPair(UScript.KOREAN, ISO15924.Kore);
        setPair(UScript.KAITHI, ISO15924.Kthi);
        setPair(UScript.MANICHAEAN, ISO15924.Mani);
        setPair(UScript.INSCRIPTIONAL_PAHLAVI, ISO15924.Phli);
        setPair(UScript.PSALTER_PAHLAVI, ISO15924.Phlp);
        setPair(UScript.BOOK_PAHLAVI, ISO15924.Phlv);
        setPair(UScript.INSCRIPTIONAL_PARTHIAN, ISO15924.Prti);
        setPair(UScript.SAMARITAN, ISO15924.Samr);
        setPair(UScript.TAI_VIET, ISO15924.Tavt);
        setPair(UScript.MATHEMATICAL_NOTATION, ISO15924.Zmth);
        setPair(UScript.SYMBOLS, ISO15924.Zsym);
        setPair(UScript.BAMUM, ISO15924.Bamu);
        setPair(UScript.LISU, ISO15924.Lisu);
        setPair(UScript.NAKHI_GEBA, ISO15924.Nkgb);
        setPair(UScript.OLD_SOUTH_ARABIAN, ISO15924.Sarb);
        setPair(UScript.BASSA_VAH, ISO15924.Bass);
        setPair(UScript.DUPLOYAN_SHORTAND, ISO15924.Dupl);
        setPair(UScript.ELBASAN, ISO15924.Elba);
        setPair(UScript.GRANTHA, ISO15924.Gran);
        setPair(UScript.KPELLE, ISO15924.Kpel);
        setPair(UScript.LOMA, ISO15924.Loma);
        setPair(UScript.MENDE, ISO15924.Mend);
        setPair(UScript.MEROITIC_CURSIVE, ISO15924.Merc);
        setPair(UScript.OLD_NORTH_ARABIAN, ISO15924.Narb);
        setPair(UScript.NABATAEAN, ISO15924.Nbat);
        setPair(UScript.PALMYRENE, ISO15924.Palm);
        setPair(UScript.SINDHI, ISO15924.Sind);
        setPair(UScript.WARANG_CITI, ISO15924.Wara);
        setPair(UScript.AFAKA, ISO15924.Afak);
        setPair(UScript.JURCHEN, ISO15924.Jurc);
        setPair(UScript.MRO, ISO15924.Mroo);
        setPair(UScript.NUSHU, ISO15924.Nshu);
        setPair(UScript.SHARADA, ISO15924.Shrd);
        setPair(UScript.SORA_SOMPENG, ISO15924.Sora);
        setPair(UScript.TAKRI, ISO15924.Takr);
        setPair(UScript.TANGUT, ISO15924.Tang);
        setPair(UScript.WOLEAI, ISO15924.Wole);
        setPair(UScript.ANATOLIAN_HIEROGLYPHS, ISO15924.Hluw);
        setPair(UScript.KHOJKI, ISO15924.Khoj);
        setPair(UScript.TIRHUTA, ISO15924.Tirh);
        setPair(UScript.CAUCASIAN_ALBANIAN, ISO15924.Aghb);
        setPair(UScript.MAHAJANI, ISO15924.Mahj);
    }
    //CHECKSTYLE:ON

    /**
     * Callers can create objects of this type to avoid heap churn for memory inside of them.
     */
    public ISO15924Utils() {
        histogram = new int[1000];
    }

    private static void setPair(int uscript, ISO15924 item) {
        ICU_TO_NUMERIC[uscript] = item.numeric();
        ICU_TO_ENUM[uscript] = item;
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
     *
     * @param c the character to examine
     * @return the script for this character.
     */
    public static ISO15924 scriptForChar(char c) {
        return scriptForCodePoint(c); // same thing.
    }

    /**
     * Return an ISO15924 script code for a single code point.
     *
     * @param codePoint
     * @return the script for this code point.
     */
    public static ISO15924 scriptForCodePoint(int codePoint) {
        int icuscript = UScript.getScript(codePoint);
        ISO15924 val = ICU_TO_ENUM[icuscript];
        if (val == null) {
            return ISO15924.Zyyy;
        } else {
            return val;
        }
    }

    private static ISO15924 extractFromStringInternal(String input, int[] histogram) {
        for (int x = 0; x < input.length(); x++) {
            char c = input.charAt(x);
            if (c == '\u30fb' || c == '\u30a0') {
                continue;
            }
            int icucode = UScript.getScript(c);
            int numeric = ICU_TO_NUMERIC[icucode];
            if (numeric != 0) {
                histogram[numeric]++;
            }
        }
        // OK, what's the plan? Look for the biggest number and return based on that?
        // and what's the convention? One AR in 20 Latn? I think that Latn likes to lose,
        // and otherwise majority wins.
        int bestNonLatinCount = 0;
        int bestNonLatinCode = ISO15924.Zyyy.numeric(); //Zyyy
        int latinCount = 0;
        for (int x = 0; x < histogram.length; x++) {
            if (x == ISO15924.Latn.numeric()) {
                latinCount = histogram[x];
            } else if (histogram[x] > bestNonLatinCount && x != ISO15924.Zinh.numeric() && x != ISO15924.Zyyy.numeric()) { // no counting of Zinh or Zyyy
                bestNonLatinCount = histogram[x];
                bestNonLatinCode = x;
            }
        }

        // because spaces and whatnot are Zinh (used to be Latn), they don't end up in bestNonLatinCount
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
