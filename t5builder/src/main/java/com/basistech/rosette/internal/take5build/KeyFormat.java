/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010-2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

/**
 * With {@link com.basistech.rosette.internal.take5build.Engine#PERFHASH}, what sort of keys to write.
 */
public enum KeyFormat {
    /**
     * For the FSA search engine.
     */
    FSA,                             /* -t fsa or -5 */
    /**
     * Just accept hash collisions.
     */
    HASH_NONE,                       /* -t hash/none */
    /**
     * Store a hash value to get very few hash collisions.
     */
    HASH_HASH32,                     /* -t hash/hash32 */
    /**
     * Store the key to get no collections.
     */
    HASH_STRING                      /* -t hash/str */
}
