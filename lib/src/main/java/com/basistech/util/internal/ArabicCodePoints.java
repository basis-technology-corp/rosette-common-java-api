/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2003-2008 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/
//CHECKSTYLE:OFF
package com.basistech.util.internal;

/*
 * Constants for Arabic characters, to make it easier to express code that uses them.
 * This is a lot faster than a complete copy of the UCD in memory index with a hashtable
 * or whatever. The names here may seem cumbersome, but it preserves the use of the full UCD
 * names. A class using a lot of this could use the 'implements' trick to pull them
 * into the immediate namespace.
 */
public interface ArabicCodePoints {
    public static char ARABIC_COMMA = 0x060C;
    public static char ARABIC_SEMICOLON = 0x061B;
    public static char ARABIC_QUESTION_MARK = 0x061F;
    public static char ARABIC_LETTER_HAMZA = 0x0621;
    public static char ARABIC_LETTER_ALEF_WITH_MADDA_ABOVE = 0x0622;
    public static char ARABIC_LETTER_ALEF_WITH_HAMZA_ABOVE = 0x0623;
    public static char ARABIC_LETTER_WAW_WITH_HAMZA_ABOVE = 0x0624;
    public static char ARABIC_LETTER_ALEF_WITH_HAMZA_BELOW = 0x0625;
    public static char ARABIC_LETTER_YEH_WITH_HAMZA_ABOVE = 0x0626;
    public static char ARABIC_LETTER_ALEF = 0x0627;
    public static char ARABIC_LETTER_BEH = 0x0628;
    public static char ARABIC_LETTER_TEH_MARBUTA = 0x0629;
    public static char ARABIC_LETTER_TEH = 0x062A;
    public static char ARABIC_LETTER_THEH = 0x062B;
    public static char ARABIC_LETTER_JEEM = 0x062C;
    public static char ARABIC_LETTER_HAH = 0x062D;
    public static char ARABIC_LETTER_KHAH = 0x062E;
    public static char ARABIC_LETTER_DAL = 0x062F;
    public static char ARABIC_LETTER_THAL = 0x0630;
    public static char ARABIC_LETTER_REH = 0x0631;
    public static char ARABIC_LETTER_ZAIN = 0x0632;
    public static char ARABIC_LETTER_SEEN = 0x0633;
    public static char ARABIC_LETTER_SHEEN = 0x0634;
    public static char ARABIC_LETTER_SAD = 0x0635;
    public static char ARABIC_LETTER_DAD = 0x0636;
    public static char ARABIC_LETTER_TAH = 0x0637;
    public static char ARABIC_LETTER_ZAH = 0x0638;
    public static char ARABIC_LETTER_AIN = 0x0639;
    public static char ARABIC_LETTER_GHAIN = 0x063A;
    public static char ARABIC_TATWEEL = 0x0640;
    public static char ARABIC_LETTER_FEH = 0x0641;
    public static char ARABIC_LETTER_QAF = 0x0642;
    public static char ARABIC_LETTER_KAF = 0x0643;
    public static char ARABIC_LETTER_LAM = 0x0644;
    public static char ARABIC_LETTER_MEEM = 0x0645;
    public static char ARABIC_LETTER_NOON = 0x0646;
    public static char ARABIC_LETTER_HEH = 0x0647;
    public static char ARABIC_LETTER_WAW = 0x0648;
    public static char ARABIC_LETTER_ALEF_MAKSURA = 0x0649;
    public static char ARABIC_LETTER_YEH = 0x064A;
    public static char ARABIC_FATHATAN = 0x064B;
    public static char ARABIC_DAMMATAN = 0x064C;
    public static char ARABIC_KASRATAN = 0x064D;
    public static char ARABIC_FATHA = 0x064E;
    public static char ARABIC_DAMMA = 0x064F;
    public static char ARABIC_KASRA = 0x0650;
    public static char ARABIC_SHADDA = 0x0651;
    public static char ARABIC_SUKUN = 0x0652;
    public static char ARABIC_MADDAH_ABOVE = 0x0653;
    public static char ARABIC_HAMZA_ABOVE = 0x0654;
    public static char ARABIC_HAMZA_BELOW = 0x0655;
    public static char ARABIC_LETTER_SUBSCRIPT_ALEF = 0x0656;
    /* better name/additions 26-Oct-2005 12:42:35 pm */;
    public static char ARABIC_SUBSCRIPT_ALEF = 0x0656;
    public static char ARABIC_INVERTED_DAMMA = 0x0657;
    public static char ARABIC_MARK_NOON_GHUNNA = 0x0658;
    public static char ARABIC_ZWARAKAY = 0x0659;
    public static char ARABIC_VOWEL_SIGN_SMALL_V_ABOVE = 0x065A;
    public static char ARABIC_VOWEL_SIGN_INVERTED_SMALL_V_ABOVE = 0x065B;
    public static char ARABIC_VOWEL_SIGN_DOT_BELOW = 0x065C;
    public static char ARABIC_VOWEL_SIGN_REVERSED_DAMMA = 0x065D;
    public static char ARABIC_FATHA_WITH_TWO_DOTS = 0x065E;;
    public static char ARABIC_INDIC_DIGIT_ZERO = 0x0660;
    public static char ARABIC_INDIC_DIGIT_ONE = 0x0661;
    public static char ARABIC_INDIC_DIGIT_TWO = 0x0662;
    public static char ARABIC_INDIC_DIGIT_THREE = 0x0663;
    public static char ARABIC_INDIC_DIGIT_FOUR = 0x0664;
    public static char ARABIC_INDIC_DIGIT_FIVE = 0x0665;
    public static char ARABIC_INDIC_DIGIT_SIX = 0x0666;
    public static char ARABIC_INDIC_DIGIT_SEVEN = 0x0667;
    public static char ARABIC_INDIC_DIGIT_EIGHT = 0x0668;
    public static char ARABIC_INDIC_DIGIT_NINE = 0x0669;
    public static char ARABIC_PERCENT_SIGN = 0x066A;
    public static char ARABIC_DECIMAL_SEPARATOR = 0x066B;
    public static char ARABIC_THOUSANDS_SEPARATOR = 0x066C;
    public static char ARABIC_FIVE_POINTED_STAR = 0x066D;
    public static char ARABIC_LETTER_DOTLESS_BEH = 0x066E;
    public static char ARABIC_LETTER_DOTLESS_QAF = 0x066F;
    public static char ARABIC_LETTER_SUPERSCRIPT_ALEF = 0x0670;
    public static char ARABIC_LETTER_ALEF_WASLA = 0x0671;
    public static char ARABIC_LETTER_ALEF_WITH_WAVY_HAMZA_ABOVE = 0x0672;
    public static char ARABIC_LETTER_ALEF_WITH_WAVY_HAMZA_BELOW = 0x0673;
    public static char ARABIC_LETTER_HIGH_HAMZA = 0x0674;
    public static char ARABIC_LETTER_HIGH_HAMZA_ALEF = 0x0675;
    public static char ARABIC_LETTER_HIGH_HAMZA_WAW = 0x0676;
    public static char ARABIC_LETTER_U_WITH_HAMZA_ABOVE = 0x0677;
    public static char ARABIC_LETTER_HIGH_HAMZA_YEH = 0x0678;
    public static char ARABIC_LETTER_TTEH = 0x0679;
    public static char ARABIC_LETTER_TTEHEH = 0x067A;
    public static char ARABIC_LETTER_BEEH = 0x067B;
    public static char ARABIC_LETTER_TEH_WITH_RING = 0x067C;
    public static char ARABIC_LETTER_TEH_WITH_THREE_DOTS_ABOVE_DOWNWARDS = 0x067D;
    public static char ARABIC_LETTER_PEH = 0x067E;
    public static char ARABIC_LETTER_TEHEH = 0x067F;
    public static char ARABIC_LETTER_BEHEH = 0x0680;
    public static char ARABIC_LETTER_HAH_WITH_HAMZA_ABOVE = 0x0681;
    public static char ARABIC_LETTER_HAH_WITH_TWO_DOTS_VERTICAL_ABOVE = 0x0682;
    public static char ARABIC_LETTER_NYEH = 0x0683;
    public static char ARABIC_LETTER_DYEH = 0x0684;
    public static char ARABIC_LETTER_HAH_WITH_THREE_DOTS_ABOVE = 0x0685;
    public static char ARABIC_LETTER_TCHEH = 0x0686;
    public static char ARABIC_LETTER_TCHEHEH = 0x0687;
    public static char ARABIC_LETTER_DDAL = 0x0688;
    public static char ARABIC_LETTER_DAL_WITH_RING = 0x0689;
    public static char ARABIC_LETTER_DAL_WITH_DOT_BELOW = 0x068A;
    public static char ARABIC_LETTER_DAL_WITH_DOT_BELOW_AND_SMALL_TAH = 0x068B;
    public static char ARABIC_LETTER_DAHAL = 0x068C;
    public static char ARABIC_LETTER_DDAHAL = 0x068D;
    public static char ARABIC_LETTER_DUL = 0x068E;
    public static char ARABIC_LETTER_DAL_WITH_THREE_DOTS_ABOVE_DOWNWARDS = 0x068F;
    public static char ARABIC_LETTER_DAL_WITH_FOUR_DOTS_ABOVE = 0x0690;
    public static char ARABIC_LETTER_RREH = 0x0691;
    public static char ARABIC_LETTER_REH_WITH_SMALL_V = 0x0692;
    public static char ARABIC_LETTER_REH_WITH_RING = 0x0693;
    public static char ARABIC_LETTER_REH_WITH_DOT_BELOW = 0x0694;
    public static char ARABIC_LETTER_REH_WITH_SMALL_V_BELOW = 0x0695;
    public static char ARABIC_LETTER_REH_WITH_DOT_BELOW_AND_DOT_ABOVE = 0x0696;
    public static char ARABIC_LETTER_REH_WITH_TWO_DOTS_ABOVE = 0x0697;
    public static char ARABIC_LETTER_JEH = 0x0698;
    public static char ARABIC_LETTER_REH_WITH_FOUR_DOTS_ABOVE = 0x0699;
    public static char ARABIC_LETTER_SEEN_WITH_DOT_BELOW_AND_DOT_ABOVE = 0x069A;
    public static char ARABIC_LETTER_SEEN_WITH_THREE_DOTS_BELOW = 0x069B;
    public static char ARABIC_LETTER_SEEN_WITH_THREE_DOTS_BELOW_AND_THREE_DOTS_ABOVE = 0x069C;
    public static char ARABIC_LETTER_SAD_WITH_TWO_DOTS_BELOW = 0x069D;
    public static char ARABIC_LETTER_SAD_WITH_THREE_DOTS_ABOVE = 0x069E;
    public static char ARABIC_LETTER_TAH_WITH_THREE_DOTS_ABOVE = 0x069F;
    public static char ARABIC_LETTER_AIN_WITH_THREE_DOTS_ABOVE = 0x06A0;
    public static char ARABIC_LETTER_DOTLESS_FEH = 0x06A1;
    public static char ARABIC_LETTER_FEH_WITH_DOT_MOVED_BELOW = 0x06A2;
    public static char ARABIC_LETTER_FEH_WITH_DOT_BELOW = 0x06A3;
    public static char ARABIC_LETTER_VEH = 0x06A4;
    public static char ARABIC_LETTER_FEH_WITH_THREE_DOTS_BELOW = 0x06A5;
    public static char ARABIC_LETTER_PEHEH = 0x06A6;
    public static char ARABIC_LETTER_QAF_WITH_DOT_ABOVE = 0x06A7;
    public static char ARABIC_LETTER_QAF_WITH_THREE_DOTS_ABOVE = 0x06A8;
    public static char ARABIC_LETTER_KEHEH = 0x06A9;
    public static char ARABIC_LETTER_SWASH_KAF = 0x06AA;
    public static char ARABIC_LETTER_KAF_WITH_RING = 0x06AB;
    public static char ARABIC_LETTER_KAF_WITH_DOT_ABOVE = 0x06AC;
    public static char ARABIC_LETTER_NG = 0x06AD;
    public static char ARABIC_LETTER_KAF_WITH_THREE_DOTS_BELOW = 0x06AE;
    public static char ARABIC_LETTER_GAF = 0x06AF;
    public static char ARABIC_LETTER_GAF_WITH_RING = 0x06B0;
    public static char ARABIC_LETTER_NGOEH = 0x06B1;
    public static char ARABIC_LETTER_GAF_WITH_TWO_DOTS_BELOW = 0x06B2;
    public static char ARABIC_LETTER_GUEH = 0x06B3;
    public static char ARABIC_LETTER_GAF_WITH_THREE_DOTS_ABOVE = 0x06B4;
    public static char ARABIC_LETTER_LAM_WITH_SMALL_V = 0x06B5;
    public static char ARABIC_LETTER_LAM_WITH_DOT_ABOVE = 0x06B6;
    public static char ARABIC_LETTER_LAM_WITH_THREE_DOTS_ABOVE = 0x06B7;
    public static char ARABIC_LETTER_NOON_GHUNNA = 0x06BA;
    public static char ARABIC_LETTER_RNOON = 0x06BB;
    public static char ARABIC_LETTER_NOON_WITH_RING = 0x06BC;
    public static char ARABIC_LETTER_NOON_WITH_THREE_DOTS_ABOVE = 0x06BD;
    public static char ARABIC_LETTER_HEH_DOACHASHMEE = 0x06BE;
    public static char ARABIC_LETTER_HEH_WITH_YEH_ABOVE = 0x06C0;
    public static char ARABIC_LETTER_HEH_GOAL = 0x06C1;
    public static char ARABIC_LETTER_HEH_GOAL_WITH_HAMZA_ABOVE = 0x06C2;
    public static char ARABIC_LETTER_TEH_MARBUTA_GOAL = 0x06C3;
    public static char ARABIC_LETTER_WAW_WITH_RING = 0x06C4;
    public static char ARABIC_LETTER_KIRGHIZ_OE = 0x06C5;
    public static char ARABIC_LETTER_OE = 0x06C6;
    public static char ARABIC_LETTER_U = 0x06C7;
    public static char ARABIC_LETTER_YU = 0x06C8;
    public static char ARABIC_LETTER_KIRGHIZ_YU = 0x06C9;
    public static char ARABIC_LETTER_WAW_WITH_TWO_DOTS_ABOVE = 0x06CA;
    public static char ARABIC_LETTER_VE = 0x06CB;
    public static char ARABIC_LETTER_FARSI_YEH = 0x06CC;
    public static char ARABIC_LETTER_YEH_WITH_TAIL = 0x06CD;
    public static char ARABIC_LETTER_YEH_WITH_SMALL_V = 0x06CE;
    public static char ARABIC_LETTER_E = 0x06D0;
    public static char ARABIC_LETTER_YEH_WITH_THREE_DOTS_BELOW = 0x06D1;
    public static char ARABIC_LETTER_YEH_BARREE = 0x06D2;
    public static char ARABIC_LETTER_YEH_BARREE_WITH_HAMZA_ABOVE = 0x06D3;
    public static char ARABIC_FULL_STOP = 0x06D4;
    public static char ARABIC_LETTER_AE = 0x06D5;
    public static char ARABIC_SMALL_HIGH_LIGATURE_SAD_WITH_LAM_WITH_ALEF_MAKSURA = 0x06D6;
    public static char ARABIC_SMALL_HIGH_LIGATURE_QAF_WITH_LAM_WITH_ALEF_MAKSURA = 0x06D7;
    public static char ARABIC_SMALL_HIGH_MEEM_INITIAL_FORM = 0x06D8;
    public static char ARABIC_SMALL_HIGH_LAM_ALEF = 0x06D9;
    public static char ARABIC_SMALL_HIGH_JEEM = 0x06DA;
    public static char ARABIC_SMALL_HIGH_THREE_DOTS = 0x06DB;
    public static char ARABIC_SMALL_HIGH_SEEN = 0x06DC;
    public static char ARABIC_END_OF_AYAH = 0x06DD;
    public static char ARABIC_START_OF_RUB_EL_HIZB = 0x06DE;
    public static char ARABIC_SMALL_HIGH_ROUNDED_ZERO = 0x06DF;
    public static char ARABIC_SMALL_HIGH_UPRIGHT_RECTANGULAR_ZERO = 0x06E0;
    public static char ARABIC_SMALL_HIGH_DOTLESS_HEAD_OF_KHAH = 0x06E1;
    public static char ARABIC_SMALL_HIGH_MEEM_ISOLATED_FORM = 0x06E2;
    public static char ARABIC_SMALL_LOW_SEEN = 0x06E3;
    public static char ARABIC_SMALL_HIGH_MADDA = 0x06E4;
    public static char ARABIC_SMALL_WAW = 0x06E5;
    public static char ARABIC_SMALL_YEH = 0x06E6;
    public static char ARABIC_SMALL_HIGH_YEH = 0x06E7;
    public static char ARABIC_SMALL_HIGH_NOON = 0x06E8;
    public static char ARABIC_PLACE_OF_SAJDAH = 0x06E9;
    public static char ARABIC_EMPTY_CENTRE_LOW_STOP = 0x06EA;
    public static char ARABIC_EMPTY_CENTRE_HIGH_STOP = 0x06EB;
    public static char ARABIC_ROUNDED_HIGH_STOP_WITH_FILLED_CENTRE = 0x06EC;
    public static char ARABIC_SMALL_LOW_MEEM = 0x06ED;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_ZERO = 0x06F0;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_ONE = 0x06F1;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_TWO = 0x06F2;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_THREE = 0x06F3;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_FOUR = 0x06F4;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_FIVE = 0x06F5;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_SIX = 0x06F6;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_SEVEN = 0x06F7;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_EIGHT = 0x06F8;
    public static char EXTENDED_ARABIC_INDIC_DIGIT_NINE = 0x06F9;

}
