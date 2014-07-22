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

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * Expose a map as an iteration of Take5Pairs.
 */
public class MapTake5PairSource implements Iterable<Take5Pair> {
    private final Map<String, CharSequence> data;
    private final Take5Builder builder;

    public MapTake5PairSource(Take5Builder builder, Map<String, CharSequence> inputData) {
        this.builder = builder;
        if (inputData instanceof SortedMap) {
            data = inputData;
        } else {
            data = Maps.newTreeMap();
            data.putAll(inputData);
        }
    }

    public Iterable<Take5Pair> pairs() {
        return Iterables.transform(data.entrySet(), new Function<Map.Entry<String, CharSequence>, Take5Pair>() {
            @Override
            public Take5Pair apply(final Map.Entry<String, CharSequence> input) {
                return new SimpleCharValueTake5Pair(builder, input.getKey(), input.getValue());
            }
        });
    }

    @Override
    public Iterator<Take5Pair> iterator() {
        return pairs().iterator();
    }
}
