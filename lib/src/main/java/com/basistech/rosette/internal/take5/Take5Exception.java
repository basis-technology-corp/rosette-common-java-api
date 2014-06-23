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

package com.basistech.rosette.internal.take5;

/**
 * Exception that can occur within Take5. Most of these are caused by problems reading the binary format.
 * Check the value of getType() to get the specific type of error that has occured.
 */
public class Take5Exception extends Exception {
    public static final int FILE_TOO_SHORT = 1;
    public static final int BAD_DATA = 2;
    public static final int WRONG_BYTE_ORDER = 3;
    public static final int FILE_TOO_NEW = 4;
    public static final int FILE_TOO_OLD = 5;
    public static final int UNSUPPORTED_ENGINE = 6;
    public static final int UNSUPPORTED_VALUE_FORMAT = 7;
    public static final int VALUE_SIZE_MISMATCH = 8;
    public static final int NO_NUMBERS_HERE = 9;
    public static final int NO_POINTERS_HERE = 10;
    public static final int UNSUPPORTED_STATE_TYPE = 11;
    public static final int ENTRY_POINT_NOT_FOUND = 12;
    public static final int IMPOSSIBLE_HASH = 13;
    public static final int BUFFER_TOO_SMALL = 14;
    public static final int UNSUPPORTED_KEYCHECK_FORMAT = 15;

    private int type;

    Take5Exception(int type) {
        super();
        this.type = type;
    }
    
    Take5Exception(int type, String additionalMessage) {
        super(additionalMessage);
        this.type = type;
    }
    
    public static String getTypeString(int type) {
        String message;
        switch (type) {
        case FILE_TOO_SHORT:
            message = "File too short.";
            break;
        case BAD_DATA:
            message = "Bad data.";
            break;
        case WRONG_BYTE_ORDER:
            message = "Wrong byte order.";
            break;
        case FILE_TOO_NEW:
            message = "File is too new.";
            break;
        case FILE_TOO_OLD:
            message = "File is too old.";
            break;
        case UNSUPPORTED_ENGINE:
            message = "Unsupported engine.";
            break;
        case UNSUPPORTED_VALUE_FORMAT:
            message = "Unsupported value format.";
            break;
        case VALUE_SIZE_MISMATCH:
            message = "Value size mismatch.";
            break;
        case NO_NUMBERS_HERE:
            message = "No numbers here.";
            break;
        case NO_POINTERS_HERE:
            message = "No pointers here.";
            break;
        case UNSUPPORTED_STATE_TYPE:
            message = "Unsupported state type.";
            break;
        case ENTRY_POINT_NOT_FOUND:
            message = "Entry point not found.";
            break;
        case IMPOSSIBLE_HASH:
            message = "Impossible hash.";
            break;
        case BUFFER_TOO_SMALL:
            message = "Buffer too small.";
            break;
        case UNSUPPORTED_KEYCHECK_FORMAT:
            message = "Unsupported key check format.";
            break;
        default:
            message = "Unknown error type.";
        }
        return message;
    }

    @Override
    public String getMessage() {
        String message = Take5Exception.getTypeString(type);
        String additional = super.getMessage();
        if (additional != null) {
            return message + " " + super.getMessage();
        } else {
            return message;
        }
    }

    public int getType() {
        return type;
    }
}
