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

package com.basistech.rosette.internal.take5;

import com.basistech.rosette.internal.take5build.ByteOrderOptionHandler;
import com.basistech.rosette.internal.take5build.Take5Format;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 * CLI to dump facts about a Take5.
 * This depends on package access into Take5Dictionary.
 * Well, maybe. It may just decide to do all its work by dumping the hard way.
 */
public final class Take5DumpHeader {
    @Argument(metaVar = "INPUT", required = true, usage = "Input Take5 file")
    File inputFile;

    @Option(name = "-buckets", usage = "dump perfhash buckets")
    boolean buckets;

    @Option(name = "-byteOrder", aliases = {"-byte-order" }, usage = "byte order (LE or BE)", metaVar = "ORDER", handler = ByteOrderOptionHandler.class)
    ByteOrder byteOrder = ByteOrder.nativeOrder();

    private ByteBuffer buffer;
    private IntBuffer ints;
    private PrintWriter pw;
    private IntList bucketCounts = new IntArrayList();
    private IntList stateStarts = new IntArrayList();

    private List<String> entrypointNames = Lists.newArrayList();
    private int valueData;
    private int engine;

    Take5DumpHeader() {
        //
    }

    public static void main(String[] args) {
        Take5DumpHeader that = new Take5DumpHeader();
        CmdLineParser parser = new CmdLineParser(that);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        that.dump();
    }

    private void dump() {
        ByteBuffer t5Buffer;
        try {
            t5Buffer = Files.map(inputFile);
        } catch (IOException e) {
            System.err.println(String.format("Failed to open %s: %s", inputFile.getAbsolutePath(), e.getMessage()));
            System.exit(1);
            return;
        }

        if (byteOrder != ByteOrder.nativeOrder()) {
            t5Buffer.order(byteOrder);
        }

        pw = new PrintWriter(new OutputStreamWriter(System.out, Charsets.UTF_8));
        buffer = t5Buffer;
        ints = buffer.asIntBuffer();
        dumpDictionary();
        if (buckets) {
            dumpBuckets();
        }
        pw.flush();
    }

    private void dumpBuckets() {
        if (engine == Take5Format.ENGINE_PERFHASH) {
            pw.format("\nBuckets:\n");
            // buckets start at valueData
            for (int epx = 0; epx < entrypointNames.size(); epx++) {
                pw.format("%s:\n", entrypointNames.get(epx));
                int bucketOffset = stateStarts.get(epx);
                bucketOffset /= 4; // it starts as a byte offset, we'll use it as an int offset.
                int bucketCount = bucketCounts.get(epx);
                for (int x = 0; x < bucketCount; x++) {
                    pw.format(" 0x%08X\n", ints.get(bucketOffset + x));
                }
                pw.format("\n");
            }
        }
    }

    void dumpDictionary() {

        byte[] magic = new byte[4];
        buffer.position(0);
        buffer.get(magic);
        pw.format("magic: %c%c%c%c\n", (char)magic[0], (char)magic[1], (char)magic[2], (char)magic[3]);
        buffer.position(0);
        int maxVersion = getNetworkInt(Take5Format.HDR_MAX_VERSION);
        pw.format("maxVersion: 0x%X\n", maxVersion);
        pw.format("content_max_version: %d\n", ints.get(Take5Format.HDR_CONTENT_MAX_VERSION));
        pw.format("flags: 0x%X\n", getNetworkInt(Take5Format.HDR_FLAGS));
        pw.format("content_flags: 0x%X\n", ints.get(Take5Format.HDR_CONTENT_FLAGS));
        pw.format("min_version: 0x%X\n", getNetworkInt(Take5Format.HDR_MIN_VERSION));
        pw.format("content_min_version: 0x%X\n", ints.get(Take5Format.HDR_CONTENT_MIN_VERSION));
        pw.format("required_alignment: %d\n", ints.get(Take5Format.HDR_REQUIRED_ALIGNMENT));
        pw.format("total_size: %d\n", ints.get(Take5Format.HDR_TOTAL_SIZE));
        pw.format("copyright_string: %d\n", ints.get(Take5Format.HDR_COPYRIGHT_STRING));
        pw.format("copyright_size: %d\n", ints.get(Take5Format.HDR_COPYRIGHT_SIZE));
        valueData = ints.get(Take5Format.HDR_VALUE_DATA);
        pw.format("value_data: %d\n", valueData);

        dumpValueFormat();
        dumpKeyFormat(maxVersion);

        pw.format("fsa_data: %d\n", ints.get(Take5Format.HDR_FSA_DATA));

        engine = ints.get(Take5Format.HDR_FSA_ENGINE);
        dumpEngine(engine);

        if (maxVersion >= Take5Format.VERSION_5_6) {
            pw.format("fsa_limit: %d\n", ints.get(Take5Format.HDR_FSA_LIMIT));
        }

        pw.format("word_count: %d\n", ints.get(Take5Format.HDR_WORD_COUNT));

        if (maxVersion >= Take5Format.VERSION_5_6) {
            pw.format("index_count: %d\n", ints.get(Take5Format.HDR_INDEX_COUNT));
            final int maxHashFun = ints.get(Take5Format.HDR_MAX_HASH_FUN);
            if (maxHashFun != 0) {
                pw.format("max_hash_fun: %d\n", maxHashFun);
            }
            final int millionsTested = ints.get(Take5Format.HDR_MILLIONS_TESTED);
            if (millionsTested != 0) {
                pw.format("millions_tested: %d\n", millionsTested);
            }
        }

        int stateCount = ints.get(Take5Format.HDR_STATE_COUNT);
        if (stateCount != 0) {
            pw.format("state_count: %d\n", stateCount);
        }

        int acceptStateCount = ints.get(Take5Format.HDR_ACCEPT_STATE_COUNT);
        if (acceptStateCount != 0) {
            pw.format("accept_state_count: %d\n", acceptStateCount);
        }

        int edgeCount = ints.get(Take5Format.HDR_EDGE_COUNT);
        if (edgeCount != 0) {
            pw.format("edge_count: %d\n", edgeCount);
        }

        int acceptEdgeCount = ints.get(Take5Format.HDR_ACCEPT_EDGE_COUNT);
        if (acceptEdgeCount != 0) {
            pw.format("accept_edge_count: %d\n", acceptEdgeCount);
        }

        if (maxVersion >= Take5Format.VERSION_5_1) {
            pw.format("max_matches: %d\n", ints.get(Take5Format.HDR_MAX_MATCHES));
        }

        dump52(maxVersion);

        dump53(maxVersion);

        dump54(maxVersion);
    }

    private void dump54(int maxVersion) {
        if (maxVersion >= Take5Format.VERSION_5_4) {
            int epCount = ints.get(Take5Format.HDR_ENTRY_POINT_COUNT);
            int epHeaderSize = ints.get(Take5Format.HDR_ENTRY_POINT_HEADER_SIZE);
            pw.format("entry_point_count: %d\nentry_point_header_size: %d\n", epCount, epHeaderSize);
            pw.format("entry points:\n");
            for (int x = 0; x < epCount; x++) {
                dumpOneEntrypoint(x, maxVersion);
            }
        }
    }

    private void dump53(int maxVersion) {
        if (maxVersion >= Take5Format.VERSION_5_3) {
            int mdString = ints.get(Take5Format.HDR_METADATA_STRING);
            int mdSize = ints.get(Take5Format.HDR_METADATA_SIZE);
            if (mdString != 0) {
                pw.format("\n");
                pw.format("meta data:\n");
                ByteBuffer metadataSubset = buffer.duplicate();
                metadataSubset.position(mdString);
                metadataSubset.limit(mdString + 2 * mdSize);
                metadataSubset.order(ByteOrder.nativeOrder());
                CharBuffer charified = metadataSubset.asCharBuffer();
                // this has embedded nulls, now we get to parse them out.
                while (charified.hasRemaining()) {
                    StringBuilder builder = new StringBuilder();
                    char c;
                    while (0 != (c = charified.get())) {
                        builder.append(c);
                    }
                    String key = builder.toString();
                    builder = new StringBuilder();
                    while (0 != (c = charified.get())) {
                        builder.append(c);
                    }
                    String value = builder.toString();
                    pw.format("name[%d]: %s\n", key.length(), key);
                    pw.format("value[%d]: %s\n", value.length(), value);
                }
            }
        }
    }

    private void dump52(int maxVersion) {
        if (maxVersion >= Take5Format.VERSION_5_2) {
            pw.format("max_word_length: %d\n", ints.get(Take5Format.HDR_MAX_WORD_LENGTH));
            pw.format("max_value_size: %d\n", ints.get(Take5Format.HDR_MAX_VALUE_SIZE));
            pw.format("min_character: 0x%X\n", ints.get(Take5Format.HDR_MIN_CHARACTER));
            pw.format("max_character: 0x%X\n", ints.get(Take5Format.HDR_MAX_CHARACTER));
            final int buildDay = ints.get(Take5Format.HDR_BUILD_DAY);
            pw.format("build_day: %d\n", buildDay);

            int t1 = buildDay;
            int day = (t1 % 31) + 1;
            t1 /= 31;
            pw.format("  %04d-%02d-%02d\n", t1 / 12, (t1 % 12) + 1, day);
            final int buildMsec = ints.get(Take5Format.HDR_BUILD_MSEC);
            pw.format("build_msec: %d\n", buildMsec);
            int t2 = buildMsec;
            int msecs = t2 % 1000;
            t2 /= 1000;
            int secs = t2 % 60;
            if (t2 >= 86400) {
                secs = t2 - (23 * 60 + 59) * 60;
                t2 = 23 * 60 + 59;
            } else {
                t2 /= 60;
            }
            pw.format("  %02d:%02d:%02d.%03d\n", t2 / 60, t2 % 60, secs, msecs);
        }
    }

    private void dumpEngine(int engine) {
        pw.format("fsa_engine: %d\n", engine);
        switch (engine) {
        case Take5Format.ENGINE_TAKE3:
            pw.format("  TKB_ENGINE_TAKE3\n");
            break;
        case Take5Format.ENGINE_SEARCH:
            pw.format("  TKB_ENGINE_SEARCH\n");
            break;
        case Take5Format.ENGINE_PERFHASH:
            pw.format("  TKB_ENGINE_PERFHASH\n");
            break;
        default:
            break;
        }
    }

    private void dumpKeyFormat(int maxVersion) {
        if (maxVersion >= Take5Format.VERSION_5_6) {
            pw.format("keycheck_data: %d\n", ints.get(Take5Format.HDR_KEYCHECK_DATA));
            final int keycheckFormat = ints.get(Take5Format.HDR_KEYCHECK_FORMAT);
            pw.format("keycheck_format: %d\n", keycheckFormat);
            switch (keycheckFormat) {
            case Take5Format.KEYCHECK_FORMAT_NONE:
                pw.format("  TKB_KEYCHECK_FORMAT_NONE\n");
                break;
            case Take5Format.KEYCHECK_FORMAT_HASH32:
                pw.format("  TKB_KEYCHECK_FORMAT_HASH32\n");
                break;
            case Take5Format.KEYCHECK_FORMAT_STR:
                pw.format("  TKB_KEYCHECK_FORMAT_STR\n");
                break;
            default:
                break;
            }
        }
    }

    private void dumpValueFormat() {
        pw.format("value_format: (0x%X)\n", ints.get(Take5Format.HDR_VALUE_FORMAT));
        int format = ints.get(Take5Format.HDR_VALUE_FORMAT);
        switch (format & 0xf0000000) {
        case Take5Format.VALUE_FORMAT_NONE:
            pw.format("  TKB_VALUE_FORMAT_NONE\n");
            break;
        case Take5Format.VALUE_FORMAT_FIXED:
            pw.format("  TKB_VALUE_FORMAT_FIXED(%d)", format & 0x00ffffff);
            break;
        case Take5Format.VALUE_FORMAT_INDIRECT:
            pw.format("  TKB_VALUE_FORMAT_INDIRECT(%d)", format & 0x00ffffff);
            break;
        case Take5Format.VALUE_FORMAT_INDEX:
            pw.format("  TKB_VALUE_FORMAT_INDEX\n");
            break;
        default:
            break;
        }
    }

    private int getNetworkInt(int intOffset) {
        ByteOrder tempOrder = buffer.order();
        try {
            buffer.order(ByteOrder.BIG_ENDIAN); // network is BE.
            return buffer.getInt(intOffset * 4);
        } finally {
            buffer.order(tempOrder);
        }
    }

    private void dumpOneEntrypoint(int epx, int maxVersion) {
        int fsaData = ints.get(Take5Format.HDR_FSA_DATA);
        int epHeaderSize = ints.get(Take5Format.HDR_ENTRY_POINT_HEADER_SIZE);
        int epBase = fsaData + (epx * epHeaderSize);
        int beg = buffer.getInt(epBase + (Take5Format.EPT_NAME * 4));
        int end = beg;
        while (buffer.get(end) != 0) {
            end++;
        }
        int len = end - beg;
        byte[] buf = new byte[len];
        for (int j = 0; j < len; j++) {
            buf[j] = buffer.get(beg + j);
        }
        final String epName = new String(buf, Charsets.UTF_8);
        entrypointNames.add(epName);
        pw.format("  %s:\n", epName);
        int contentFlags = buffer.getInt(epBase + (Take5Format.EPT_CONTENT_FLAGS * 4));
        if (contentFlags != 0) {
            pw.format("  content_flags: 0x%X\n", contentFlags);
        }

        int contentMaxVersion = buffer.getInt(epBase + (Take5Format.EPT_CONTENT_MAX_VERSION * 4));
        if (contentMaxVersion != 0) {
            pw.format("  content_flags: %d\n", contentMaxVersion);
        }


        int contentMinVersion = buffer.getInt(epBase + (Take5Format.EPT_CONTENT_MIN_VERSION * 4));
        if (contentMinVersion != 0) {
            pw.format("  content_min_version: %d\n", contentMinVersion);
        }

        final int stateStart = buffer.getInt(epBase + (Take5Format.EPT_STATE_START * 4));
        pw.format("  state_state: %d\n", stateStart);
        stateStarts.add(stateStart);
        pw.format("  index_start: %d\n", buffer.getInt(epBase + (Take5Format.EPT_INDEX_START * 4)));
        pw.format("  index_offset: %d\n", buffer.getInt(epBase + (Take5Format.EPT_INDEX_OFFSET * 4)));

        pw.format("  word_count: %d\n", buffer.getInt(epBase + (Take5Format.EPT_WORD_COUNT * 4)));

        dumpEntrypoint56(maxVersion, epBase);

        int stateCount = buffer.getInt(epBase + (Take5Format.EPT_STATE_COUNT * 4));
        if (stateCount != 0) {
            pw.format("  state_count: %d\n", stateCount);
        }

        int acceptStateCount = buffer.getInt(epBase + (Take5Format.EPT_ACCEPT_STATE_COUNT * 4));
        if (acceptStateCount != 0) {
            pw.format("  accept_state_count: %d\n", acceptStateCount);
        }

        int edgeCount = buffer.getInt(epBase + (Take5Format.EPT_EDGE_COUNT * 4));
        if (edgeCount != 0) {
            pw.format("  edge_count: %d\n", edgeCount);
        }

        int acceptEdgeCount = buffer.getInt(epBase + (Take5Format.EPT_ACCEPT_EDGE_COUNT * 4));
        if (acceptEdgeCount != 0) {
            pw.format("  accept_edge_count: %d\n", acceptEdgeCount);
        }

        pw.format("  max_matches: %d\n", buffer.getInt(epBase + (Take5Format.EPT_MAX_MATCHES * 4)));
        pw.format("  max_word_length: %d\n", buffer.getInt(epBase + (Take5Format.EPT_MAX_WORD_LENGTH * 4)));
        pw.format("  max_value_size: %d\n", buffer.getInt(epBase + (Take5Format.EPT_MAX_VALUE_SIZE * 4)));
        pw.format("  min_character: 0x%X\n", buffer.getInt(epBase + (Take5Format.EPT_MIN_CHARACTER * 4)));
        pw.format("  max_character: 0x%X\n", buffer.getInt(epBase + (Take5Format.EPT_MAX_CHARACTER * 4)));
    }

    private void dumpEntrypoint56(int maxVersion, int epBase) {
        if (maxVersion >= Take5Format.VERSION_5_6) {
            pw.format("  index_count: %d\n", buffer.getInt(epBase + (Take5Format.EPT_INDEX_COUNT * 4)));
            int bucketCount = buffer.getInt(epBase + (Take5Format.EPT_BUCKET_COUNT * 4));
            bucketCounts.add(bucketCount);
            if (bucketCount != 0) {
                pw.format("  bucket_count: %d\n", bucketCount);
            }
            int maxHash = buffer.getInt(epBase + (Take5Format.EPT_MAX_HASH_FUN * 4));
            if (maxHash != 0) {
                pw.format("  max_hash_fun: %d\n", maxHash);
            }
            int millionsTested = buffer.getInt(epBase + (Take5Format.EPT_MILLIONS_TESTED * 4));
            if (millionsTested != 0) {
                pw.format("  millions_tested: %d\n", millionsTested);
            }
        }
    }
}
