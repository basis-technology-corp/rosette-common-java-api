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

/* NOTE: This list MUST be alphabetically ordered by the first field because 
 * RLI (binary) searches will rely on it.
 */

package com.basistech.util;

/**
 * This is a list of many encoding names in mime name alphabetical order.
 */
public enum EncodingCode {

    Big5("Big5", "Big5", "Ascii"),
    EucJp("EUC-JP", "EucJp", "Ascii"),
    EucKr("EUC-KR", "EucKr", "Ascii"),
    Gb18030("GB18030", "Gb18030", "Gb2312"),
    Gb2312("GB2312", "Gb2312", "Ascii"),
    Hz("HZ-GB-2312", "Hz", "Hz"),
    Cp866("IBM866", "Cp866", "Ascii"),
    ISCIIBengali("ISCII-Bengali", "ISCIIBengali", "Ascii"),
    ISCIIDevanagari("ISCII-Devanagari", "ISCIIDevanagari", "Ascii"),
    ISCIIGujarati("ISCII-Gujarati", "ISCIIGujarati", "Ascii"),
    ISCIIKannada("ISCII-Kannada", "ISCIIKannada", "Ascii"),
    ISCIIMalayalam("ISCII-Malayalam", "ISCIIMalayalam", "Ascii"),
    ISCIITamil("ISCII-Tamil", "ISCIITamil", "Ascii"),
    ISCIITelugu("ISCII-Telugu", "ISCIITelugu", "Ascii"),
    Iso2022cn("ISO-2022-CN", "Iso2022cn", "Iso2022cn"),
    Iso2022jp("ISO-2022-JP", "Iso2022jp", "Iso2022jp"),
    Iso2022kr("ISO-2022-KR", "Iso2022kr", "Iso2022kr"),
    Latin1("ISO-8859-1", "Latin1", "Ascii"),
    Latin7("ISO-8859-13", "Latin7", "Ascii"),
    Latin2("ISO-8859-2", "Latin2", "Ascii"),
    Latin3("ISO-8859-3", "Latin3", "Ascii"),
    Latin4("ISO-8859-4", "Latin4", "Ascii"),
    LatinCyrillic("ISO-8859-5", "LatinCyrillic", "Ascii"),
    LatinArabic("ISO-8859-6", "LatinArabic", "Ascii"),
    LatinGreek("ISO-8859-7", "LatinGreek", "Ascii"),
    LatinHebrew("ISO-8859-8", "LatinHebrew", "Ascii"),
    Latin5("ISO-8859-9", "Latin5", "Ascii"),
    Koi8R("KOI8-R", "Koi8R", "Ascii"),
    ShiftJis("Shift_JIS", "ShiftJis", "Ascii"),
    ShiftJis2004("Shift_JIS-2004", "ShiftJis2004", "ShiftJis"),
    Tcvn("TCVN", "Tcvn", "Tcvn"),
    Unknown("Unknown", "Unknown", "Unknown"),
    Ascii("US-ASCII", "Ascii", "Ascii"),
    Utf16("UTF-16", "Utf16", "Utf8"),
    Utf16be("UTF-16BE", "Utf16be", "Utf8"),
    Utf16le("UTF-16LE", "Utf16le", "Utf8"),
    Utf8("UTF-8", "Utf8", "Ascii"),
    Viqr("VIQR", "Viqr", "Viqr"),
    Viscii("VISCII", "Viscii", "Viscii"),
    Vni("VNI", "Vni", "Vni"),
    Vps("VPS", "Vps", "Vps"),
    Cp1250("windows-1250", "Cp1250", "Latin2"),
    Cp1251("windows-1251", "Cp1251", "Ascii"),
    Cp1252("windows-1252", "Cp1252", "Latin1"),
    Cp1253("windows-1253", "Cp1253", "LatinGreek"),
    Cp1254("windows-1254", "Cp1254", "Latin5"),
    Cp1255("windows-1255", "Cp1255", "LatinHebrew"),
    Cp1256("windows-1256", "Cp1256", "Ascii"),
    Cp1257("windows-1257", "Cp1257", "Latin7"),
    Cp1258("windows-1258", "Cp1258", "Ascii"),
    Cp720("windows-720", "Cp720", "Ascii"),
    Cp874("windows-874", "Cp874", "Ascii"),
    MacCyrillic("x-mac-cyrillic", "MacCyrillic", "Ascii");
        
    private String mimeName;           // Clear text MIME name
    private String typeName;
    private String demotionTypeName;  // encoding that this encoding can be demoted to

    EncodingCode(String mimeName, String typeName, String demotionTypeName) {
        this.mimeName = mimeName;
        this.typeName = typeName;
        this.demotionTypeName = demotionTypeName;
    }

    /**
     * Get the mime name for this encoding.
     * 
     * @return the mime name for this encoding.
     */
    public String getMimeName() {
        return mimeName;
    }

    /**
     * Get the type name for this encoding.
     * 
     * @return the type name for this encoding.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Get the demotion type name code for this encoding.
     * 
     * @return the demotion type name code for this encoding.
     */
    public String getDemotionTypeName() {
        return demotionTypeName;
    }

    /**
     * Take a mime name and return an enumerator.
     * 
     * @param name mime name
     * @return enumerator for this encoding
     */
    public static EncodingCode lookupByMimeName(String name) {
        for (EncodingCode v : EncodingCode.values()) {
            if (v.getMimeName().equalsIgnoreCase(name)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid mime name " + name);
    }

    /**
     * Take a encoding name and return an enumerator.
     * 
     * @param name encoding name string
     * @return enumerator for this encoding
     */
    public static EncodingCode lookupByTypeName(String name) {
        for (EncodingCode v : EncodingCode.values()) {
            if (v.getTypeName().equalsIgnoreCase(name)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid type name " + name);
    }
    
    public static int getCount() {
        return values().length;
    }
}
