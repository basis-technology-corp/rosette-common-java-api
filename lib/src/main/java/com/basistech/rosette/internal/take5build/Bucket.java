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

import com.google.common.collect.Lists;

import java.util.LinkedList;

/**
 * Hash bucket (build.h)
 */
class Bucket implements Comparable<Bucket> {
    final int index;
    int fun;
    int count;
    LinkedList<PerfhashKeyValuePair> pairs;

    Bucket(int index) {
        this.index = index;
        this.pairs = Lists.newLinkedList();
    }

    @Override
    public int compareTo(Bucket o) {
        int rv = count - o.count;
        if (rv != 0) {
            return rv;
        }
        return index - o.index;
    }
}
