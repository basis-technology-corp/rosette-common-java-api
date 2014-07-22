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
 * Options for what {@link com.basistech.rosette.internal.take5build.Take5Builder} writes.
 */
public enum OutputFormat {
    /** A standard binary file. */
    TAKE5,
    /** A textual dump of the FSA, or nothing for PERFHASH. */
    FSA,
    /** nothing at all. Useful for checking for valid data. */
    NONE
}
