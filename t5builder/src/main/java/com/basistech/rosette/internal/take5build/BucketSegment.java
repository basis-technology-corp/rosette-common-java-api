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

import java.nio.IntBuffer;

import static com.basistech.rosette.internal.take5build.Take5Format.EPT_ACCEPT_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_ACCEPT_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_BUCKET_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_FLAGS;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_MAX_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_MIN_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_INDEX_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_INDEX_OFFSET;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_INDEX_START;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_MATCHES;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_VALUE_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_WORD_LENGTH;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MIN_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_STATE_START;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_WORD_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_HASH_FUN;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MILLIONS_TESTED;

/**
 * The hash table as a segment to write.
 * This also sets up the entrypoints.
 */
class BucketSegment extends SimpleSegment {

    BucketSegment(Take5Builder builder, String description, SimpleSegment entrySeg) {
        super(builder, description, 4 * builder.globalBucketCount, 4);

        int bucketBase = getAddress();
        IntBuffer entry = entrySeg.getIntBuffer();

        int entryIndex = 0;
        int ix = 0;

        IntBuffer buffer = getIntBuffer();
        int offset = 0;

        for (Take5EntryPoint ep : builder.entryPoints) {
            entry.put(entryIndex + EPT_STATE_START, bucketBase);
            entry.put(entryIndex + EPT_INDEX_START, ix);
            entry.put(entryIndex + EPT_INDEX_OFFSET, ix);
            int bucketCount = ep.bucketCount;
            for (int bx = 0; bx < bucketCount; bx++) {
                buffer.put(offset + ep.bucketTable[bx].index, ep.bucketTable[bx].fun);
            }

            ix += ep.indexCount;
            offset += bucketCount;
            bucketBase += bucketCount * 4;

            // Common header:
            entry.put(entryIndex + EPT_CONTENT_FLAGS, ep.contentFlags);
            entry.put(entryIndex + EPT_CONTENT_MIN_VERSION, ep.contentMinVersion);
            entry.put(entryIndex + EPT_CONTENT_MAX_VERSION, ep.contentMaxVersion);
            entry.put(entryIndex + EPT_WORD_COUNT, ep.keyCount);
            entry.put(entryIndex + EPT_STATE_COUNT, ep.stateCount);
            entry.put(entryIndex + EPT_ACCEPT_STATE_COUNT, 0); // It's a Mealy machine...
            entry.put(entryIndex + EPT_EDGE_COUNT, ep.edgeCount);
            entry.put(entryIndex + EPT_ACCEPT_EDGE_COUNT, ep.acceptEdgeCount);
            entry.put(entryIndex + EPT_MAX_MATCHES, ep.maxMatches);
            entry.put(entryIndex + EPT_MAX_WORD_LENGTH, ep.maxKeyLength);
            entry.put(entryIndex + EPT_MAX_VALUE_SIZE, ep.maxValueSize);
            entry.put(entryIndex + EPT_MIN_CHARACTER, ep.minCharacter);
            entry.put(entryIndex + EPT_MAX_CHARACTER, ep.maxCharacter);
            entry.put(entryIndex + EPT_INDEX_COUNT, ep.indexCount);
            entry.put(entryIndex + EPT_BUCKET_COUNT, ep.bucketCount);
            entry.put(entryIndex + EPT_MAX_HASH_FUN, ep.maxHashFun);
            entry.put(entryIndex + EPT_MILLIONS_TESTED, ep.millionsTested);

            entryIndex += Take5Builder.EPTLEN;
        }
        assert ix == builder.globalIndexCount;
        assert offset == builder.globalBucketCount;

    }
}
