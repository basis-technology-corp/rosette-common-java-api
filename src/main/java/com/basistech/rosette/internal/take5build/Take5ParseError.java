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

package com.basistech.rosette.internal.take5build;

/**
 * A runtime exception for things that can go wrong while parsing Take5
 * input files.
 */
public class Take5ParseError extends Take5BuilderException {
    String message;             // message without location
    String inputName;           // file name <EM>or</EM> entry point name.
    int keyNumber;

    public Take5ParseError(String message, String inputName, int keyNumber) {
        super(message + " at key number " + keyNumber + " in " + inputName);
        this.message = message;
        this.inputName = inputName;
        this.keyNumber = keyNumber;
    }
}
