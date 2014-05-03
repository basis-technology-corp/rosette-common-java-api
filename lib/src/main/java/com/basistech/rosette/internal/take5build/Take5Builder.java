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

package com.basistech.rosette.internal.take5build;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSink;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.basistech.rosette.internal.take5build.Take5Format.ENGINE_PERFHASH;
import static com.basistech.rosette.internal.take5build.Take5Format.ENGINE_SEARCH;
import static com.basistech.rosette.internal.take5build.Take5Format.EPTLEN_VERSION_5_6;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_ACCEPT_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_ACCEPT_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_FLAGS;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_MAX_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_CONTENT_MIN_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_INDEX_OFFSET;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_INDEX_START;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_MATCHES;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_VALUE_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MAX_WORD_LENGTH;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_MIN_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_NAME;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_STATE_START;
import static com.basistech.rosette.internal.take5build.Take5Format.EPT_WORD_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.FLAG_LITTLE_ENDIAN;
import static com.basistech.rosette.internal.take5build.Take5Format.FLAG_LOOKUP_AUTOMATON;
import static com.basistech.rosette.internal.take5build.Take5Format.HDRLEN_VERSION_5_6;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_ACCEPT_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_ACCEPT_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_BUILD_DAY;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_BUILD_MSEC;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_CONTENT_FLAGS;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_CONTENT_MAX_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_CONTENT_MIN_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_COPYRIGHT_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_COPYRIGHT_STRING;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_EDGE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_ENTRY_POINT_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_ENTRY_POINT_HEADER_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_FLAGS;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_FSA_DATA;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_FSA_ENGINE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_KEYCHECK_DATA;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_KEYCHECK_FORMAT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAGIC;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAX_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAX_MATCHES;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAX_VALUE_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAX_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MAX_WORD_LENGTH;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_METADATA_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_METADATA_STRING;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MIN_CHARACTER;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_MIN_VERSION;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_REQUIRED_ALIGNMENT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_STATE_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_TOTAL_SIZE;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_VALUE_DATA;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_VALUE_FORMAT;
import static com.basistech.rosette.internal.take5build.Take5Format.HDR_WORD_COUNT;
import static com.basistech.rosette.internal.take5build.Take5Format.MAGIC_T4K3;
import static com.basistech.rosette.internal.take5build.Take5Format.VALUE_FORMAT_INDIRECT;
import static com.basistech.rosette.internal.take5build.Take5Format.VERSION_5_0;
import static com.basistech.rosette.internal.take5build.Take5Format.VERSION_5_4;
import static com.basistech.rosette.internal.take5build.Take5Format.VERSION_5_6;

/**
 * Builder for Take5 binaries. Use {@link com.basistech.rosette.internal.take5build.Take5Builder.Factory}
 * as a factory to obtain these classes.
 */
public class Take5Builder {
    // Currently this builder is up to version 5.6:
    static final int VERSION_MIN = VERSION_5_0;
    static final int VERSION_MAX = VERSION_5_6;
    static final int HDRLEN = HDRLEN_VERSION_5_6;
    static final int EPTLEN = EPTLEN_VERSION_5_6;

/*
 * 1e7 is about 1 second for 100k keys on my desktop:
 */
    static final double TIME_BUDGET_SEC = 1e7;
    static final double TIME_BUDGET = 15 * TIME_BUDGET_SEC;       /* -t hash -r */

    Engine engine;
    int valueSize;
    KeyFormat keyFormat;
    ValueFormat valueFormat;
    OutputFormat outputFormat;

    boolean storeValues;
    boolean generateBinary;
    boolean varyNothing;
    ByteOrder byteOrder = ByteOrder.nativeOrder();
    byte[] copyright;
    Map<String, String> metadata;
    List<Take5EntryPoint> entryPoints = Lists.newArrayList();
    ValueRegistry valueRegistry;
    List<Value> valueTable = Lists.newArrayListWithCapacity(1000);
    State[] stateRegistry;
    State terminalState;        // the state with no edges
    State lastState;            // (initially null) most recent state
    int stateCount;             // (initially 0) number of states
    int edgeCount;              // (initially 0) number of edges
    int acceptEdgeCount;        // (initially 0) number of accept edges
    int totalKeyCount;          // (initially 0) sum of all entry points
    int globalMaxHashFun;
    int globalMillionsTested;
    int globalMaxMatches;       // (initially 0) max of all entry points
    int globalIndexCount;
    int globalBucketCount;

    int globalMaxKeyLength;     // (initially 0) max of all entry points
    int globalMaxValueSize;     // (initially 0) max of all entry points
    char globalMinCharacter = 0xFFFF; //         min of all entry points
    char globalMaxCharacter;    // (initially 0) max of all entry points
    double bestExpectedTime;
    /*
     * Allow as much as 0.1% chance of failure:
     */
    double finishProbability = 0.999;               /* -t hash -a */
    double bestFinishProbability;
    /*
     * If you're storing both keys and values, and using the default
     * time_budget, then 0.15% more indexes than keys is close to optimal as
     * dictionaries get large enough for you to care:
     */
    double indexExpansion = 1.001500;

    // Used during the construction of a single entry point:
    String inputName;
    int keyCount;
    int frameCount;
    // These size guesses don't matter much, as we expand on demand:
    int[] frameIndex = new int[20];
    char[] edgeChar = new char[100];
    boolean[] edgeIsAccept = new boolean[100];
    State[] edgeState = new State[100];
    int maxKeyLength;
    char minCharacter;
    char maxCharacter;

    // For code generation:
    List<Segment> segments;     // all segments
    List<Segment> outputList;   // in output order
    LinkedList<PerfhashKeyValuePair> allPerfhashPairs;
    Primes primes = new Primes();

    public enum ValueFormat {
        IGNORE,                        /* -i */
        INDEX,                         /* -x */
        STRING,                        /* -s */
        PTR                            /* -p */
    }

    /**
     * With {@link Engine#PERFHASH}, what sort of keys to write.
     */
    public enum KeyFormat {
        /**
         * For the FSA search engine.
         */
        FSA,                             /* -t fsa or -5 */
        /**
         * Just accept hash collisions.
         */
        HASH_NONE,                       /* -t hash/none */
        /**
         * Store a hash value to get very few hash collisions.
         */
        HASH_HASH32,                     /* -t hash/hash32 */
        /**
         * Store the key to get no collections.
         */
        HASH_STRING                      /* -t hash/str */
    }

    /**
     * What gets written.
     */
    public enum OutputFormat {
        // Take5 binary file
        TAKE5,
        // Textual dump of FSA
        FSA,
        // Nothing at all.
        NONE
    }

    /**
     * Which lookup engine.
     */
    public enum Engine {
        /**
         * Finite State engine.
         */
        FSA,
        /**
         * Perfect Hash Engine.
         */
        PERFHASH;
    }

    // XXX Should be a way to vary these:
    int valueRegistryLength = 99991;
    int stateRegistryLength = 99991;

    /**
     * Create a Take5Builder.  If the valueFormat is {@link ValueFormat#PTR},
     * then the value size must be divisible by all the
     * alignments required by all payload values.  If the output mode is
     * something else, then the value size must be 0.
     */
    Take5Builder(Factory factory) throws Take5BuildException {
        this.engine = factory.engine;
        this.valueSize = factory.valueSize;
        this.keyFormat = factory.keyFormat;
        this.valueFormat = factory.valueFormat;
        this.outputFormat = factory.outputFormat;

        // I'd parenthesize this to make it more readable, but checkstyle
        // won't let me:
        storeValues = valueFormat == ValueFormat.PTR; //
        generateBinary = !(outputFormat == OutputFormat.NONE || outputFormat == OutputFormat.FSA);

        if (storeValues) {
            if (valueSize <= 0) {
                throw new Take5BuildException("Inconsistent valueSize and outputMode");
            }
            valueRegistry = new ValueRegistry(valueRegistryLength);
        } else {
           valueSize = 0; // Don't get distracted by the default in the factory.
        }

        if (keyFormat == KeyFormat.FSA) {
            initializeFsaBuilder();
        } else {
            initializePerfhashBuilder();
        }
    }

    private void initializeFsaBuilder() {
        stateRegistry = new State[stateRegistryLength];
        terminalState = findState(0, 0);
    }

    private void initializePerfhashBuilder() {
        // nothing required
    }

    /**
     * Set the copyright string to be stored in the Take5 binary.
     *
     * @param copyright the copyright notice.
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright.getBytes(Charsets.UTF_8);
    }

    /**
     * Set the metadata map to be stored in the Take5 binary.  Neither the
     * keys nor the values in the metadata map may contain the null
     * character.
     *
     * @param metadata the metadate map.
     */
    public void setMetadata(Map<String, String> metadata) throws Take5BuildException {
        for (String key : metadata.keySet()) {
            if (0 <= key.indexOf(0) || 0 <= metadata.get(key).indexOf(0)) {
                throw new Take5BuildException("Metadata contains a null");
            }
        }
        this.metadata = metadata;
    }

    /**
     * Create a new entry point.  Every entry point you create must be
     * loaded with content before you can build a Take5 binary.  The name
     * must consist of only ASCII characters. (why?)
     * <P>
     * Note that creating a single entry point Take5 binary whose entry
     * point is named <CODE>"main"</CODE> will result in a binary that can
     * be interpreted by older Take5 runtimes.
     *     
     * @param name the name for the new entry point.
     * @return a new, unloaded, entry point.
     */
    public Take5EntryPoint newEntryPoint(String name) throws Take5BuildException {
        assert name != null;
        for (Take5EntryPoint ep : entryPoints) {
            if (ep.name.equals(name)) {
                throw new Take5BuildException("Duplicate entry point name " + name + ".");
            }
        }
        Take5EntryPoint ep = new Take5EntryPoint(name, this);
        entryPoints.add(ep);
        return ep;
    }

    /**
     * Set the byte order for the generated binary.  By default, the byte
     * order is set to the native byte order, so you probably don't want to
     * call this unless you are cross-compiling.
     *
     * @param byteOrder the new byte order.
     */
    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    /**
     * Get the byte order that will be used for the generated binary.  By
     * default, the byte order is set to the native byte order.
     *
     * @return the currrent byte order.
     */
    public ByteOrder getByteOrder() {
        return byteOrder;
    }

    // Intentionally not public.  This is just for testing purposes.
    void setVaryNothing(boolean varyNothing) {
        this.varyNothing = varyNothing;
    }

    void loadContent(Take5EntryPoint ep,  Iterator<Take5Pair> pairs) throws Take5ParseError {
        Take5Pair pair;
        beginKeys(ep);
        if (storeValues) {
            while (pairs.hasNext()) {
                pair = pairs.next();
                final Value value = findValue(pair);
                addKey(pair.getKey(), pair.getKeyLength(), value);
            }
        } else {
            while (pairs.hasNext()) {
                pair = pairs.next();
                addKey(pair.getKey(), pair.getKeyLength(), null);
            }
        }
        endKeys(ep);
    }

    /**
     * Get ready to process keys.
     */
    private void beginKeys(Take5EntryPoint ep) {
        inputName = ep.inputName;
        keyCount = 0;
        maxKeyLength = 0;
        if (valueRegistry != null) {
            valueRegistry.maxValueSize = 0;
        }
        if (keyFormat == KeyFormat.FSA) {
            fsaBeginKeys();
        } else {
            perfhashBeginKeys();
        }
    }

    private void fsaBeginKeys() {
        minCharacter = 0xFFFF;
        maxCharacter = 0;
        frameCount = 0;
        frameIndex[0] = 1;
        edgeIsAccept[0] = false;
        edgeState[0] = terminalState;
    }

    private void perfhashBeginKeys() {
        allPerfhashPairs = Lists.newLinkedList();
    }

    /**
     * This is the main entry into the FSA builder.  Each key is presented
     * in turn.
     */
    private void addKey(char[] key, int keyLength, Value value) throws Take5ParseError {
        if (keyLength > maxKeyLength) {
            maxKeyLength = keyLength;
        }

        if (keyFormat == KeyFormat.FSA) {
            fsaAddKey(key, keyLength, value);
        } else {
            perfhashAddKey(key, keyLength, value);
        }
        keyCount++;

    }

    private void perfhashAddKey(char[] key, int keyLength, Value value) {
        byte[] keyBytes = new String(key, 0, keyLength).getBytes(Charsets.UTF_16BE);
        Value keyAsValue = valueRegistry.intern(keyBytes, 0, keyLength, 2, Value.KEY);

        int keyHash = FnvHash.fnvhash(0, keyAsValue.data, 0, keyAsValue.length);
        PerfhashKeyValuePair pair = new PerfhashKeyValuePair(keyAsValue, value, keyHash);
        allPerfhashPairs.addFirst(pair);
    }

    private void fsaAddKey(final char[] key, final int kl, final Value value) throws Take5ParseError {
        int fc = frameCount;
        int top = frameIndex[fc];
        char c;

        // Make sure there is enough room:
        if (kl >= frameIndex.length) {
            int[] old = frameIndex;
            frameIndex = new int[kl + 20]; // XXX 20 is a guess
            for (int i = 0; i < old.length; i++) {
                frameIndex[i] = old[i];
            }
        }
        if (top + kl > edgeChar.length) {
            char[] oChar = edgeChar;
            boolean[] oIsAccept = edgeIsAccept;
            State[] oState = edgeState;
            int len = top + kl + 100; // XXX 100 is a guess
            edgeChar = new char[len];
            edgeIsAccept = new boolean[len];
            edgeState = new State[len];
            for (int i = 0; i < oChar.length; i++) {
                edgeChar[i] = oChar[i];
                edgeIsAccept[i] = oIsAccept[i];
                edgeState[i] = oState[i];
            }
        }

        // determine the common prefix with the previous key
        int idx = 0;
        final int minLength = Math.min(kl, fc);
        while (idx < minLength && key[idx] == edgeChar[frameIndex[1 + idx] - 1]) {
            idx++;
        }
        // idx == kl || idx == fc || key[idx] != edgeChar[frameIndex[1 + idx] - 1]
        if (idx == kl) {
            // New suffix is empty, so this key is equal to a prefix of the
            // previous key and is thus out of order.  Except it isn't if
            // this is the empty key, and this is the first key we have
            // seen.
            if (!(kl == 0 && fc == 0 && !edgeIsAccept[0])) {
                throw new Take5ParseError("Keys out of order", inputName, keyCount);
            }
            edgeIsAccept[0] = true;
            return;
        }

        // Pack up the old suffix:
        if (idx == fc) {
            // Old suffix was empty, so create a new frame for the first
            // new character.
            assert frameIndex[fc] == top;
            fc++;
        } else {
            // Old suffix was nonempty, so pack up until we're back to the
            // frame that contains its first character.

            if (!(key[idx] > edgeChar[frameIndex[1 + idx] - 1])) {
                throw new Take5ParseError("Keys out of order", inputName, keyCount);
            }

            idx++;
            while (idx < fc) {
                fc--;
                int beg = frameIndex[fc];
                edgeState[beg - 1] = findState(beg, top);
                top = beg;
            }
        }

        // Load the new suffix:
        while (fc < kl) {
            c = key[fc - 1];
            if (c > maxCharacter) { maxCharacter = c; }
            if (c < minCharacter) { minCharacter = c; }
            edgeChar[top] = c;
            edgeState[top] = terminalState;
            edgeIsAccept[top] = false;
            top++;
            frameIndex[fc] = top;
            fc++;
        }
        c = key[fc - 1];
        if (c > maxCharacter) { maxCharacter = c; }
        if (c < minCharacter) { minCharacter = c; }
        edgeChar[top] = c;
        edgeState[top] = terminalState;
        edgeIsAccept[top] = true; // last new edge is an accept edge
        top++;
        frameIndex[fc] = top;
        frameCount = fc;
        if (storeValues) {
            valueTable.add(value);
        }
    }

    /**
     * No more keys, close the entry point.
     */
    private void endKeys(Take5EntryPoint ep) {

        ep.maxKeyLength = maxKeyLength;
        globalMaxKeyLength = Math.max(globalMaxKeyLength, maxKeyLength);
        ep.indexOffset = totalKeyCount;
        ep.keyCount = keyCount;
        totalKeyCount += keyCount;
        assert !storeValues || totalKeyCount == valueTable.size();
        if (valueRegistry != null) {
            ep.maxValueSize = valueRegistry.maxValueSize;
            globalMaxValueSize = Math.max(globalMaxValueSize, valueRegistry.maxValueSize);
        }

        if (keyFormat == KeyFormat.FSA) {
            fsaEndKeys(ep);
        } else {
            perfhashEndKeys(ep);
        }

        globalIndexCount += ep.indexCount;
        globalMillionsTested += ep.millionsTested;
        globalBucketCount += ep.bucketCount;
    }

    private void fsaEndKeys(Take5EntryPoint ep) {
        int fc = frameCount;
        int top = frameIndex[fc];
        // Pack up the old suffix:
        while (0 < fc) {
            fc--;
            int beg = frameIndex[fc];
            edgeState[beg - 1] = findState(beg, top);
            top = beg;
        }
        ep.acceptEmpty = edgeIsAccept[0];
        ep.startState = edgeState[0];
        assert keyCount == ep.startState.keyCount + (ep.acceptEmpty ? 1 : 0);

        ep.maxMatches = ep.startState.maxMatches + (ep.acceptEmpty ? 1 : 0);
        globalMaxMatches = Math.max(globalMaxMatches, ep.maxMatches);
        ep.indexCount = ep.startState.keyCount;
        if (ep.acceptEmpty) {
            ep.indexCount++;
        }

        ep.minCharacter = minCharacter;
        if (minCharacter < globalMinCharacter) { globalMinCharacter = minCharacter; }
        ep.maxCharacter = maxCharacter;
        if (maxCharacter > globalMaxCharacter) { globalMaxCharacter = maxCharacter; }
    }

    private void perfhashEndKeys(Take5EntryPoint ep) {
        int indexCount = (int)Math.ceil(keyCount * indexExpansion);
        indexCount = primes.nextPrime(indexCount);
        int bucketCount = bestBucketCount(indexCount);

        Bucket[] bucketTable = new Bucket[bucketCount];

        for (int x = 0; x < ep.bucketCount; x++) {
            bucketTable[x] = new Bucket(x);
        }

        int wordCount = 0;
        for (PerfhashKeyValuePair pair : allPerfhashPairs) {
            wordCount++;
            int x = pair.keyHash % ep.bucketCount;
            bucketTable[x].pairs.addFirst(pair);
            bucketTable[x].count++;
        }

        Arrays.sort(bucketTable);

        boolean[] indexUsed = new boolean[ep.indexCount];
        int maxHashFun = 0;
        int millionsTested = 0;
        int funCnt = 0;
        int fun;

        for (int x = 0; x < bucketCount; x++) {
            if (bucketTable[x].count > 1) {
                fun = indexCount;
                while (!tryFit(fun, indexCount, indexUsed, bucketTable[x].pairs)) {
                    if (fun > 0x7FFFFFFF) {
                        throw new Take5BuilderException("Failure! hash space exhausted!");
                    }
                }
                bucketTable[x].fun = fun;
                if (fun > maxHashFun) {
                    maxHashFun = fun;
                }
                funCnt += 1 + (funCnt / 1000000);
                funCnt %= 1000000;
            }

        }

        ep.wordCount = wordCount;
        ep.indexCount = indexCount;
        ep.bucketCount = bucketCount;
        ep.bucketTable = bucketTable;
        ep.maxHashFun = maxHashFun;
        ep.millionsTested = millionsTested + (funCnt + 500000) / 1000000;


    }

    private boolean tryFit(int fun, int indexCount, boolean[] indexUsed, LinkedList<PerfhashKeyValuePair> pairs) {
        for (PerfhashKeyValuePair pair : pairs) {
            int idx = pair.key.getKeyHash(fun) % indexCount;
            if (!indexUsed[idx]) {
                pair.index = idx;
                indexUsed[idx] = true;
            } else {
                for (PerfhashKeyValuePair undoPair : pairs) {
                    indexUsed[undoPair.index] = false;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Given a candidate bucket count, the target index count, and the list of
     * key hash values, check whether the expected value of the number of hash
     * functions that we will have to test is less than our time budget.  Also
     * check whether the risk that we will might exhaust our hash function
     * space is accepably low.
     *
     * We could compute almost as good estimates knowing _just_ the number of
     * keys, but this is plenty fast, and slighly more accurate.
     *
     * "More accurate" does mean that this function is _not_ monotonic: if
     * bucket_count_ok(i, ...) is false, it does _not_ follow that
     * bucket_count_ok(j, ...) is false for all j < i.  So the binary search
     * that best_bucket_count() does is not actually deterministic.  But as
     * long as you're not pushing the limits, that doesn't really matter.
     */
    private boolean bucketCountOk(int bucketCount, int indexCount) {
        int i;
        int holes;
        int[] counts;
        double e;
        double expected;
        double willFinish;
        double funs;

        counts = new int[bucketCount]; // Java fills us with zeros.

        for (PerfhashKeyValuePair pair : allPerfhashPairs) {
            counts[pair.keyHash % bucketCount]++;
        }

        Arrays.sort(counts);

        funs = 0x80000000 - indexCount;
        holes = indexCount;
        expected = 0.0;
        willFinish = 1.0;
        for (i = 0; i < bucketCount; i++) {
            if (counts[i] > 1) {
                e = 1.0;
                while (counts[i]-- > 0) {
                    e *= (double)indexCount / (double)holes;
                    holes--;
                }
                expected += e;
                willFinish *= 1 - Math.pow(1 - (1 / e), funs);
            }
        }

        if (expected <= TIME_BUDGET && willFinish >= finishProbability) {
            bestExpectedTime = expected;
            bestFinishProbability = willFinish;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Given the target index count, and the list of key hash values, find the
     * smallest prime number (>= 5) where we expect to test no more than
     * time_budget hash functions, and where the probability of success is at
     * least finish_probability.  No magic here, just binary search.
     */
    private int bestBucketCount(int indexCount) {
        int low;
        int med;
        int high;

        low = 5;
        if (bucketCountOk(low, indexCount)) {
            return low;
        }

        assert primes.isPrime(indexCount);

        high = indexCount;

        if (!bucketCountOk(high, indexCount)) {
            throw new Take5BuilderException("Desired compile time is unachievable.");
        }

        for (;;) {
            med = primes.nextPrime((low + high) / 2);
    /* The following makes sure we really try _all_ the prime numbers... */
            if (med >= high) {
                med = primes.nextPrime(low + 1);
            }
            assert low < med;
            if (med >= high) {
                return high;
            }
            if (bucketCountOk(med, indexCount)) {
                high = med;
            } else {
                low = med;
            }
        }
    }

    private State findState(int beg, int end) {
        int nedges = end - beg;
        int hash = nedges;
        for (int i = beg; i < end; i++) {
            hash = Utils.hashStep(hash, edgeChar[i]);
            hash = Utils.hashStep(hash, edgeIsAccept[i] ? 17 : 23); // random small primes
            hash = Utils.hashStep(hash, edgeState[i].hashCode());
        }
        int index = (hash & 0x7FFFFFFF) % stateRegistryLength;
        State firstState = stateRegistry[index];
        State s;
        char[] ec;
        boolean[] ea;
        State[] es;
    bucketNext:
        for (s = firstState; s != null; s = s.next) {
            if (s.hash == hash && s.edgeChar.length == nedges) {
                ec = s.edgeChar;
                ea = s.edgeIsAccept;
                es = s.edgeState;
                for (int i = 0; i < nedges; i++) {
                    if (!(ec[i] == edgeChar[beg + i]
                          && ea[i] == edgeIsAccept[beg + i]
                          && es[i] == edgeState[beg + i])) {
                        continue bucketNext;
                    }
                }                
                return s;
            }
        }
        s = new State();
        ec = new char[nedges];
        ea = new boolean[nedges];
        es = new State[nedges];
        int keys = 0;
        int maxMatches = 0;
        edgeCount += nedges;
        for (int i = 0; i < nedges; i++) {
            ec[i] = edgeChar[beg + i];
            ea[i] = edgeIsAccept[beg + i];
            es[i] = edgeState[beg + i];
            keys += es[i].keyCount;
            int mm = es[i].maxMatches;
            if (ea[i]) {
                keys++;
                mm++;
                acceptEdgeCount++;
            }
            if (mm > maxMatches) { maxMatches = mm; }
        }
        s.keyCount = keys;
        s.maxMatches = maxMatches;
        s.edgeChar = ec;
        s.edgeIsAccept = ea;
        s.edgeState = es;
        s.id = stateCount++;
        s.previous = lastState;
        s.hash = hash;
        s.next = firstState;
        lastState = s;
        stateRegistry[index] = s;
        return s;
    }

    private Value findValue(Take5Pair pair) {
        byte[] data = pair.getValue();
        if (data == null) {
          return null;
        }
        int offset = pair.getOffset();
        return valueRegistry.intern(data, offset, offset + pair.getLength(), pair.getAlignment(), Value.VALUE);
    }

    /**
     * Create a byte array that contains a Take5 binary.
     * @return a byte array containing a Take5 binary.
     */
    public byte[] buildArray() throws Take5BuildException {
        if (!generateBinary) {
            throw new Take5BuildException("Output mode is not binary");
        }
        int size = prepareSegments();
        byte[] buf = new byte[size];
        writeSegments(ByteBuffer.wrap(buf));
        releaseSegments();
        return buf;
    }

    /**
     * Create a ByteBuffer that contains a Take5 binary.
     * @return a ByteBuffer containing a Take5 binary.
     */
    public ByteBuffer buildBuffer() throws Take5BuildException {
        if (!generateBinary) {
            throw new Take5BuildException("Output mode is not binary");
        }
        int size = prepareSegments();
        ByteBuffer buffer = ByteBuffer.allocate(size); // or allocateDirect...
        writeSegments(buffer);
        releaseSegments();
        buffer.position(0);
        buffer.limit(size);
        return buffer;
    }

    /**
     * Write a Take5 binary into a {@link ByteSink}.
     *
     * @param sink the sinl.
     */
    public void buildToSink(ByteSink sink) throws IOException {
        if (outputFormat == OutputFormat.NONE) {
            throw new Take5BuilderException("Output mode is NONE");
        } else if (outputFormat == OutputFormat.FSA) {
            final Writer writer = sink.asCharSink(Charsets.UTF_8).openBufferedStream();
            formatDescription(writer);
            writer.close();
        } else {
            prepareSegments();
            OutputStream out = sink.openBufferedStream();
            writeSegments(out);
            out.close();
            releaseSegments();
        }
    }

    void writeSegments(OutputStream out) throws IOException {
        assert segments != null && outputList != null;
        int addr = 0;
        for (Segment s : outputList) {
            if (s.getSize() > 0) {
                int pad = s.getAddress() - addr;
                assert pad >= 0;
                while (pad-- > 0) {
                    out.write(0xAA); // XXX what does the C builder use?
                }
                s.writeSegment(out);
                addr = s.getLimit();
            }
        }
    }

    void writeSegments(ByteBuffer buffer)  {
        assert segments != null && outputList != null;
        for (Segment s : outputList) {
            if (s.getSize() > 0) {
                s.writeSegment(buffer);
            }
        }
    }

    // Release everything we allocated in order to build the binary.
    void releaseSegments() {
        segments = null;
        outputList = null;
    }

    int prepareSegments() {
        assert generateBinary;
        assert segments == null && outputList == null;
        segments = new ArrayList<Segment>();
        outputList = new ArrayList<Segment>();
        int flags = 0;                // header flags
        int minVersion = VERSION_MIN; // backwards compatible to this version

        if (byteOrder.equals(ByteOrder.LITTLE_ENDIAN)) {
            flags |= FLAG_LITTLE_ENDIAN;
        }
        if (valueFormat != ValueFormat.IGNORE) {
            flags |= FLAG_LOOKUP_AUTOMATON;
        }

        // Header Segment
        SimpleSegment headerSeg = new SimpleSegment(this, "Header", 4 * HDRLEN, 4);
        int headerAddr = headerSeg.getAddress();
        IntBuffer header = headerSeg.getIntBuffer();
        ByteBuffer headerBytes = headerSeg.getByteBuffer();
        assert headerAddr == 0; // The header must be first!

        doMetaData(header);
        doCopyright(header);
        doBuildTime(header);
        doIndex(header);
        doValues(header);

        if (entryPoints.size() == 1 && entryPoints.get(0).name.equals("main")) {
            Take5EntryPoint ep = entryPoints.get(0);
            header.put(HDR_CONTENT_FLAGS, ep.contentFlags);
            header.put(HDR_CONTENT_MIN_VERSION, ep.contentMinVersion);
            header.put(HDR_CONTENT_MAX_VERSION, ep.contentMaxVersion);
        } else {
            // multiple entry point support was introduced in version 5.4:
            minVersion = Math.max(minVersion, VERSION_5_4);
            header.put(HDR_CONTENT_FLAGS, 0);
            header.put(HDR_CONTENT_MIN_VERSION, 0);
            header.put(HDR_CONTENT_MAX_VERSION, 0);
        }

        // Now force all segments closed.  Compute the total size of the
        // output, and the required alignment.  This will make sure that
        // all segments to be written have now been added to outputList.
        int totalSize = 0;
        int alignment = 4;
        for (Segment s : segments) {
            totalSize = Math.max(totalSize, s.getLimit());
            alignment = Utils.lcm(alignment, s.alignment);
        }

        // And fill in the final bits in the header.
        Utils.putNetworkInt(headerBytes, HDR_MAGIC, MAGIC_T4K3);
        Utils.putNetworkInt(headerBytes, HDR_FLAGS, flags);
        Utils.putNetworkInt(headerBytes, HDR_MIN_VERSION, minVersion);
        Utils.putNetworkInt(headerBytes, HDR_MAX_VERSION, VERSION_MAX);
        header.put(HDR_TOTAL_SIZE, totalSize);
        header.put(HDR_REQUIRED_ALIGNMENT, alignment);

        return totalSize;
    }

    void doIndex(IntBuffer header) {
        // Entry Point Segment
        int nentries = entryPoints.size();
        header.put(HDR_ENTRY_POINT_COUNT, nentries);
        header.put(HDR_ENTRY_POINT_HEADER_SIZE, 4 * EPTLEN);
        SimpleSegment entrySeg = new SimpleSegment(this, "Entry Points",  nentries * (4 * EPTLEN), 4);


        if (keyFormat == KeyFormat.FSA) {
            doFstStates(header, entrySeg);
        } else {
            doHashBuckets(header, entrySeg);
        }
    }

    void doHashBuckets(IntBuffer header, SimpleSegment entrySeg) {
        assert globalIndexCount >= totalKeyCount;

        header.put(HDR_FSA_ENGINE, ENGINE_PERFHASH);
        new BucketSegment(this, "Hash Buckets", entrySeg); // construction does all the work.
    }

    void doFstStates(IntBuffer header, SimpleSegment entrySeg) {
        IntBuffer entry = entrySeg.getIntBuffer();

        header.put(HDR_FSA_ENGINE, ENGINE_SEARCH);

        // State Segment
        StateSegment stateSeg = new StateSegment(this, "States");
        int stateAddr = stateSeg.getAddress();

        int index = -1;
        int i = 0;
        for (Take5EntryPoint ep : entryPoints) {
            if (!ep.loaded) {
                throw new Take5BuilderException(String.format("Entrypoint %s not loaded.", ep.name));
            }
            int state = stateAddr + ep.startState.address;
            if (ep.acceptEmpty) {
                state |= 1;
                index++;
            }
            entry.put(i + EPT_STATE_START, state);
            entry.put(i + EPT_INDEX_START, index);
            entry.put(i + EPT_INDEX_OFFSET, ep.indexOffset);
            entry.put(i + EPT_NAME, ascizString(ep.asciiName));
            entry.put(i + EPT_CONTENT_FLAGS, ep.contentFlags);
            entry.put(i + EPT_CONTENT_MIN_VERSION, ep.contentMinVersion);
            entry.put(i + EPT_CONTENT_MAX_VERSION, ep.contentMaxVersion);

            // Common header:
            entry.put(i + EPT_WORD_COUNT, ep.keyCount);
            entry.put(i + EPT_STATE_COUNT, ep.stateCount);
            entry.put(i + EPT_ACCEPT_STATE_COUNT, 0); // It's a Mealy machine...
            entry.put(i + EPT_EDGE_COUNT, ep.edgeCount);
            entry.put(i + EPT_ACCEPT_EDGE_COUNT, ep.acceptEdgeCount);
            entry.put(i + EPT_MAX_MATCHES, ep.maxMatches);
            entry.put(i + EPT_MAX_WORD_LENGTH, ep.maxKeyLength);
            entry.put(i + EPT_MAX_VALUE_SIZE, ep.maxValueSize);
            entry.put(i + EPT_MIN_CHARACTER, ep.minCharacter);
            entry.put(i + EPT_MAX_CHARACTER, ep.maxCharacter);

            index += ep.startState.keyCount;
            i += EPTLEN;
        }
        assert index + 1 == totalKeyCount;

        header.put(HDR_FSA_DATA, entrySeg.getAddress());
        header.put(HDR_FSA_ENGINE, ENGINE_SEARCH);

        // Common header:
        header.put(HDR_WORD_COUNT, totalKeyCount);
        header.put(HDR_STATE_COUNT, stateCount);
        header.put(HDR_ACCEPT_STATE_COUNT, 0); // It's a Mealy machine...
        header.put(HDR_EDGE_COUNT, edgeCount);
        header.put(HDR_ACCEPT_EDGE_COUNT, acceptEdgeCount);
        header.put(HDR_MAX_MATCHES, globalMaxMatches);
        header.put(HDR_MAX_WORD_LENGTH, globalMaxKeyLength);
        header.put(HDR_MAX_VALUE_SIZE, globalMaxValueSize);
        header.put(HDR_MIN_CHARACTER, globalMinCharacter);
        header.put(HDR_MAX_CHARACTER, globalMaxCharacter);
    }

    // Allocate a null-terminated ASCII string in the output and return it's address.
    int ascizString(byte[] ascii) {
        SimpleSegment seg = new SimpleSegment(this, "ASCIZ", ascii.length + 1, 1);
        ByteBuffer buf = seg.getByteBuffer();
        buf.put(ascii);
        buf.put((byte)0);
        return seg.getAddress();
    }

    void doCopyright(IntBuffer header) {
        if (copyright != null) {
            header.put(HDR_COPYRIGHT_STRING, ascizString(copyright));
            header.put(HDR_COPYRIGHT_SIZE, copyright.length);
        } else {
            header.put(HDR_COPYRIGHT_STRING, 0);
            header.put(HDR_COPYRIGHT_SIZE, 0);
        }
    }

    void doMetaData(IntBuffer header) {
        if (metadata != null) {
            StringBuilder build = new StringBuilder(100);
            for (String key : metadata.keySet()) {
                build.append(key);
                build.append("\u0000");
                build.append(metadata.get(key));
                build.append("\u0000");
            }
            String str = build.toString();
            int len = str.length();
            SimpleSegment seg = new SimpleSegment(this, "Metadata", (len + 1) * 2, 2);
            CharBuffer buf = seg.getCharBuffer();
            buf.put(str);
            buf.put((char)0);   // Yes, an extra null goes at the end!
            header.put(HDR_METADATA_STRING, seg.getAddress());
            header.put(HDR_METADATA_SIZE, len);
        } else {
            header.put(HDR_METADATA_STRING, 0);
            header.put(HDR_METADATA_SIZE, 0);
        }
    }

    void doBuildTime(IntBuffer header) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        if (varyNothing) {
            // Caconical test build time is 2005/12/31 23:59:60.259 UTC:
            header.put(HDR_BUILD_DAY, 746231);
            header.put(HDR_BUILD_MSEC, 86400259);
        } else {
            header.put(HDR_BUILD_DAY,
                       31 * (12 * calendar.get(Calendar.YEAR)
                             + calendar.get(Calendar.MONTH))
                       + (calendar.get(Calendar.DAY_OF_MONTH) - 1));
            header.put(HDR_BUILD_MSEC,
                       1000 * (60 * (60 * calendar.get(Calendar.HOUR_OF_DAY)
                                     + calendar.get(Calendar.MINUTE))
                               + calendar.get(Calendar.SECOND))
                       + calendar.get(Calendar.MILLISECOND));
        }
    }

    private int alignUp(int addr, int align) {
        assert align > 0;
        addr += align - 1;
        addr -= addr % align;
        return addr;
    }

    void doValues(IntBuffer header) {
        short flags = 0;

        if (keyFormat == KeyFormat.HASH_STRING) {
            flags |= Value.KEY;
        }
        if (valueFormat == ValueFormat.PTR || valueFormat == ValueFormat.STRING) {
            flags |= Value.VALUE;
        }

        ValueSegment valueSegment = null;

        if (flags != 0) {
            valueSegment = new ValueSegment(this, "Data", flags);
        }


        SimpleSegment keySegment = null;

        switch (keyFormat) {
        case HASH_NONE:
            header.put(HDR_KEYCHECK_FORMAT, Take5Format.KEYCHECK_FORMAT_NONE);
            break;
        case HASH_HASH32:
            assert valueTable.size() == 0;
            //Take5Builder builder, String description,         int bufferSize, int bufferAlignment
            keySegment = new SimpleSegment(this, "Key Hash32 Table", globalIndexCount * 4, 4);
            Arrays.fill(keySegment.rawbuf, (byte)-1);
            header.put(HDR_KEYCHECK_FORMAT, Take5Format.KEYCHECK_FORMAT_HASH32);
            header.put(HDR_KEYCHECK_DATA, keySegment.getAddress());
            break;
        case HASH_STRING:
            assert valueTable.size() == 0;
            keySegment = new SimpleSegment(this, "Key Table", globalIndexCount * 4, 4);
            header.put(HDR_KEYCHECK_DATA, keySegment.getAddress());
            header.put(HDR_KEYCHECK_DATA, keySegment.getAddress());
            break;
        case FSA:
            break;
        default:
            throw new RuntimeException("stupidity;");
        }


        switch (valueFormat) {
        case INDEX:
            header.put(HDR_VALUE_FORMAT, Take5Format.VALUE_FORMAT_INDEX);
            break;
        case PTR:
            if (valueSize % valueRegistry.commonAlign != 0) {
                throw new Take5BuilderException(String.format("Element size %d is not divisible"
                                + " by the required alignment %d",
                        valueSize,
                        valueRegistry.commonAlign));
            }
            // yes, fall through.
        case STRING:
            header.put(HDR_VALUE_FORMAT, VALUE_FORMAT_INDIRECT + valueSize);
            break;
        default:
            header.put(HDR_VALUE_FORMAT, Take5Format.VALUE_FORMAT_NONE);
            break;

        }

        if (valueTable.size() != 0) {
            assert keySegment == null;
            assert valueSegment != null;
            assert  valueTable.size() == globalIndexCount : "Stored value count inconsistent";
            TableSegment valueTableSegment = new TableSegment(this, "Value Table", valueSegment);
            header.put(HDR_VALUE_DATA, valueTableSegment.getAddress());
        } else if (storeValues || keySegment != null) {
            assert valueSegment != null;
            int ix = 0;
            SimpleSegment valueTableSegment = null;
            if (storeValues) {
                // we can't use the TableSegment class since we want to interleave this with key management.
                valueTableSegment = new SimpleSegment(this, "Value Table", allPerfhashPairs.size(), 4);
                header.put(HDR_VALUE_DATA, valueTableSegment.getAddress());
            }
            for (Take5EntryPoint ep : entryPoints) {
                int bucketCount = ep.bucketCount;
                Bucket[] bucketTable = ep.bucketTable;
                assert bucketTable != null;

                for (int bx = 0; bx < bucketCount; bx++) {
                    for (PerfhashKeyValuePair pair : bucketTable[bx].pairs) {
                        if (valueTableSegment != null) {
                            valueTableSegment.getIntBuffer().put(ix + pair.index, valueSegment.getAddress() + pair.value.address);
                        }
                        if (keySegment != null) {
                            if (keyFormat == KeyFormat.HASH_HASH32) {
                                keySegment.getIntBuffer().put(ix + pair.index, pair.keyHash);
                            } else {
                                assert keyFormat == KeyFormat.HASH_STRING;
                                keySegment.getIntBuffer().put(ix + pair.index, valueSegment.getAddress() + pair.key.address);
                            }
                        }
                    }
                    ix += ep.indexCount;
                }
                assert ix == globalIndexCount;
            }
        }
    }

    /**
     * Write a textual description of the automaton to a stream.
     *
     * @param writer where to write the description.
     */
    public void formatDescription(Writer writer) {
        PrintWriter out = new PrintWriter(writer);
        out.format("Entry points: (%d)%n", entryPoints.size());
        for (Take5EntryPoint ep : entryPoints) {
            out.format("  %s:%n", ep.name);
            out.format("    flags = 0x%X%n", ep.contentFlags);
            out.format("    versions = [%d, %d]%n", ep.contentMinVersion, ep.contentMaxVersion);
            out.format("    offset = %d%n", ep.indexOffset);
            out.format("    key count = %d%n", ep.keyCount);
            out.format("    state count = %d%n", ep.stateCount);
            out.format("    edge count = %d%n", ep.edgeCount);
            out.format("    accept count = %d%n", ep.acceptEdgeCount);
            out.format("    max matches = %d%n", ep.maxMatches);
            out.format("    max key length = %d%n", ep.maxKeyLength);
            out.format("    max value size = %d%n", ep.maxValueSize);
            out.format("    characters = [%s, %s]%n",
                       Utils.charString(ep.minCharacter),
                       Utils.charString(ep.maxCharacter));
            if (ep.acceptEmpty) {
                out.format("    start = *!%d%+d%n", ep.startState.id, ep.indexOffset);
            } else {
                out.format("    start = *:%d%+d%n", ep.startState.id, ep.indexOffset - 1);
            }
        }
        out.format("Global metrics:%n");
        out.format("  key count = %d%n", totalKeyCount);
        out.format("  state count = %d%n", stateCount);
        out.format("  edge count = %d%n", edgeCount);
        out.format("  accept count = %d%n", acceptEdgeCount);
        out.format("  max matches = %d%n", globalMaxMatches);
        out.format("  max key length = %d%n", globalMaxKeyLength);
        out.format("  max value size = %d%n", globalMaxValueSize);
        out.format("  characters = [%s, %s]%n",
                   Utils.charString(globalMinCharacter),
                   Utils.charString(globalMaxCharacter));
        out.format("States:%n");
        for (State st = lastState; st != null; st = st.previous) {
            out.format("%8d ", st.id);
            int nedges = st.edgeChar.length;
            int index = 0;
            for (int e = 0; e < nedges; e++) {
                State nst = st.edgeState[e];
                if (st.edgeIsAccept[e]) {
                    index++;
                    out.format(" %s!%d%+d", Utils.charString(st.edgeChar[e]), nst.id, index);
                } else {
                    out.format(" %s:%d%+d", Utils.charString(st.edgeChar[e]), nst.id, index);
                }
                index += nst.keyCount;
            }
            out.format("%n");
        }
        out.flush();
    }

    /**
     * Builder class; use this to construct new instances.
     */
    public static final class Factory {
        Engine engine = Engine.FSA;
        ValueFormat valueFormat = ValueFormat.STRING;
        KeyFormat keyFormat = KeyFormat.FSA;
        OutputFormat outputFormat = OutputFormat.TAKE5; // default is to write Take5.
        int valueSize = 2; // correct for STRING but not for IGNORE or INDEX, but main builder will cope.

        public Factory() {
            //
        }

        public Factory engine(Engine engine) {
            this.engine = engine;
            return this;
        }

        public Factory valueFormat(ValueFormat valueFormat) {
            this.valueFormat = valueFormat;
            return this;
        }

        public Factory keyFormat(KeyFormat keyFormat) {
            this.keyFormat = keyFormat;
            return this;
        }

        public Factory valueSize(int valueSize) {
            this.valueSize = valueSize;
            return this;
        }

        public Factory outputFormat(OutputFormat outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public Take5Builder build() throws Take5BuildException {

            if (engine == null) {
                throw new Take5BuildException("Engine must be specified.");
            }
            if (valueFormat == null) {
                throw new Take5BuildException("Mode must be specified.");
            }

            if (keyFormat == null && engine == Engine.FSA) {
                keyFormat = KeyFormat.FSA;
            } else if (keyFormat == null) {
                throw new Take5BuildException("Key format must not be null.");
            } else if (keyFormat == KeyFormat.FSA && engine != Engine.FSA) {
                throw new Take5BuildException("Key format must not be FSA for non-FSA engine.");
            }
            return new Take5Builder(this);
        }
    }
}
