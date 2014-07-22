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
* What payloads are stored in the binary file.
*/
public enum ValueFormat {
    /** No payloads. */
    IGNORE,                        /* -i: keys ignored. The result is a 'Set' in java terms. */
    /** For each key, map it to its ordinal position in the key set. */
    INDEX,                         /* -x: each key maps to its index in the sorted key set. */
    /** Store arbitrary data payloads. */
    PTR                            /* -p: each key maps to a payload. */
}
