/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2010 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5build;

import java.util.Iterator;

/**
 * One entry point from a {@link Take5Builder}.
 */
public class Take5EntryPoint {
    Take5Builder builder;
    String name;
    byte[] asciiName;
    String inputName;           // file name or whatever
    int contentFlags;
    int contentMinVersion;
    int contentMaxVersion;
    boolean loaded;        // each entry point must be loaded exactly once!

    boolean acceptEmpty;
    State startState;
    int indexOffset;
    int keyCount;
    int maxMatches;
    int maxKeyLength;
    int maxValueSize;
    char minCharacter;
    char maxCharacter;
    int stateCount;             // set by countStuff
    int edgeCount;              // set by countStuff
    int acceptEdgeCount;        // set by countStuff

    Take5EntryPoint(String name, Take5Builder builder) {
        this.builder = builder;
        this.name = name;
        this.asciiName = Utils.toAscii(name);
        this.inputName = name;
    }

    /**
     * Set the content flags.
     * @param flags the content flags.
     */
    public void setContentFlags(int flags) {
        this.contentFlags = flags;
    }

    /**
     * Set both the minimum and maximum content version numbers to the
     * same value.
     * @param version the minimum and maximum content version.
     */
    public void setContentVersion(int version) {
        setContentVersion(version, version);
    }

    /**
     * Set the minimum and the maximum content version numbers.
     * @param minVersion the minimum content version.
     * @param maxVersion the maximum content version.
     */
    public void setContentVersion(int minVersion, int maxVersion) {
        if (!(0 <= minVersion && minVersion <= maxVersion)) {
            throw new Take5BuilderException("Bad content version");
        }
        this.contentMinVersion = minVersion;
        this.contentMaxVersion = maxVersion;
    }
 
    /**
     * Load the entry point with a sequence of {@link Take5Pair}s.  The
     * Take5Pairs must be presented in the order determined by comparing
     * their keys.  (Fortunately Take5 and Java both compare strings
     * lexicographically by Unicode code points!)
     * <P>
     * An entry point can only be loaded once, and every outstanding entry
     * point must be loaded before the Take5Builder can build a binary.
     *
     * @param pairs an iterator that generates Take5Pairs.
     * @throws Take5ParseError
     */
    public void loadContent(Iterator<? extends Take5Pair> pairs) {
        if (loaded) {
            throw new Take5BuilderException("Entry point already loaded");
        }
        loaded = true;
        builder.loadContent(this, pairs);
        stateCount = 0;
        edgeCount = 0;
        acceptEdgeCount = 0;
        countStuff(startState);
    }

    void countStuff(State s) {
        if (s.mark != this) {
            s.mark = this;
            boolean[] ea = s.edgeIsAccept;
            State[] es = s.edgeState;
            int nedges = ea.length;
            stateCount++;
            edgeCount += nedges;
            for (int i = 0; i < nedges; i++) {
                if (ea[i]) {
                    acceptEdgeCount++;
                }
                countStuff(es[i]);
            }
        }
    }
}
