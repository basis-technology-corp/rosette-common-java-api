/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2014 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.t5build;

import java.io.File;

/**
 * Input information from a control file or the command line.
 */
class InputSpecification {
    // derived from ESCAPES
    boolean simpleKeys = true;
    boolean ignorePayloads;
    boolean noPayloads;
    int contentFlags;
    int minVersion = -1;
    int maxVersion = -1;
    String entrypointName;
    File inputFile;
    String defaultMode;
}
