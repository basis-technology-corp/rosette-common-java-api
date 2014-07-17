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
import com.basistech.rosette.internal.take5.Take5Exception;
import com.basistech.rosette.internal.take5.Take5Match;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * test the convenience feature.
 */
public class MapTake5PairSourceTest extends Assert {

    @Test
    public void testMappedBuild() throws Exception {
        Map<String, CharSequence> inputData = Maps.newHashMap();
        inputData.put("duck", "quack");
        inputData.put("kine", "low");
        inputData.put("frog", "croak");
        MapTake5PairSource source = new MapTake5PairSource(inputData);

        Take5Builder builder = new Take5BuilderFactory().valueFormat(ValueFormat.PTR)
                .engine(Engine.PERFHASH)
                .keyFormat(KeyFormat.HASH_STRING)
                .build();

        Take5EntryPoint ep = builder.newEntryPoint("main");
        ep.loadContent(source.iterator());
        ByteBuffer t5 = builder.buildBuffer();

        Take5Dictionary dict = new Take5Dictionary(t5, t5.capacity());
        Take5Match match = dict.matchExact("duck");
        assertNotNull(match);
        assertEquals("quack", getStringResult(dict, match));
        match = dict.matchExact("kine");
        assertEquals("low", getStringResult(dict, match));
        match = dict.matchExact("frog");
        assertEquals("croak", getStringResult(dict, match));
    }

    private String getStringResult(Take5Dictionary dict, Take5Match match) throws Take5Exception {
        int offset = match.getOffsetValue();
        ByteBuffer data = dict.getData();
        data.position(offset);
        char c;
        StringBuilder sb = new StringBuilder();
        while ((c = data.getChar()) != 0) {
            sb.append(c);
        }
        return sb.toString();
    }
}
