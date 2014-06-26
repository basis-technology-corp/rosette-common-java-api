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

import com.basistech.rosette.internal.take5.Take5Dictionary;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.io.output.NullWriter;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Cross-create the mixed dictionary, compare at a low level.
 *
 * LE Dump
 *
 * Take5 header contents for './lib/src/test/dicts/mixed-hash-LE.bin'
 magic: T4K3
 max_version: 0x506
 content_max_version: 0
 flags: 0x5
 content_flags: 0x0
 min_version: 0x500
 content_min_version: 0
 required_alignment: 4
 total_size: 1052
 copyright_string: 0
 copyright_size: 0
 value_data: 1008
 value_format: (0x4000004)
 TKB_VALUE_FORMAT_INDIRECT(4)
 keycheck_data: 964
 keycheck_format: 2
 TKB_KEYCHECK_FORMAT_STR
 fsa_data: 148
 fsa_engine: 3
 TKB_ENGINE_PERFHASH
 fsa_limit: -1
 word_count: 10
 index_count: 11
 max_hash_fun: 15
 max_matches: 0
 max_word_length: 6
 max_value_size: 196
 min_character: 0x30
 max_character: 0xFFFF
 build_day: 749328
 2014-04-28
 build_msec: 72601000
 20:10:01.000
 metadata_string: 0
 metadata_size: 0
 entry_point_count: 1
 entry_point_header_size: 84
 entry points:
 main:
 state_start: 232
 index_start: 0
 index_offset: 0
 word_count: 10
 index_count: 11
 bucket_count: 5
 max_hash_fun: 15
 max_matches: 0
 max_word_length: 6
 max_value_size: 196
 min_character: 0x30
 max_character: 0xFFFF
 *
 * BE dump
 *
 * magic: T4K3
 maxVersion: 0x506
 content_max_version: 0
 flags: 0x4
 content_flags: 0x0
 min_version: 0x500
 content_min_version: 0x0
 required_alignment: 4
 total_size: 1052
 copyright_string: 0
 copyright_size: 0
 value_data: 1008
 value_format: (0x4000004)
 keycheck_data: 964
 keycheck_format: 2
 TKB_KEYCHECK_FORMAT_STR
 fsa_data: 148
 fsa_engine: 3
 TKB_ENGINE_PERFHASH
 fsa_limit: -1
 word_count: 10
 index_count: 11
 max_hash_fun: 15
 max_matches: 0
 max_word_length: 6
 max_value_size: 196
 min_character: 0x30
 max_character: 0xFFFF
 build_day: 749329
 2014-04-29
 build_msec: 73653000
 20:27:33.000
 entry_point_count: 1
 entry_point_header_size: 84
 entry points:
 main:
 state_state: 232
 index_start: 0
 index_offset: 0
 word_count: 10
 index_count: 11
 bucket_count: 5
 max_hash_fun: 15
 max_matches: 0
 max_word_length: 6
 max_value_size: 196
 min_character: 0x30
 max_character: 0xFFFF
 *
 */
public class BruteForceCrossBuildTest extends Assert {

    @Test
    public void buildAndCompare() throws Exception {
        Assume.assumeThat(ByteOrder.nativeOrder(), is(equalTo(ByteOrder.LITTLE_ENDIAN)));
        File t5File = File.createTempFile("t5btest.", ".bin");
        t5File.deleteOnExit();
        File inputFile = new File("src/test/data/mixed.txt");
        Take5Build cmd = new Take5Build();
        cmd.alignment = 4;
        cmd.byteOrder = ByteOrder.BIG_ENDIAN;
        cmd.simpleKeys = true;
        cmd.engine = Engine.PERFHASH;
        cmd.outputFile = t5File;
        cmd.keyFormat = KeyFormat.HASH_STRING;
        cmd.commandInputFile = inputFile;
        cmd.checkOptionConsistency();
        cmd.build();

        ByteBuffer resultT5 = Files.map(t5File);
        Take5Dictionary dict = new Take5Dictionary(resultT5, resultT5.capacity());





    }
}
