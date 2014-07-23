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

import com.google.common.collect.Maps;

import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.Map;

/**
 * Builder class; use this to construct new instances of {@link com.basistech.rosette.internal.take5build.Take5Builder}.
 */
public final class Take5BuilderFactory {
    Engine engine = Engine.FSA;
    ValueFormat valueFormat = ValueFormat.PTR;
    KeyFormat keyFormat = KeyFormat.FSA;
    OutputFormat outputFormat = OutputFormat.TAKE5; // default is to write Take5.
    int valueSize = 2; // correct for STRING but not for IGNORE or INDEX, but main builder will cope.
    PrintWriter progressWriter = new PrintWriter(System.out, true);
    String copyright;
    ByteOrder byteOrder = ByteOrder.nativeOrder();
    Map<String, String> metadata;

    public Take5BuilderFactory() {
        metadata = Maps.newHashMap();
    }

    /**
     * Specify the engine; default is {@link Engine#FSA}.
     * @param engine the engine
     * @return this
     */
    public Take5BuilderFactory engine(Engine engine) {
        this.engine = engine;
        return this;
    }

    /**
     * Specify the value format; the default is {@link ValueFormat#PTR}.
     * @param valueFormat the format.
     * @return this.
     */
    public Take5BuilderFactory valueFormat(ValueFormat valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    /**
     * Specify the key format. The default is {@link KeyFormat#FSA}; you must select
     * another value when you use {@link Engine#PERFHASH}.
     * @param keyFormat the key format.
     * @return this
     */
    public Take5BuilderFactory keyFormat(KeyFormat keyFormat) {
        this.keyFormat = keyFormat;
        return this;
    }

    /**
     * Specify the size and alignment of payload items. The default is 2.
     * This value is only important for Take5 dictionaries that will be read by C or C++ code.
     * In C and C++, this ensures that each payload item is aligned on the correct natural
     * item, following the rule that a POD must be aligned to its size. It must be a multiple
     * of the largest value of any alignment value specified for any payload value; see
     * {@link Take5Pair#getAlignment()}.
     * @param valueSize the size
     * @return this
     */
    public Take5BuilderFactory valueSize(int valueSize) {
        this.valueSize = valueSize;
        return this;
    }

    /**
     * Specify what is written then the builder builds. The default is
     * {@link com.basistech.rosette.internal.take5build.OutputFormat#TAKE5}. The other values
     * are for debugging and testing.
     * @param outputFormat the format
     * @return this
     */
    public Take5BuilderFactory outputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    /**
     * Specify a writer to be called with text describing the progress of the build, especially
     * for PERFHASH. The default is to write to {@link System#out}.
     * @param progressWriter The writer.
     * @return this.
     */
    public Take5BuilderFactory progressWriter(PrintWriter progressWriter) {
        this.progressWriter = progressWriter;
        return this;
    }

    /**
     * Specify a copyright notice for the Take5.
     * @param copyright the notice
     * @return this
     */
    public Take5BuilderFactory copyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    /**
     * Specify the target byte order. Note that it is up to the application to ensure
     * that payloads are in the correct byte order. The default is {@link java.nio.ByteOrder#nativeOrder()}.
     * @param order the order
     * @return this
     */
    public Take5BuilderFactory order(ByteOrder order) {
        this.byteOrder = order;
        return this;
    }

    /**
     * Add a key-value pair to the metadata.
     * @param key key
     * @param value value
     * @return this
     */
    public Take5BuilderFactory putMetadata(String key, String value) {
        metadata.put(key, value);
        return this;
    }

    /**
     * Specify the entire metadata as a map.
     * @param metadata the metadata
     * @return this
     */
    public Take5BuilderFactory metadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Create the {@link com.basistech.rosette.internal.take5build.Take5Builder}.
     * @return the builder
     * @throws Take5BuildException for impossible or inconsistent options.
     */
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
