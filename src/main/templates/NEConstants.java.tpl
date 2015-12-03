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
/*
[= dne(" * ") =] 
*/
package com.basistech.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Constants used for labeling Named Entities.
 *
 **/
@SuppressWarnings("PMD")
public final class NEConstants {
    /** Start of the user-defined NE type space */
    public static final int NE_TYPE_USER_DEFINED_START = 0x1000 << 16;
[% id = iter(xrange(len(ne_types['entity_types']))) %][< for ne_types['entity_types'] >]
    /** [= type['doc'] =] */
    public static final int NE_TYPE_[= type['name'] =] = [= "0x%0.4X" % id.next() =] << 16;[<
end-for >]

[< for ne_types['entity_types'] >][< if 'subtypes' in type >][%
id = iter(xrange(len(type['subtypes']))) %][<
for type['subtypes'] >]
    /** [= stype['doc'] =] */
    public static final int NE_TYPE_[= type['name'] =]_[= stype['name'] =] = NE_TYPE_[= type['name'] =] | [= "0x%0.4X" % (id.next()+1) =];[<
end-for >]
    /** Other [= type['name'].lower() =] instances */
    public static final int NE_TYPE_[= type['name'] =]_OTHER = NE_TYPE_[= type['name'] =] | 0xFFFF;[<
end-if >][<
end-for >]

[< for ne_types['entity_types'] >]
    /** Printable name for NAMED_ENTITY_[= type['name'] =] */
    public static final String NAMED_ENTITY_[= type['name'] =] = "[= type['name'] =]";[<
if 'subtypes' in type >][<
for type['subtypes'] >]
    /** Printable name for NAMED_ENTITY_[= type['name'] =]_[= stype['name'] =] */
    public static final String NAMED_ENTITY_[= type['name'] =]_[= stype['name'] =] = "[= type['name'] =]:[= stype['name'] =]";[<
end-for >]
    /** Printable name for NAMED_ENTITY_[= type['name'] =]_OTHER */
    public static final String NAMED_ENTITY_[= type['name'] =]_OTHER = "[= type['name'] =]:OTHER";[<
end-if >][<
end-for >]

    /** Internal hash for mapping numeric value to string value */
    private static Map<Integer, String> neMap;
    
    /** Map built-in type string to int value */
    private static final Map<String, Integer> neTypeMap;

    private NEConstants() {
    }    

    /**
     * Takes a named entity type constant and returns the human-readable name.
     * @param v The named entity type.
     * @return Human-readable name.
     **/
    public static String toString(int v) {
        return neMap.get(new Integer(v));
    }

    /**
     * Takes a standard human-readable named entity type name
     * and returns the integer constant.  Note that only built-in types are
     * supported.  For custom types, use RLPNENameMap.parse().
     * @param s Named entity type name.
     * @throws InvalidNamedEntityTypeNameException when an ID for the string can not be found.
     * @return  Integer constant for the given entity type.
     **/
    public static int parse(String s) throws InvalidNamedEntityTypeNameException {
        // COMN-27: Look for built-in types first to avoid the costly
        // reflection below.
        String key = s.toUpperCase(Locale.ENGLISH);
        if (neTypeMap.containsKey(key)) {
            return neTypeMap.get(key);
        }
        throw new InvalidNamedEntityTypeNameException("No type. Named entity \""
                                                      + key
                                                      + "\" is not supported.");
    }

    /**
     * Takes a source language processor id and returns its human-readable
     * name.
     * To get processor id, use {@link #extractLP(int) extractLP(int source)}. Return values
     * include "statistical", "gazetteer" (exact match), "regex" pattern match), "none".
     *
     * @param v The source language processor.
     * @return Human-readable name.
     **/
    public static String getSourceLPName(int v) {
        switch(v) {
        case NEConstants.RLP_LP_NONE:
            return "none";
        case NEConstants.RLP_LP_STAT:
            return "statistical";
        case NEConstants.RLP_LP_GAZ:
            return "gazetteer";
        case NEConstants.RLP_LP_REGEX:
            return "regex";
        }
        return "NONE";
    }

    /**
     * Takes the name of a source language processor and returns the ID.
     * Return values include NEConstants.RLP_LP_STAT, NEConstants.RLP_LP_GAZ,  
     * NEConstants.RLP_LP_REGEX, and NEConstants.RLP_LP_NONE.
     
     * @param s The name: "statistical", "gazetteer" (exact match), or "regex" (pattern match).
     * @return The source language processor ID.
     **/
    public static int getSourceLPID(String s) {
        if (s.equals("STAT") || s.equals("statistical")) {
            return NEConstants.RLP_LP_STAT;
        } else if (s.equals("GAZE") || s.equals("gazetteer")) {
            return NEConstants.RLP_LP_GAZ;
        } else if (s.equals("REGX") || s.equals("regex")) {
            return NEConstants.RLP_LP_REGEX;
        }
        return NEConstants.RLP_LP_NONE;
    }

    static {
        neMap = new TreeMap<Integer, String>();
[< for ne_types['entity_types'] >]
        neMap.put(new Integer(NEConstants.NE_TYPE_[= type['name'] =]), 
                  NEConstants.NAMED_ENTITY_[= type['name'] =]);[<
if 'subtypes' in type >][< for type['subtypes'] >]
        neMap.put(new Integer(NEConstants.NE_TYPE_[= type['name'] =]_[= stype['name'] =]),
                  NEConstants.NAMED_ENTITY_[= type['name'] =]_[= stype['name'] =]);[<
end-for >]
        neMap.put(new Integer(NEConstants.NE_TYPE_[= type['name'] =]_OTHER), 
                  NEConstants.NAMED_ENTITY_[= type['name'] =]_OTHER);[<
end-if >][<
end-for >]

        neTypeMap = new HashMap<String, Integer>();
[< for ne_types['entity_types'] >]
        neTypeMap.put(NAMED_ENTITY_[= type['name'] =], NE_TYPE_[= type['name'] =]);[<
if 'subtypes' in type >][<
for type['subtypes'] >]
        neTypeMap.put(NAMED_ENTITY_[= type['name'] =]_[= stype['name'] =], NE_TYPE_[= type['name'] =]_[= stype['name'] =]);[<
end-for >]
        neTypeMap.put(NAMED_ENTITY_[= type['name'] =]_OTHER, NE_TYPE_[= type['name'] =]_OTHER);[<
end-if >][<
end-for >]
    }

    /**
     * Returns the major type of the Named Entity Type
     * @param t Named entity type ID
     * @return Major type
     */
    public static int extractType(int t) {
        return t & 0xffff0000;
    }

    /**
     * Returns the subtype of the Named Entity Type
     * @param t Named entity type ID
     * @return Subtype
     */
    public static int extractSubtype(int t) {
        return t & 0x0000ffff;
    }

    /**
     * Returns the language processor ID of the Named Entity Source.
     * Return values include NEConstants.RLP_LP_STAT, NEConstants.RLP_LP_GAZ,  
     * NEConstants.RLP_LP_REGEX, and NEConstants.RLP_LP_NONE.
     *
     * @param s Named entity source ID
     * @return LP IP
     */
    public static int extractLP(int s) {
        return s & 0xff000000;
    }

    /**
     * Returns the subsource of the Named Entity Source.
     * If source is statistical or regex, return value is 0;
     * if source is gazetteer, return value identifies the actual gazetteer.
     * @param s Named entity source ID
     * @return Subsource
     */
    public static int extractSubsource(int s) {
        return s & 0x00ffffff;
    }

    /** unknown NE source language processor ID.*/
    public static final int RLP_LP_NONE = 0x00 << 24;
    /** Named Entity Extractor source language processor ID.*/
    public static final int RLP_LP_STAT = 0x01 << 24;
    /** Exact Match Entity Extractor  source language processor ID.*/
    public static final int RLP_LP_GAZ = 0x02 << 24;
    /** Pattern Match Entity Extractor  source language processor ID.*/
    public static final int RLP_LP_REGEX = 0x03 << 24;    
    
[< for ne_types['entity_types'] >]
    /**
      Determines if the named entity is a [= type['name'].lower() =].
      @param t Named entity type ID
      @return true if the named entity is a [= type['name'].lower() =] , false otherwise.
     */
    public static boolean is[= type['name'] =](int t) {
        return extractType(t) == NEConstants.NE_TYPE_[= type['name'].upper() =]; 
    }
[<
end-for >]
};
