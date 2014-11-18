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

/*
 * Spring requires that value editors be in the same package as the classes they handle.
 * We want the enums in com.basistech.util in the common-api jar so that OSGi works,
 * so we have the editors here, which means that they aren't usable in a combination
 * of Spring and OSGi. So be it.
 */


package com.basistech.util;