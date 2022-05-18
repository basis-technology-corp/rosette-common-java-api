/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

//CHECKSTYLE:OFF
/*
[= dne(" * ") =]
 */
package com.basistech.util;

import java.util.Map;
import java.util.HashMap;

/**
Enumeration for a set of ISO 639-based language codes used in Basis products.<br>
<br>

<code>LanguageCodes</code> are based on the Feb 10, 2009 version of ISO 639-3.  A <code>LanguageCode</code> is either
standard, meaning that it is based on an ISO 639-3 language code, or nonstandard, meaning that it is a Basis extension.
The nonstandard <code>LanguageCodes</code> are:

<ul>
<li>{@link #UNKNOWN UNKNOWN}</li>
<li>{@link #SIMPLIFIED_CHINESE SIMPLIFIED_CHINESE}</li>
<li>{@link #TRADITIONAL_CHINESE TRADITIONAL_CHINESE}</li>
<li>{@link #ENGLISH_UPPERCASE ENGLISH_UPPERCASE}</li>
</ul>

<code>LanguageCodes</code> have the following attributes.

<ul>

<li>{@linkplain #languageName() Name}:

An ASCII, English name for the language.  For standard <code>LanguageCodes</code>, it is based on the ISO 639-3
reference name field; in some cases it is a simplified version of the field.  No two <code>LanguageCodes</code> have the
same value of this attribute.

<li>{@linkplain #ISO639_3() ISO 639-3 code}:

For standard <code>LanguageCodes</code>, it is a three-letter ISO 639-3 code.  For nonstandard
<code>LanguageCodes</code>, it is a three-letter code different from any ISO 639-3 code.  No two
<code>LanguageCodes</code> have the same value of this attribute.

<li>{@linkplain #ISO639_1() ISO 639-1 code}:

In the ISO 639-3 specification, all languages have a three-letter code, and some languages also have a two-letter ISO
639-1 code.

For standard <code>LanguageCodes</code>, this attribute is either a two-letter ISO 639-1 code, or {@link
#UNCODED_ISO639_1 UNCODED_ISO639_1} (<code>"zz"</code>).  For nonstandard <code>LanguageCodes</code>, this attribute is
either <code>"xx"</code> for {@link #UNKNOWN UNKNOWN}, or a five-letter code of the form <code>ab_cde</code>.  Except
for {@link #UNCODED_ISO639_1 UNCODED_ISO639_1}, no two <code>LanguageCodes</code> have the same value of this attribute.

<li>{@linkplain #getDefaultScript() Default script}:

For languages predominantly written in only one script, this attribute is that script.  For other languages, it is
{@link ISO15924#Zyyy}.  This mapping from languages to scripts is provided by Basis; it does not directly correspond to
any ISO data.

<li>{@linkplain #languageID() Numeric ID}:

A unique integer for each LanguageCode.  It is not necessarily the same value as the position of the
<code>LanguageCode</code> in the result of {@link #values() values()}.

</ul>
    
*/

[< if 0 >]  
Set include_aux_name to be non-zero (e.g 1) if the code related to auxiliary names are to be included in the auto
generated file.  This will add a new String[] field to the enum LanguageCode, which is a String[] of all the auxilary
names for the lanugage.
[< end-if >] 
[% include_aux_name = 0 %]  
public enum LanguageCode {
[% id = iter(xrange(len(language_names))) %][< for (language_names, ',') >][%

# Generate the declaration of the current element.

iso639_1 = language['iso639-1']
if "basis_iso639_1_suffix" in language:
    iso639_1 += language['basis_iso639_1_suffix']

declaration  = (                    language['symbol']
                + " ("            + language['id']
                + ", \""          + language['iso639-3']
                + "\", \""        + iso639_1
                + "\", \""        + language['name']
                + "\", ISO15924." + language['script']
                )

if (include_aux_name):
    declaration += ", new String[] {"
    for field in ['aux_name0', 'aux_name1', 'aux_name2', 'aux_name3']:
        if field in language:
            if field != 'aux_name0':
                declaration += ","
            declaration += "\"" + language[field] + "\""
    declaration += "}"

declaration += ")"

%]
    /** <table border="1">
        <caption>Language Codes</caption>
        <tr >
            <td style='width: 100;'>[= language['name'] =]</td >
            <td style='width: 100;'>[= language['iso639-3'] =]</td >
            <td style='width: 100;'>[= iso639_1 =]</td >
            <td style='width: 100;'>[= language['script'] =]</td >
            <td style='width: 100;'>[= language['id'] =]</td >
        </tr >
        </table >
    */
    [= declaration =][< end-for >],
    ;
	
    private int id;
    private String iso3;
    private String iso1;
    private String name;
    private ISO15924 defaultScript;
    [< if include_aux_name >]private String[] aux_names;[< end-if >]
    
[% aux_name_param = ", String[] aux_names " if include_aux_name else ""
%]    LanguageCode(int id, String iso3, String iso1, String name, ISO15924 defaultScript [= aux_name_param =]) {
        this.id = id;
        this.iso3 = iso3;
        this.iso1 = iso1;
        this.name = name;
        this.defaultScript = defaultScript;
        [< if include_aux_name >]this.aux_names = aux_names;[< end-if>]
    }
    
    /**
     * Returns the numeric ID attribute.
     * @return the numeric ID attribute.
     */
    public int languageID() {
        return id;
    }
    
    /**
     * Returns the ISO639-1 code attribute.
     * @return the ISO639-1 code attribute.
     */
    public String ISO639_1() {
        return iso1;
    }
    
    /**
     * Returns the ISO639-3 code attribute.
     * @return the ISO639-3 code attribute.
     */
    public String ISO639_3() {
    	return iso3;
    }

    /**
     * Returns the default script attribute.
     * @return the default script attribute.
     */
    public ISO15924 getDefaultScript() {
        return defaultScript;
    }
    
    /**
     * Returns the name attribute.
     * @return the name attribute.
     */
    public String languageName() {
        return name;
    }
    [< if include_aux_name >]
    /**
    * Get the Array of auxiliary names for this language
    * @return the string array of auxiliary names for this language.
    */
    
    public String[] auxiliaryNames() {
		return aux_names;
	}
	[< end-if >]

    // "NativeCode" is a convention in com.basistech.jnigen.BeanJNIGenerator.ClassInfo.MethodIDInitialization.
                                                                            
    /**
     * Get the numeric ID for this language.
     * @return the numeric ID for this language.
     */
    int getNativeCode() {
        return id;
    }
    
    static LanguageCode lookupByNativeCode(int nativeCode) {
        if (nativeCode < 0 || values().length <= nativeCode)
            throw new IllegalArgumentException("Invalid Language ID native code " + nativeCode);
        else
            return values()[id_index[nativeCode]];
    }

    /**
     * Returns whether there is a <code>LanguageCode</code> with ID <code>languageID</code>.
     * @param languageID the numeric ID of a <code>LanguageCode</code>.
     * @return whether there is a <code>LanguageCode</code> with ID <code>languageID</code>.
     */
    // This gives callers a way to check lookup validity if they're working with attribute values from a messy or
    // dangerous source such as a file.  It wouldn't be so bad to force callers to just probe for the exception, but
    // this way is a little cleaner.  Providing this function also makes the API more parallel to the C++ API, where
    // the analagous probe would have been harder for callers to do without being careful.
    //
    // We could even provide these and the lookup functions as Maps, but that's a little more functionality than I think
    // callers need, and we'd have to do more work to implement Map for the nice fast array lookup that we use for the
    // lookup by ID.  Not that I know it's performance-sensitive, but the speed is nice to have.
    // 
    public static boolean LanguageIDIsValid(int languageID) {
        try {
            lookupByLanguageID(languageID);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns the <code>LanguageCode</code> with ID <code>languageID</code>.
     * @param languageID the numeric ID of a <code>LanguageCode</code>.
     * @return the <code>LanguageCode</code> with ID <code>languageID</code>.
     * @throws IllegalArgumentException if there is no such <code>LanguageCode</code>.
     */
    public static LanguageCode lookupByLanguageID(int languageID) {
        return lookupByNativeCode(languageID);
    }

    /**
     * Returns whether there is a <code>LanguageCode</code> with ISO code attribute <code>iso639</code>.
     * @param iso639 An ISO code attribute of a <code>LanguageCode</code>: either its ISO 639-3 code attribute, or its
     * ISO 639-1 code attribute.  The comparison is case-sensitive.  Returns <code>false</code> for {@link
     * #UNCODED_ISO639_1 UNCODED_ISO639_1}.
     * @return whether there is a <code>LanguageCode</code> with ISO code attribute <code>iso639</code>.
     */
    // See LanguageIDIsValid for notes.
    public static boolean ISO639IsValid(String iso639) {
        try {
            lookupByISO639(iso639);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Returns the <code>LanguageCode</code> with ISO code attribute <code>iso639</code>.
     * @param iso639 An ISO code attribute of a <code>LanguageCode</code>: either its ISO 639-3 code attribute, or its
     * ISO 639-1 code attribute (but not {@link #UNCODED_ISO639_1 UNCODED_ISO639_1}, because that value does not
     * uniquely identify a language code.)  The comparison is case-sensitive.
     * @return the <code>LanguageCode</code> with ISO code attribute <code>iso639</code>.
     * @throws IllegalArgumentException if there is no such <code>LanguageCode</code>, or if <code>iso639</code> equals
     * {@link #UNCODED_ISO639_1 UNCODED_ISO639_1}.
     */
    public static LanguageCode lookupByISO639(String iso639){
        int size = iso639.length();
        LanguageCode result = null;
        if (size == ISO639_1_CODE_LENGTH || size == ISO639_1_BASIS_CODE_LENGTH)
            result = iso639_1_index.get(iso639);
        else
            result = iso639_3_index.get(iso639);
        if (result == null)
            throw new IllegalArgumentException("Invalid ISO639 " + iso639);
        else
            return result;
    }
    
    /**
    * If the given <code>LanguageCode</code> is non-standard, returns the ISO 639-3 standard <code>LanguageCode</code> that best encapsulates it.
    * This does not apply to {@link #UNKNOWN UNKNOWN}. If the given <code>LanguageCode</code> is already standard, it is returned as it is.
    * @param lc A <code>LanguageCode</code>.
    * @return the standard <code>LanguageCode</code> that encapsulates <code>lc</code> if it is non-standard. Otherwise returns <code>lc</code>.
    */
    public static LanguageCode normalizeNonStandard(LanguageCode lc) {
        switch(lc) {
            case SIMPLIFIED_CHINESE:
            case TRADITIONAL_CHINESE:
                return LanguageCode.CHINESE;
            case ENGLISH_UPPERCASE:
                return LanguageCode.ENGLISH;
            default: 
                return lc;
        }
    }
    
    /** The string <code>"zz"</code>, used as the ISO 639-1 attribute for languages present in the ISO 639-3
     * specification, but for which the ISO 639-1 specification does not define a code.
     */
    public static final String UNCODED_ISO639_1 = "zz";

    private static final int ISO639_1_CODE_LENGTH = 2;
    private static final int ISO639_1_BASIS_CODE_LENGTH = 5; // 2 + underscore + 2-char-suffix

    // Indexes to look up elements by their attributes.
    // 
    // In C++ the string lookups are implemented with code-generated static const sorted lists of strings that are
    // binary searched.  We could do that here, but this implementation was faster to program.  Furthermore, static
    // initialization is more predictable in Java (called once each time the class is loaded), so the compile-time setup
    // that we do in C++ is not needed for that purpose.  I'm not even certain that hash tables are needed (the previous
    // version used linear search), but I'm making this sub-linear because the C++ version is sub-linear.
    private static Map<String, LanguageCode> iso639_1_index;
    private static Map<String, LanguageCode> iso639_3_index;
    private static int[] id_index; // id_index[id] = position of language code #id in values()
    private static final float HASHMAP_DEFAULT_LOAD_FACTOR = 0.75f;
    //
    // Initialization
    // 
    static
    {
        // Initialize the attribute indexes.
        
        int indexCapacity = (int) (values().length / HASHMAP_DEFAULT_LOAD_FACTOR);
        iso639_1_index = new HashMap<String, LanguageCode>(indexCapacity);
        iso639_3_index = new HashMap<String, LanguageCode>(indexCapacity);
        id_index = new int[values().length];
        LanguageCode[] values = values();
        for (int valuesIdx = 0; valuesIdx < values.length; valuesIdx++)
        {
            LanguageCode code = values[valuesIdx];        
            if (! code.ISO639_1().equals(UNCODED_ISO639_1))
                iso639_1_index.put(code.ISO639_1(), code);
            iso639_3_index.put(code.ISO639_3(), code);
            id_index[code.languageID()] = valuesIdx;
        }
    }
}
