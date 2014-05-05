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

import au.com.bytecode.opencsv.CSVParser;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import org.apache.commons.io.FileUtils;
import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.FileOptionHandler;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.Setter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Command line interface for take5 builder.
 */
public final class Take5Build {

    private static final File NO_FILE = new File(".fnord.");

    public static class FileOrDashOptionHandler extends FileOptionHandler {

        public FileOrDashOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super File> setter) {
            super(parser, option, setter);
        }

        @Override
        protected File parse(String argument) throws CmdLineException {
            if ("-".equals(argument)) {
                return NO_FILE;
            } else {
                return super.parse(argument);
            }
        }

        @Override
        public String getDefaultMetaVariable() {
            return "INPUT_FILE_OR_-";
        }
    }

    public static class ByteOrderOptionHandler extends OneArgumentOptionHandler<ByteOrder> {
        public ByteOrderOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super ByteOrder> setter) {
            super(parser, option, setter);
        }

        @Override
        protected ByteOrder parse(String argument) throws NumberFormatException, CmdLineException {
            if ("LE".equalsIgnoreCase(argument)) {
                return ByteOrder.LITTLE_ENDIAN;
            } else if ("BE".equalsIgnoreCase(argument)) {
                return ByteOrder.BIG_ENDIAN;
            } else {
                throw new CmdLineException("Invalid byte order " + argument);
            }
        }
    }

    @Argument(handler = FileOrDashOptionHandler.class, usage = "input file or - for standard input", metaVar = "INPUT")
    File commandInputFile;

    @Option(name = "-help")
    boolean help;

    @Option(name = "-output", metaVar = "OUTPUT_FILE", handler = FileOrDashOptionHandler.class,
            usage = "output file")
    File outputFile;

    @Option(name = "-metadata", usage = "metadata content")
    File metadataFile;

    @Option(name = "-copyright", metaVar = "COPYRIGHT_FILE",
            usage = "File containing a copyright notice")
    File copyrightFile;

    @Option(name = "-engine", metaVar = "ENGINE",
            usage = "lookup engine (FSA or PERFHASH).")
    Engine engine = Engine.FSA;

    @Option(name = "-key-format", metaVar = "FORMAT", usage = "Key format; only useful with -engine PERFHASH")
    KeyFormat keyFormat;

    @Option(name = "-join", metaVar = "CONTROL_FILE", usage = "Combine multiple Take5's into one output.")
    File controlFile;

    @Option(name = "-binaryPayloads", metaVar = "ALIGNMENT",
            usage = "payload size/alignment")
    Integer alignment;

    @Option(name = "-defaultMode", metaVar = "MODE",
            usage = "default payload mode (e.g. #4f).")
    String defaultPayloadFormat;

    // payloads are in the file, but we ignore them.
    @Option(name = "-ignore-payloads", usage = "expect payloads in input file but ignore them")
    boolean ignorePayloads;

    // no payloads in the input _at all_
    @Option(name = "-no-payloads", usage = "input file is just keys")
    boolean noPayloads;

    //This is the inverse of -q.
    @Option(name = "-simpleKeys", usage = "simple keys; no escapes")
    boolean simpleKeys;

    @Option(name = "-entrypoint", metaVar = "NAME", usage = "entrypoint (default 'main')")
    String entrypointName;

    @Option(name = "-contentVersion", metaVar = "VERSION",
            usage = "version of content")
    int version;

    @Option(name = "-indexLookup", usage = "lookups map from key to (sorted) key indices; no stored payloads.")
    boolean indexLookup;

    @Option(name = "-debug-dump-lookup", usage = "write textual lookup dump.")
    boolean dumpLookup;

    @Option(name = "-content-flags", metaVar = "FLAGS", usage = "abstruse content flags")
    int contentFlags;

    @Option(name = "-no-output", usage = "No output at all.")
    boolean noOutput;

    @Option(name = "-byteOrder", usage = "byte order (LE or BE)", metaVar = "ORDER", handler = ByteOrderOptionHandler.class)
    ByteOrder byteOrder = ByteOrder.nativeOrder();

    private List<InputSpecification> inputSpecifications;
    private Take5Builder builder;

    static class Failure extends Exception {
        Failure() {
        }

        Failure(String message) {
            super(message);
        }

        Failure(String message, Throwable cause) {
            super(message, cause);
        }

        Failure(Throwable cause) {
            super(cause);
        }
    }

    Take5Build() {
        //
    }

    public static void main(String[] args) {
        Take5Build that = new Take5Build();
        CmdLineParser parser = new CmdLineParser(that);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        if (that.help) {
            parser.printUsage(System.out);
            return;
        }

        try {
            that.checkOptionConsistency();
            ThreadMXBean tm = ManagementFactory.getThreadMXBean();
            long cpuTime = tm.getCurrentThreadCpuTime();
            that.build();
            long endCpuTime = tm.getCurrentThreadCpuTime();

            long millistart = TimeUnit.MILLISECONDS.convert(cpuTime, TimeUnit.NANOSECONDS);
            long milliend = TimeUnit.MILLISECONDS.convert(endCpuTime, TimeUnit.NANOSECONDS);
            Duration duration = new Duration(millistart, milliend);
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                    .appendDays()
                    .appendSuffix("d")
                    .appendHours()
                    .appendSuffix("h")
                    .appendMinutes()
                    .appendSuffix("m")
                    .appendSecondsWithMillis()
                    .appendSuffix("s")
                    .toFormatter();
            String formatted = formatter.print(duration.toPeriod());
            System.out.println(formatted);
        } catch (Failure failure) {
            System.err.println(failure.getMessage());
            System.exit(1);
        }
    }

    void checkOptionConsistency() throws Failure {
        if (controlFile != null) {
            try {
                inputSpecifications = new ControlFile().read(Files.asCharSource(controlFile, Charsets.UTF_8));
            } catch (IOException e) {
                throw new Failure(String.format("Failed to read control file %s.", controlFile.getAbsolutePath()), e);
            } catch (InputFileException e) {
                throw new Failure(String.format("Error in control file %s: %s.", controlFile.getAbsolutePath(),
                        e.getMessage()), e);
            }
        } else {
            inputSpecifications = Lists.newArrayList();
            InputSpecification spec = new InputSpecification();
            spec.contentFlags = contentFlags;
            spec.minVersion = version;
            spec.maxVersion = version;
            spec.defaultMode = defaultPayloadFormat;
            spec.entrypointName = entrypointName;
            spec.inputFile = commandInputFile;
            spec.simpleKeys = simpleKeys;
            spec.noPayloads = noPayloads;
            spec.ignorePayloads = ignorePayloads;
            inputSpecifications.add(spec);
        }

        if ((outputFile == null || outputFile == NO_FILE) && !dumpLookup) {
            throw new Failure("You must provide an output file unless you specify -debug-dump-lookup");
        }

        if (engine == Engine.FSA) {
            if (keyFormat == null) {
                keyFormat = KeyFormat.FSA;
            } else if (keyFormat != KeyFormat.FSA) {
                throw new Failure(String.format("Invalid key format %s for FSA engine.", keyFormat.name()));
            }
        } else {
            // perfhash
            if (keyFormat == null) {
                keyFormat = KeyFormat.HASH_STRING;
            } else if (keyFormat == KeyFormat.FSA) {
                throw new Failure(String.format("Invalid key format %s for PERFHASH engine.", keyFormat.name()));
            }
        }
    }

    void build() throws Failure {
        Take5Builder.Factory factory = new Take5Builder.Factory();
        // our default is to write something.
        factory.outputFormat(OutputFormat.TAKE5);

        /*
         * Value Format
         *  indexLookup -> ValueFormat.INDEX
         *   ... or if noPayloads or ignorePayloads, IGNORE.
         *  binary payloads (alignment != 0) -> ValueFormat.PTR
         */

        if (noOutput) {
            factory.outputFormat(OutputFormat.NONE);
        } else if (dumpLookup) {
            factory.outputFormat(OutputFormat.FSA);
        } else if (indexLookup) {
            factory.valueFormat(ValueFormat.INDEX);
        } else if (alignment != null) {
            factory.valueFormat(ValueFormat.PTR);
            if (alignment < 2) {
                alignment = 2;
            }
            factory.valueSize(alignment);
        } else if (noPayloads || ignorePayloads) {
            factory.valueFormat(ValueFormat.IGNORE);
        } else {
            factory.valueFormat(ValueFormat.IGNORE);
        }

        try {
            builder = factory.engine(engine).keyFormat(keyFormat).build();
        } catch (Take5BuildException e) {
            throw new Failure(e);
        }

        if (byteOrder != ByteOrder.nativeOrder()) {
            builder.setByteOrder(byteOrder);
        }

        copyright();
        metadata();

        for (InputSpecification spec : inputSpecifications) {
            String name = spec.entrypointName;
            if (name == null) {
                name = "main";
            }
            Take5EntryPoint entrypoint;
            try {
                entrypoint = builder.newEntryPoint(name);
            } catch (Take5BuildException e) {
                throw new Failure(e);
            }
            oneEntrypoint(spec, entrypoint);
        }

        try {
            if (dumpLookup) {
                builder.formatDescription(new OutputStreamWriter(System.out, Charsets.UTF_8));
            } else {
                builder.buildToSink(Files.asByteSink(outputFile));
            }
        } catch (IOException e) {
            throw new Failure("Failed to write output " + outputFile.getAbsolutePath());
        }
    }

    private void oneEntrypoint(InputSpecification spec, Take5EntryPoint entrypoint) throws Failure {
        CharSource source;
        if (spec.inputFile == NO_FILE) {
            source = new StdinByteSource().asCharSource(Charsets.UTF_8);
        } else {
            source = Files.asCharSource(spec.inputFile, Charsets.UTF_8);
        }
        InputFile inputFile = new InputFile();
        inputFile.setSimpleKeys(spec.simpleKeys);
        inputFile.setPayloads(!spec.noPayloads);
        inputFile.setIgnorePayloads(spec.ignorePayloads);
        try {
            inputFile.read(source); // pull the whole thing into memory.
        } catch (IOException e) {
            throw new Failure("IO error reading " + (spec.inputFile == NO_FILE ? "standard input" : spec.inputFile.getAbsolutePath()));
        } catch (InputFileException e) {
            throw new Failure(String.format("Format error reading %s: %s",
                    spec.inputFile == NO_FILE ? "standard input" : spec.inputFile.getAbsolutePath(),
                    e.getCause().getMessage()));
        }
        try {
            entrypoint.loadContent(inputFile.getPairs().iterator());
        } catch (Take5BuildException e) {
            throw new Failure(e);
        }
    }

    private void metadata() throws Failure {
        if (metadataFile != null) {
            CharSource metaSource = Files.asCharSource(metadataFile, Charsets.UTF_8);
            try {
                builder.setMetadata(metaSource.readLines(new LineProcessor<Map<String, String>>() {
                    private final Map<String, String> results = Maps.newHashMap();
                    private final CSVParser parser = new CSVParser('\t');

                    @Override
                    public boolean processLine(String line) throws IOException {
                        String[] tokens = parser.parseLine(line);
                        results.put(tokens[0], tokens[1]);
                        return true;
                    }

                    @Override
                    public Map<String, String> getResult() {
                        return results;
                    }
                }));
            } catch (IOException e) {
                throw new Failure("Failed to read metadata" + metadataFile.getAbsolutePath(), e);
            } catch (Take5BuildException e) {
                throw new Failure("Error in metadata " + metadataFile.getAbsolutePath(), e);
            }
        }
    }

    private void copyright() throws Failure {
        if (copyrightFile != null) {
            try {
                builder.setCopyright(FileUtils.readFileToString(copyrightFile, "UTF-8"));
            } catch (IOException e) {
                throw new Failure("Failed to read copyright file " + copyrightFile.getAbsolutePath());
            }
        }
    }
}
