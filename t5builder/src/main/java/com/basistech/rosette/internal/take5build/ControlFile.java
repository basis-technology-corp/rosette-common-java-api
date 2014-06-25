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

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.LineProcessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Control file parsing for multiple entrypoints.
 */
public class ControlFile {


    class Processor implements LineProcessor<List<InputSpecification>> {
        private int lineNumber;
        private List<InputSpecification> results = Lists.newArrayList();

        @Override
        public boolean processLine(String line) throws IOException {
            if (line.startsWith("#")) {
                return true;
            }

            int eqidx = line.indexOf('=');
            if (eqidx < 1) {
                throw Throwables.propagate(new InputFileException("No key on line " + lineNumber));
            }
            String key = line.substring(0, eqidx);
            String value = line.substring(eqidx + 1);

            InputSpecification spec = new InputSpecification();

            /*
             * Where I<kind> is C<0> for no escapes, C<1> for backslash escape sequences
             * (see B<-q>), or C<2> for sortable escape sequences (see B<-u>).  If B<-q>
             * or B<-u> were given on the command line, this defaults to that setting,
             * otherwise it defaults to no escapes.
             */
            if ("ESCAPE".equalsIgnoreCase(key)) {
                if ("0".equals(value) || "none".equalsIgnoreCase(value)) {
                    spec.simpleKeys = true;
                } else if ("1".equals(value) || "backslash".equalsIgnoreCase(value)) {
                    // OK as we are
                } else if ("2".equals(value)) {
                    // obsolete
                    throw Throwables.propagate(new InputFileException(String.format("Backslash escapes are not supported at line %d", lineNumber)));
                } else {
                    throw Throwables.propagate(new InputFileException(String.format("Unsupported ESCAPE value %s at line %d", value, lineNumber)));
                }
            } else if ("MIN_VERSION".equalsIgnoreCase(key)) {
                spec.minVersion = Integer.parseInt(value);
            } else if ("MAX_VERSION".equalsIgnoreCase(key)) {
                spec.maxVersion = Integer.parseInt(value);
            } else if ("NAME".equalsIgnoreCase(key)) {
                spec.entrypointName = value;
            } else if ("PATH".equalsIgnoreCase(key)) {
                spec.inputFile = new File(value);
            } else if ("VALUE_MODE".equalsIgnoreCase(key)) {
                spec.defaultMode = value;
            } else if ("VERSION".equalsIgnoreCase(key)) {
                spec.minVersion = Integer.parseInt(value);
                spec.maxVersion = spec.minVersion;
            } else if ("FLAGS".equalsIgnoreCase(key)) {
                if (value.startsWith("0x") || value.startsWith("0X")) {
                    spec.contentFlags = Integer.parseInt(value.substring(2), 16);
                } else {
                    spec.contentFlags = Integer.parseInt(value);
                }
            } else {
                throw Throwables.propagate(new InputFileException(String.format("Unsupported specification %s at line %d", key, lineNumber)));
            }
            results.add(spec);
            lineNumber++;
            return true;
        }

        @Override
        public List<InputSpecification> getResult() {
            return results;
        }
    }

    List<InputSpecification> read(CharSource source) throws IOException, InputFileException {
        try {
            return source.readLines(new Processor());
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InputFileException) {
                throw (InputFileException) e.getCause();
            }
            throw e;
        }
    }
}
