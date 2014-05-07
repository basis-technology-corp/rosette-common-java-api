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

import java.io.PrintWriter;

/**
 * Builder class; use this to construct new instances.
 */
public final class Take5BuilderFactory {
    Engine engine = Engine.FSA;
    ValueFormat valueFormat = ValueFormat.PTR;
    KeyFormat keyFormat = KeyFormat.FSA;
    OutputFormat outputFormat = OutputFormat.TAKE5; // default is to write Take5.
    int valueSize = 2; // correct for STRING but not for IGNORE or INDEX, but main builder will cope.
    PrintWriter progressWriter = new PrintWriter(System.out, true);

    public Take5BuilderFactory() {
        //
    }

    public Take5BuilderFactory engine(Engine engine) {
        this.engine = engine;
        return this;
    }

    public Take5BuilderFactory valueFormat(ValueFormat valueFormat) {
        this.valueFormat = valueFormat;
        return this;
    }

    public Take5BuilderFactory keyFormat(KeyFormat keyFormat) {
        this.keyFormat = keyFormat;
        return this;
    }

    public Take5BuilderFactory valueSize(int valueSize) {
        this.valueSize = valueSize;
        return this;
    }

    public Take5BuilderFactory outputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
        return this;
    }

    public Take5BuilderFactory progressWriter(PrintWriter progressWriter) {
        this.progressWriter = progressWriter;
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
