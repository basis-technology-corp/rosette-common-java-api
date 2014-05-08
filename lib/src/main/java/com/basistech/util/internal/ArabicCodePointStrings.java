//CHECKSTYLE:OFF
/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (C) 2003-2007 Basis Technology Corp. All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/
package com.basistech.util.internal;


/*
 * Constants for Arabic characters, to make it easier to express code that uses them.
 * This is a lot faster than a complete copy of the UCD in memory index with a hashtable
 * or whatever. The names here may seem cumbersome, but it preserves the use of the full UCD
 * names. A class using a lot of this could use the 'implements' trick to pull them
 * into the immediate namespace.
 */
public interface ArabicCodePointStrings {
    public static String ARABIC_COMMA = "\u060C";
    public static String ARABIC_SEMICOLON = "\u061B";
    public static String ARABIC_QUESTION_MARK = "\u061F";
    public static String ARABIC_LETTER_HAMZA = "\u0621";
    public static String ARABIC_LETTER_ALEF_WITH_MADDA_ABOVE = "\u0622";
    public static String ARABIC_LETTER_ALEF_WITH_HAMZA_ABOVE = "\u0623";
    public static String ARABIC_LETTER_WAW_WITH_HAMZA_ABOVE = "\u0624";
    public static String ARABIC_LETTER_ALEF_WITH_HAMZA_BELOW = "\u0625";
    public static String ARABIC_LETTER_YEH_WITH_HAMZA_ABOVE = "\u0626";
    public static String ARABIC_LETTER_ALEF = "\u0627";
    public static String ARABIC_LETTER_BEH = "\u0628";
    public static String ARABIC_LETTER_TEH_MARBUTA = "\u0629";
    public static String ARABIC_LETTER_TEH = "\u062A";
    public static String ARABIC_LETTER_THEH = "\u062B";
    public static String ARABIC_LETTER_JEEM = "\u062C";
    public static String ARABIC_LETTER_HAH = "\u062D";
    public static String ARABIC_LETTER_KHAH = "\u062E";
    public static String ARABIC_LETTER_DAL = "\u062F";
    public static String ARABIC_LETTER_THAL = "\u0630";
    public static String ARABIC_LETTER_REH = "\u0631";
    public static String ARABIC_LETTER_ZAIN = "\u0632";
    public static String ARABIC_LETTER_SEEN = "\u0633";
    public static String ARABIC_LETTER_SHEEN = "\u0634";
    public static String ARABIC_LETTER_SAD = "\u0635";
    public static String ARABIC_LETTER_DAD = "\u0636";
    public static String ARABIC_LETTER_TAH = "\u0637";
    public static String ARABIC_LETTER_ZAH = "\u0638";
    public static String ARABIC_LETTER_AIN = "\u0639";
    public static String ARABIC_LETTER_GHAIN = "\u063A";
    public static String ARABIC_TATWEEL = "\u0640";
    public static String ARABIC_LETTER_FEH = "\u0641";
    public static String ARABIC_LETTER_QAF = "\u0642";
    public static String ARABIC_LETTER_KAF = "\u0643";
    public static String ARABIC_LETTER_LAM = "\u0644";
    public static String ARABIC_LETTER_MEEM = "\u0645";
    public static String ARABIC_LETTER_NOON = "\u0646";
    public static String ARABIC_LETTER_HEH = "\u0647";
    public static String ARABIC_LETTER_WAW = "\u0648";
    public static String ARABIC_LETTER_ALEF_MAKSURA = "\u0649";
    public static String ARABIC_LETTER_YEH = "\u064A";
    public static String ARABIC_FATHATAN = "\u064B";
    public static String ARABIC_DAMMATAN = "\u064C";
    public static String ARABIC_KASRATAN = "\u064D";
    public static String ARABIC_FATHA = "\u064E";
    public static String ARABIC_DAMMA = "\u064F";
    public static String ARABIC_KASRA = "\u0650";
    public static String ARABIC_SHADDA = "\u0651";
    public static String ARABIC_SUKUN = "\u0652";
    public static String ARABIC_MADDAH_ABOVE = "\u0653";
    public static String ARABIC_HAMZA_ABOVE = "\u0654";
    public static String ARABIC_HAMZA_BELOW = "\u0655";
    public static String ARABIC_LETTER_SUBSCRIPT_ALEF = "\u0656";
    /* better name/additions 26-Oct-2005 12:42:35 pm */
    public static String ARABIC_SUBSCRIPT_ALEF = "\u0656";
    public static String ARABIC_INVERTED_DAMMA = "\u0657";
    public static String ARABIC_MARK_NOON_GHUNNA = "\u0658";
    public static String ARABIC_ZWARAKAY = "\u0659";
    public static String ARABIC_VOWEL_SIGN_SMALL_V_ABOVE = "\u065A";
    public static String ARABIC_VOWEL_SIGN_INVERTED_SMALL_V_ABOVE = "\u065B";
    public static String ARABIC_VOWEL_SIGN_DOT_BELOW = "\u065C";
    public static String ARABIC_VOWEL_SIGN_REVERSED_DAMMA = "\u065D";
    public static String ARABIC_FATHA_WITH_TWO_DOTS = "\u065E";

    public static String ARABIC_INDIC_DIGIT_ZERO = "\u0660";
    public static String ARABIC_INDIC_DIGIT_ONE = "\u0661";
    public static String ARABIC_INDIC_DIGIT_TWO = "\u0662";
    public static String ARABIC_INDIC_DIGIT_THREE = "\u0663";
    public static String ARABIC_INDIC_DIGIT_FOUR = "\u0664";
    public static String ARABIC_INDIC_DIGIT_FIVE = "\u0665";
    public static String ARABIC_INDIC_DIGIT_SIX = "\u0666";
    public static String ARABIC_INDIC_DIGIT_SEVEN = "\u0667";
    public static String ARABIC_INDIC_DIGIT_EIGHT = "\u0668";
    public static String ARABIC_INDIC_DIGIT_NINE = "\u0669";
    public static String ARABIC_PERCENT_SIGN = "\u066A";
    public static String ARABIC_DECIMAL_SEPARATOR = "\u066B";
    public static String ARABIC_THOUSANDS_SEPARATOR = "\u066C";
    public static String ARABIC_FIVE_POINTED_STAR = "\u066D";
    public static String ARABIC_LETTER_DOTLESS_BEH = "\u066E";
    public static String ARABIC_LETTER_DOTLESS_QAF = "\u066F";
    public static String ARABIC_LETTER_SUPERSCRIPT_ALEF = "\u0670";
    public static String ARABIC_LETTER_ALEF_WASLA = "\u0671";
    public static String ARABIC_LETTER_ALEF_WITH_WAVY_HAMZA_ABOVE = "\u0672";
    public static String ARABIC_LETTER_ALEF_WITH_WAVY_HAMZA_BELOW = "\u0673";
    public static String ARABIC_LETTER_HIGH_HAMZA = "\u0674";
    public static String ARABIC_LETTER_HIGH_HAMZA_ALEF = "\u0675";
    public static String ARABIC_LETTER_HIGH_HAMZA_WAW = "\u0676";
    public static String ARABIC_LETTER_U_WITH_HAMZA_ABOVE = "\u0677";
    public static String ARABIC_LETTER_HIGH_HAMZA_YEH = "\u0678";
    public static String ARABIC_LETTER_TTEH = "\u0679";
    public static String ARABIC_LETTER_TTEHEH = "\u067A";
    public static String ARABIC_LETTER_BEEH = "\u067B";
    public static String ARABIC_LETTER_TEH_WITH_RING = "\u067C";
    public static String ARABIC_LETTER_TEH_WITH_THREE_DOTS_ABOVE_DOWNWARDS = "\u067D";
    public static String ARABIC_LETTER_PEH = "\u067E";
    public static String ARABIC_LETTER_TEHEH = "\u067F";
    public static String ARABIC_LETTER_BEHEH = "\u0680";
    public static String ARABIC_LETTER_HAH_WITH_HAMZA_ABOVE = "\u0681";
    public static String ARABIC_LETTER_HAH_WITH_TWO_DOTS_VERTICAL_ABOVE = "\u0682";
    public static String ARABIC_LETTER_NYEH = "\u0683";
    public static String ARABIC_LETTER_DYEH = "\u0684";
    public static String ARABIC_LETTER_HAH_WITH_THREE_DOTS_ABOVE = "\u0685";
    public static String ARABIC_LETTER_TCHEH = "\u0686";
    public static String ARABIC_LETTER_TCHEHEH = "\u0687";
    public static String ARABIC_LETTER_DDAL = "\u0688";
    public static String ARABIC_LETTER_DAL_WITH_RING = "\u0689";
    public static String ARABIC_LETTER_DAL_WITH_DOT_BELOW = "\u068A";
    public static String ARABIC_LETTER_DAL_WITH_DOT_BELOW_AND_SMALL_TAH = "\u068B";
    public static String ARABIC_LETTER_DAHAL = "\u068C";
    public static String ARABIC_LETTER_DDAHAL = "\u068D";
    public static String ARABIC_LETTER_DUL = "\u068E";
    public static String ARABIC_LETTER_DAL_WITH_THREE_DOTS_ABOVE_DOWNWARDS = "\u068F";
    public static String ARABIC_LETTER_DAL_WITH_FOUR_DOTS_ABOVE = "\u0690";
    public static String ARABIC_LETTER_RREH = "\u0691";
    public static String ARABIC_LETTER_REH_WITH_SMALL_V = "\u0692";
    public static String ARABIC_LETTER_REH_WITH_RING = "\u0693";
    public static String ARABIC_LETTER_REH_WITH_DOT_BELOW = "\u0694";
    public static String ARABIC_LETTER_REH_WITH_SMALL_V_BELOW = "\u0695";
    public static String ARABIC_LETTER_REH_WITH_DOT_BELOW_AND_DOT_ABOVE = "\u0696";
    public static String ARABIC_LETTER_REH_WITH_TWO_DOTS_ABOVE = "\u0697";
    public static String ARABIC_LETTER_JEH = "\u0698";
    public static String ARABIC_LETTER_REH_WITH_FOUR_DOTS_ABOVE = "\u0699";
    public static String ARABIC_LETTER_SEEN_WITH_DOT_BELOW_AND_DOT_ABOVE = "\u069A";
    public static String ARABIC_LETTER_SEEN_WITH_THREE_DOTS_BELOW = "\u069B";
    public static String ARABIC_LETTER_SEEN_WITH_THREE_DOTS_BELOW_AND_THREE_DOTS_ABOVE = "\u069C";
    public static String ARABIC_LETTER_SAD_WITH_TWO_DOTS_BELOW = "\u069D";
    public static String ARABIC_LETTER_SAD_WITH_THREE_DOTS_ABOVE = "\u069E";
    public static String ARABIC_LETTER_TAH_WITH_THREE_DOTS_ABOVE = "\u069F";
    public static String ARABIC_LETTER_AIN_WITH_THREE_DOTS_ABOVE = "\u06A0";
    public static String ARABIC_LETTER_DOTLESS_FEH = "\u06A1";
    public static String ARABIC_LETTER_FEH_WITH_DOT_MOVED_BELOW = "\u06A2";
    public static String ARABIC_LETTER_FEH_WITH_DOT_BELOW = "\u06A3";
    public static String ARABIC_LETTER_VEH = "\u06A4";
    public static String ARABIC_LETTER_FEH_WITH_THREE_DOTS_BELOW = "\u06A5";
    public static String ARABIC_LETTER_PEHEH = "\u06A6";
    public static String ARABIC_LETTER_QAF_WITH_DOT_ABOVE = "\u06A7";
    public static String ARABIC_LETTER_QAF_WITH_THREE_DOTS_ABOVE = "\u06A8";
    public static String ARABIC_LETTER_KEHEH = "\u06A9";
    public static String ARABIC_LETTER_SWASH_KAF = "\u06AA";
    public static String ARABIC_LETTER_KAF_WITH_RING = "\u06AB";
    public static String ARABIC_LETTER_KAF_WITH_DOT_ABOVE = "\u06AC";
    public static String ARABIC_LETTER_NG = "\u06AD";
    public static String ARABIC_LETTER_KAF_WITH_THREE_DOTS_BELOW = "\u06AE";
    public static String ARABIC_LETTER_GAF = "\u06AF";
    public static String ARABIC_LETTER_GAF_WITH_RING = "\u06B0";
    public static String ARABIC_LETTER_NGOEH = "\u06B1";
    public static String ARABIC_LETTER_GAF_WITH_TWO_DOTS_BELOW = "\u06B2";
    public static String ARABIC_LETTER_GUEH = "\u06B3";
    public static String ARABIC_LETTER_GAF_WITH_THREE_DOTS_ABOVE = "\u06B4";
    public static String ARABIC_LETTER_LAM_WITH_SMALL_V = "\u06B5";
    public static String ARABIC_LETTER_LAM_WITH_DOT_ABOVE = "\u06B6";
    public static String ARABIC_LETTER_LAM_WITH_THREE_DOTS_ABOVE = "\u06B7";
    public static String ARABIC_LETTER_NOON_GHUNNA = "\u06BA";
    public static String ARABIC_LETTER_RNOON = "\u06BB";
    public static String ARABIC_LETTER_NOON_WITH_RING = "\u06BC";
    public static String ARABIC_LETTER_NOON_WITH_THREE_DOTS_ABOVE = "\u06BD";
    public static String ARABIC_LETTER_HEH_DOACHASHMEE = "\u06BE";
    public static String ARABIC_LETTER_HEH_WITH_YEH_ABOVE = "\u06C0";
    public static String ARABIC_LETTER_HEH_GOAL = "\u06C1";
    public static String ARABIC_LETTER_HEH_GOAL_WITH_HAMZA_ABOVE = "\u06C2";
    public static String ARABIC_LETTER_TEH_MARBUTA_GOAL = "\u06C3";
    public static String ARABIC_LETTER_WAW_WITH_RING = "\u06C4";
    public static String ARABIC_LETTER_KIRGHIZ_OE = "\u06C5";
    public static String ARABIC_LETTER_OE = "\u06C6";
    public static String ARABIC_LETTER_U = "\u06C7";
    public static String ARABIC_LETTER_YU = "\u06C8";
    public static String ARABIC_LETTER_KIRGHIZ_YU = "\u06C9";
    public static String ARABIC_LETTER_WAW_WITH_TWO_DOTS_ABOVE = "\u06CA";
    public static String ARABIC_LETTER_VE = "\u06CB";
    public static String ARABIC_LETTER_FARSI_YEH = "\u06CC";
    public static String ARABIC_LETTER_YEH_WITH_TAIL = "\u06CD";
    public static String ARABIC_LETTER_YEH_WITH_SMALL_V = "\u06CE";
    public static String ARABIC_LETTER_E = "\u06D0";
    public static String ARABIC_LETTER_YEH_WITH_THREE_DOTS_BELOW = "\u06D1";
    public static String ARABIC_LETTER_YEH_BARREE = "\u06D2";
    public static String ARABIC_LETTER_YEH_BARREE_WITH_HAMZA_ABOVE = "\u06D3";
    public static String ARABIC_FULL_STOP = "\u06D4";
    public static String ARABIC_LETTER_AE = "\u06D5";
    public static String ARABIC_SMALL_HIGH_LIGATURE_SAD_WITH_LAM_WITH_ALEF_MAKSURA = "\u06D6";
    public static String ARABIC_SMALL_HIGH_LIGATURE_QAF_WITH_LAM_WITH_ALEF_MAKSURA = "\u06D7";
    public static String ARABIC_SMALL_HIGH_MEEM_INITIAL_FORM = "\u06D8";
    public static String ARABIC_SMALL_HIGH_LAM_ALEF = "\u06D9";
    public static String ARABIC_SMALL_HIGH_JEEM = "\u06DA";
    public static String ARABIC_SMALL_HIGH_THREE_DOTS = "\u06DB";
    public static String ARABIC_SMALL_HIGH_SEEN = "\u06DC";
    public static String ARABIC_END_OF_AYAH = "\u06DD";
    public static String ARABIC_START_OF_RUB_EL_HIZB = "\u06DE";
    public static String ARABIC_SMALL_HIGH_ROUNDED_ZERO = "\u06DF";
    public static String ARABIC_SMALL_HIGH_UPRIGHT_RECTANGULAR_ZERO = "\u06E0";
    public static String ARABIC_SMALL_HIGH_DOTLESS_HEAD_OF_KHAH = "\u06E1";
    public static String ARABIC_SMALL_HIGH_MEEM_ISOLATED_FORM = "\u06E2";
    public static String ARABIC_SMALL_LOW_SEEN = "\u06E3";
    public static String ARABIC_SMALL_HIGH_MADDA = "\u06E4";
    public static String ARABIC_SMALL_WAW = "\u06E5";
    public static String ARABIC_SMALL_YEH = "\u06E6";
    public static String ARABIC_SMALL_HIGH_YEH = "\u06E7";
    public static String ARABIC_SMALL_HIGH_NOON = "\u06E8";
    public static String ARABIC_PLACE_OF_SAJDAH = "\u06E9";
    public static String ARABIC_EMPTY_CENTRE_LOW_STOP = "\u06EA";
    public static String ARABIC_EMPTY_CENTRE_HIGH_STOP = "\u06EB";
    public static String ARABIC_ROUNDED_HIGH_STOP_WITH_FILLED_CENTRE = "\u06EC";
    public static String ARABIC_SMALL_LOW_MEEM = "\u06ED";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_ZERO = "\u06F0";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_ONE = "\u06F1";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_TWO = "\u06F2";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_THREE = "\u06F3";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_FOUR = "\u06F4";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_FIVE = "\u06F5";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_SIX = "\u06F6";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_SEVEN = "\u06F7";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_EIGHT = "\u06F8";
    public static String EXTENDED_ARABIC_INDIC_DIGIT_NINE = "\u06F9";

}
