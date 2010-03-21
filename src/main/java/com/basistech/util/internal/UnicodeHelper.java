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
package com.basistech.util.internal;

public final class UnicodeHelper {
    private UnicodeHelper() {
        
    }

    /**
     * Return an escaped Unicode representation of a string
     * 
     * @param The Unicode input string
     * @param boolean specifying whether to escape ASCII chars or not
     * @return The escaped output string
     */
    public static String escapeUnicodeString(String str, boolean escapeAscii) {
        StringBuffer ostr = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!escapeAscii && ch >= 0x0020 && ch <= 0x007e) {
                ostr.append(ch);
            } else {
                ostr.append("\\u");
                String hex = Integer.toHexString(str.charAt(i) & 0xFFFF);
                for (int j = 0; j < 4 - hex.length(); j++) {
                    ostr.append("0");
                }
                ostr.append(hex.toUpperCase(java.util.Locale.ENGLISH));
            }
        }
        return new String(ostr);
    }

    /**
     * Overloaded version of escapeUnicodeString(String,boolean) just calls escapeUnicodeString(String,false)
     * i.e. and always leaves the ASCII chars unescaped.
     * 
     * @param The Unicode input string
     * @return The escaped output string
     */
    public static String escapeUnicodeString(String str) {
        return escapeUnicodeString(str, false);
    }
}
