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
package com.basistech.util;

/**
 * Exception class when a Basis Technology runtime component cannot locate the necessary data or library files
 * at runtime. This results when an application fails to set the bt.root system property or call
 * com.basistech.util.Pathnames.setBTRootDirectory.
 */
public class NoRootDirectoryException extends RuntimeException {

    /**
     * Create an exception with a default message.
     */
    public NoRootDirectoryException() {
        super(
              "No Basis product root directory. Set the bt.root system property or call "
              + "com.basistech.util.Pathnames.setBTRootDirectory.");
    }

}
