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

package com.basistech.rosette.internal.take5build;

/**
 * Definitions for Take5 binary file format.  This is the Java version of
 * the <CODE>binary.h</CODE> header from the C implementation.  Both files
 * must be updated in parallel.
 * <P>
 * Currently the Java Take5 runtime isn't using this file.  It probably
 * should.  Which may mean that this class belongs in its own package, like
 * maybe <CODE>com.basistech.rosette.internal.take5common</CODE>?
 * <P>
 * Note that even in the Java builder, there are a lot of constant values
 * that are integrated into the code that could be symbolic.
 */
public final class Take5Format {

    /*
     * Known versions.
     */
    public static final int VERSION_5_0 = 0x500;
    public static final int VERSION_5_1 = 0x501;
    public static final int VERSION_5_2 = 0x502;
    public static final int VERSION_5_3 = 0x503;
    public static final int VERSION_5_4 = 0x504;
    public static final int VERSION_5_5 = 0x505;
    public static final int VERSION_5_6 = 0x506;
    public static final int VERSION_MIN = VERSION_5_0;

    /*
     * The magic word.
     */
    public static final int MAGIC_T4K3 = 'T' << 24 | '4' << 16 | 'K' << 8 | '3';

    /*
     * Flags in header.
     */
    public static final int FLAG_LITTLE_ENDIAN = 0x01;
    public static final int FLAG_OBFUSCATION = 0x02;
    public static final int FLAG_LOOKUP_AUTOMATON = 0x04;

    /*
     * File header.
     */
    // Original 5.0 fields:
    public static final int HDR_MAGIC = 0;       // 'T', '4', 'K', '3'
    public static final int HDR_MAX_VERSION = 1; // in network byte order
    public static final int HDR_CONTENT_MAX_VERSION = 2;
    public static final int HDR_FLAGS = 3;       // in network byte order
    public static final int HDR_CONTENT_FLAGS = 4;
    public static final int HDR_MIN_VERSION = 5; // in network byte order
    public static final int HDR_CONTENT_MIN_VERSION = 6;
    // End of 'T4K3' compatibility
    public static final int HDR_REQUIRED_ALIGNMENT = 7;
    public static final int HDR_TOTAL_SIZE = 8;
    public static final int HDR_COPYRIGHT_STRING = 9;
    public static final int HDR_COPYRIGHT_SIZE = 10;
    public static final int HDR_VALUE_DATA = 11;
    public static final int HDR_VALUE_FORMAT = 12;
    public static final int HDR_FSA_DATA = 13;
    public static final int HDR_FSA_ENGINE = 14;
    public static final int HDR_WORD_COUNT = 15;
    public static final int HDR_STATE_COUNT = 16;
    public static final int HDR_ACCEPT_STATE_COUNT = 17;
    public static final int HDR_EDGE_COUNT = 18;
    public static final int HDR_ACCEPT_EDGE_COUNT = 19;
    public static final int HDRLEN_VERSION_5_0 = 20;
    // New in 5.1:
    public static final int HDR_MAX_MATCHES = 20;
    public static final int HDRLEN_VERSION_5_1 = 21;
    // New in 5.2:
    public static final int HDR_MAX_WORD_LENGTH = 21;
    public static final int HDR_MAX_VALUE_SIZE = 22;
    public static final int HDR_MIN_CHARACTER = 23;
    public static final int HDR_MAX_CHARACTER = 24;
    public static final int HDR_BUILD_DAY = 25;
    public static final int HDR_BUILD_MSEC = 26;
    public static final int HDRLEN_VERSION_5_2 = 27;
    // New in 5.3:
    public static final int HDR_METADATA_STRING = 27;
    public static final int HDR_METADATA_SIZE = 28;
    public static final int HDRLEN_VERSION_5_3 = 29;
    // New in 5.4:
    public static final int HDR_ENTRY_POINT_COUNT = 29;
    public static final int HDR_ENTRY_POINT_HEADER_SIZE = 30;
    public static final int HDRLEN_VERSION_5_4 = 31;
    public static final int HDRLEN_VERSION_5_5 = 31;
    // New in 5.6:
    public static final int HDR_INDEX_COUNT = 31;
    public static final int HDR_KEYCHECK_DATA = 32;
    public static final int HDR_KEYCHECK_FORMAT = 33;
    public static final int HDR_MAX_HASH_FUN = 34;
    public static final int HDR_MILLIONS_TESTED = 35;
    public static final int HDR_FSA_LIMIT = 36;
    public static final int HDRLEN_VERSION_5_6 = 37;

    /*
     * Entry point header.
     */
    // Original 5.0 fields:
    public static final int EPT_STATE_START = 0;
    public static final int EPT_INDEX_START = 1;
    public static final int EPTLEN_VERSION_5_0 = 2;
    public static final int EPTLEN_VERSION_5_1 = 2;
    public static final int EPTLEN_VERSION_5_2 = 2;
    public static final int EPTLEN_VERSION_5_3 = 2;
    // New in 5.4:
    public static final int EPT_INDEX_OFFSET = 2;
    public static final int EPT_NAME = 3;
    public static final int EPT_CONTENT_FLAGS = 4;
    public static final int EPT_CONTENT_MAX_VERSION = 5;
    public static final int EPT_CONTENT_MIN_VERSION = 6;
    public static final int EPT_WORD_COUNT = 7;
    public static final int EPT_STATE_COUNT = 8;
    public static final int EPT_ACCEPT_STATE_COUNT = 9;
    public static final int EPT_EDGE_COUNT = 10;
    public static final int EPT_ACCEPT_EDGE_COUNT = 11;
    public static final int EPT_MAX_MATCHES = 12;
    public static final int EPT_MAX_WORD_LENGTH = 13;
    public static final int EPT_MAX_VALUE_SIZE = 14;
    public static final int EPT_MIN_CHARACTER = 15;
    public static final int EPT_MAX_CHARACTER = 16;


    public static final int EPTLEN_VERSION_5_4 = 17;
    public static final int EPTLEN_VERSION_5_5 = 17;
    // New in 5.6:

    public static final int EPT_INDEX_COUNT = 17;
    public static final int EPT_BUCKET_COUNT = 18;
    public static final int EPT_MAX_HASH_FUN = 19;
    public static final int EPT_MILLIONS_TESTED = 20;
  /* TKB_VERSION_5_6 header ends here */

    public static final int EPTLEN_VERSION_5_6 = 21;

    /*
     * Value format codes.
     */
    public static final int VALUE_FORMAT_NONE =     0x01000000;
    public static final int VALUE_FORMAT_INDEX =    0x02000000;
    public static final int VALUE_FORMAT_FIXED =    0x03000000;
    public static final int VALUE_FORMAT_INDIRECT = 0x04000000;

    /*
     * Runtime engines.
     */
    public static final int ENGINE_TAKE3 = 1;
    public static final int ENGINE_SEARCH = 2;
    public static final int ENGINE_PERFHASH = 3;

    /*
     * Search type opcodes.
     */
    public static final int SEARCH_TYPE_MAX_PARAMETER = 16;
    public static final int SEARCH_TYPE_BINARY_0 = 0;   //  0 - 16
    public static final int SEARCH_TYPE_LINEAR_0 = 17;  // 17 - 33
    public static final int SEARCH_TYPE_CHOICE_0 = 34;  // 34 - 50
    // 51 - 67 is the deprecated SEARCH_TYPE_DISPATCH range
    public static final int SEARCH_TYPE_LINEAR_MANY = 68;

    public static final int KEYCHECK_FORMAT_NONE = 0;
    public static final int KEYCHECK_FORMAT_HASH32 = 1;
    public static final int KEYCHECK_FORMAT_STR = 2;

    private Take5Format() {
    }
}
