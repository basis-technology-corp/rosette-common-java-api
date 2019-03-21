//CHECKSTYLE:OFF
/*
[= dne(" * ") =] 
*/
/*
* Copyright 2014-2019 Basis Technology Corp.
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

package com.basistech.util;

import java.util.HashMap;


/**
 * Named constants for the numeric codes for BT_Xlit_Scheme.
 * NOTE: see the documentation of specific Basis products to see which schemes they support.
 */
public enum TransliterationScheme
{   [< for (xlitSchemesdefinitions, ",") >]

    /** xlit scheme code for [= scheme['presentationName'] =]
            description: [= scheme.get('description', '') =] */
    [= scheme['name'].upper() =] ([= scheme['numeric'] =], "[= scheme['name'] =]", "[= scheme['presentationName'] =]",
        "[= scheme.get('description', '') =]")[< end-for >];

    private final int BT_Xlit_Scheme;
    private final String name; 
    private final String presentationName;
    private final String description;


    /*
     * We employ a variation of "Initialization on demand holder" idiom
     * http://en.wikipedia.org/wiki/Initialization_on_demand_holder_idiom
     * to implement lazy initialization of the two HashMaps we use.
     * The following two small private static classes are for this trick.
     */
    private static class CatalogByBTXlitScheme{
         private static final HashMap<Integer, TransliterationScheme> it;
         static{
            it = new HashMap<Integer, TransliterationScheme>();
            for (TransliterationScheme t : values()) {
                it.put(t.BT_Xlit_Scheme, t);
            }
         }
    }

    private static class CatalogByName{
        private static HashMap<String, TransliterationScheme> it;
        static{
            it = new HashMap<String, TransliterationScheme>();
            for (TransliterationScheme t : values()) {
                it.put(t.name, t);
            }
        }
    }

    TransliterationScheme(int BT_Xlit_Scheme, String name, String presentationName,
            String description){
        this.BT_Xlit_Scheme = BT_Xlit_Scheme;
        this.name = name;
        this.presentationName = presentationName;
        this.description = description;
    }

    // want these to not be in the .tpl
    // might like these next few to only be Basis accessible (how?) 
    /**
     * Retrieves a static TranslitearationScheme object of the given id.
     * @param BT_Xlit_Scheme numeric id of the scheme to get.
     * @return TransliterationScheme object corresponding to the param.
     */
    public static TransliterationScheme getObjectByBT_Xlit_Scheme(int BT_Xlit_Scheme) {

        TransliterationScheme ts = CatalogByBTXlitScheme.it.get(BT_Xlit_Scheme);
        if(ts == null)
            throw new IllegalArgumentException("Unknown BT_Xlit_Scheme code: "+BT_Xlit_Scheme);
        return ts;
    }
    
    static TransliterationScheme lookupByNativeCode(int nativeCode) {
        return getObjectByBT_Xlit_Scheme(nativeCode);
    }
    
    /**
     * Returns the numeric code (C++ BT_Xlit_Scheme) for this transliteration scheme.
     * @return Numeric code
     **/

    public int getNativeCode() {
        return BT_Xlit_Scheme;
    }

    /**
     * Retrieves a static TransliterationScheme object of the given name.
     * @param name Name of the scheme to retrieve.
     * @return TransliterationScheme object corresponding to the param.
     */
    public static TransliterationScheme getObjectByName(String name) {

        TransliterationScheme ts = CatalogByName.it.get(name);
        if(ts == null)
            throw new IllegalArgumentException("Unknown TransliterationScheme name: "+name);
        return ts;
    }
    
    /**
     * Gets the internally recognized name, such as what might be passed into NameTranslator.translateName or would be used to create a new TransliterationScheme
     * @return Internal name for the transliteration scheme
     */
    public String getName(){
        return name;
    }
    
    /**
     * Gets the name as should be presented visibly to a user
     * @return Transliteration scheme as should be viewed on a form or in output
     */
    public String getPresentationName(){
        return presentationName;
    }
    
    /**
     * Gets the description of the transliteration scheme, which generally would be displayed, for example, on a form when a particular scheme is chosen from a dropdown box
     * @return Short description of this particular scheme
     */
    public String getDescription(){
        return description;
    }
}
