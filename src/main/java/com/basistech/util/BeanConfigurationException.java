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
 * This exception is thrown when one of the Basis 
 * classes is configured in Spring (or some related environment) but a required
 * property is not set.
 */
public class BeanConfigurationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2611977288757897494L;

    /**
     * Default constructor.
     */
    public BeanConfigurationException() {
    }

    /**
     * Exception with an error message.
     * 
     * @param message the message.
     */
    public BeanConfigurationException(String message) {
        super(message);
    }

    /**
     * Exception wrapping another exception.
     * 
     * @param cause the other exception.
     */
    public BeanConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with an error message and an underlying other exception.
     * 
     * @param message the message.
     * @param cause the other exception.
     */
    public BeanConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
