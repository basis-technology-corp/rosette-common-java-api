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

/**
 * Java enumeration for the ISO15924 system of script codes. There is one enumerated item for each defined code,
 * named after its CODE4 value. Accessors deliver the numeric and English-name properties of each script.
 */
public enum ISO15924 {      
[% id = iter(xrange(len(iso15924definitions))) %][< for (iso15924definitions, ',') >]
    /** for "[= code['name'] =]" */
    [= code['char4'] =] ([= int(code['numeric']) =], "[= code['name'] =]")[< end-for >];
    
    private final int numeric;
    private final String english;
    
    ISO15924(int numeric, String english) {
    	this.numeric = numeric;
       this.english = english;
    } 
    
    /**
     * Get the 4-character code for this script. This returns the same value as {@link #name()}.
     * @return the 4-character code for this script.
     */
    public String code4() {
        return name();
    }
    
    /**
     * Get the numeric code for this script.
     * @return the numeric code for this script.
     */
    public int numeric() {
    	return this.numeric;
    }
    
    /**
     * Get the English name for this script.
     * @return the English name for this script.
     */
    public String englishName () {
        return english;
    }
    
    /**
     * Get the numeric code for this script.
     * @return the numeric code for this script.
     */
    int getNativeCode() {
    	return numeric;
    }
    private static ISO15924[] staticValues = values();
    
    /**
     * Locate a script by native code.
     * @param nativeCode
     * @return
     */
    static ISO15924 lookupByNativeCode(int nativeCode) {
    	for(ISO15924 v : staticValues) {
    		if(v.getNativeCode() == nativeCode)
    			return v;
    	}
    	throw new RuntimeException("Invalid ISO15924 native code " + nativeCode);
    }
    
    /**
     * Locate a script by numeric code value.
     * @param numeric the numeric value.
     * @return the enumerated value.
     */
    public static ISO15924 lookupByNumeric(int numeric) {
    	return lookupByNativeCode(numeric);
    }
    
    /**
     * Locate a script by 4-character code value.
     * @param code4 the 4-character code value.
     * @return the enumerated value.
     */
    public static ISO15924 lookupByCode4(String code4) {
    	for(ISO15924 v: staticValues) {
    		if(code4.equals(v.toString()))
    			return v;
    	}
    	return null;
    }
}
