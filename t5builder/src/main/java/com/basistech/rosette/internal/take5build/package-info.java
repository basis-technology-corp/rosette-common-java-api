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

/**
 * Implementation of 100%-Java Take5 builder.
 * <p>
 * The central class in building a Take5 is {@link com.basistech.rosette.internal.take5build.Take5Builder}.
 * Applications do not construct instances of {@link com.basistech.rosette.internal.take5build.Take5Build}
 * directly. Instead, they use {@link com.basistech.rosette.internal.take5build.Take5BuilderFactory}.
 * The factory provides a fluent API that allows for the many options that control the Take5 process.
 * Given a {@link com.basistech.rosette.internal.take5build.Take5Builder}, the application defines one or more
 * entrypoints. For each entrypoint, the application supplies the actual data (keys and values) in the form
 * of an iterator that returns objects that implement {@link com.basistech.rosette.internal.take5build.Take5Pair}.
 * </p>
 * <h1>Byte Order</h1>
 * This package can build Take5's optimized for either byte order. The Java runtime can read in either order;
 * the C++ runtime can only read in the native byte order.
 * <h1>Settings Options on the Factory</h1>
 * <p/>
 * <ul>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#engine(Engine)} takes an item from
 *  {@link com.basistech.rosette.internal.take5build.Engine}. This specifies whether lookups will use
 *  the FSA engine (which is slower, but supports substring matches) or the perfect hash engine,
 *  which is faster, but only supports exact matches.</li>
 *  <li>
 *      {@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#valueFormat(ValueFormat)}
 *      specifies whether there will be any payload. {@link com.basistech.rosette.internal.take5build.ValueFormat#IGNORE}
 *      builds a file with no payload; the only information is whether the key is present.
 *      {@link com.basistech.rosette.internal.take5build.ValueFormat#INDEX} stores only the ordinal index of the
 *      key. {@link com.basistech.rosette.internal.take5build.ValueFormat#PTR} stores payload data.
 *  </li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#valueSize(int)}.
 *  Practically, specify an alignment for payload data. See the description for more details.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#keyFormat(KeyFormat)}
 *  specifies the key structure for {@link com.basistech.rosette.internal.take5build.Engine#PERFHASH}.
 *  {@link com.basistech.rosette.internal.take5build.KeyFormat#HASH_STRING} stores copies of all the keys,
 *  guaranteeing an exact match at the cost of the comparison. {@link com.basistech.rosette.internal.take5build.KeyFormat#HASH_HASH32}
 *  stores a hash of the key. This almost always delivers the right answer, and reduced the cost to a single comparison.
 *  {@link com.basistech.rosette.internal.take5build.KeyFormat#HASH_NONE} stores nothing at all; it is the fastest, but has
 *  the highest chance of a false positive match.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#valueFormat(ValueFormat)}
 *  is very confusing. It has to be divisible by the largest alignment specified in any of the
 *  {@link com.basistech.rosette.internal.take5build.Take5Pair} items.
 *  </li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#outputFormat(OutputFormat)} defines what
 *  the builder writes. The default, {@link com.basistech.rosette.internal.take5build.OutputFormat#TAKE5},
 *  is to write a usable file. {@link com.basistech.rosette.internal.take5build.OutputFormat#FSA} writes
 *  a textual representation of an FSA lookup engine. {@link com.basistech.rosette.internal.take5build.OutputFormat#NONE}
 *  writes nothing at all.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#progressWriter(java.io.PrintWriter)}
 *  specifies a print writer that is called with information about the build process.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#order(java.nio.ByteOrder)} specifies
 *  the output byte order. Note that this only effects the lookup engine; the application is responsible for the
 *  byte order of any payload data.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#metadata(java.util.Map)} and
 *  {@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#putMetadata(String, String)} specify file-level
 *  metadata.</li>
 *  <li>{@link com.basistech.rosette.internal.take5build.Take5BuilderFactory#copyright(String)} provides a
 *  copywright string.</li>
 * </ul>
 * <h1>Example of Building a Builder</h1>
 * <pre>
 *         {@code
 *         factory = new Take5BuilderFactory().progressWriter(new PrintWriter(new NullWriter()));
 *         builder = factory.build();
 * }
 * </pre>
 * <h1>Data</h1>
 * Once you have an instance of {@link com.basistech.rosette.internal.take5build.Take5Builder}, you load it with data.
 * A Take5 can have multiple entrypoints. You load each entrypoint with key-value pairs. The builder detects sharable
 * values, even across entrypoints, so value data is not duplicated. If you are only creating one entrypoint, you should
 * name it <tt>main</tt>.
 * <p/>
 * You call {@link com.basistech.rosette.internal.take5build.Take5Builder#newEntryPoint(String)} for each entrypoint.
 * You call {@link com.basistech.rosette.internal.take5build.Take5EntryPoint#loadContent(java.util.Iterator)}
 * to load the content. The content is defined as an {@link java.util.Iterator} that returns instances of
 * {@link com.basistech.rosette.internal.take5build.Take5Pair}.
 * For {@link com.basistech.rosette.internal.take5build.Engine#FSA}, the iterator must return the pairs in
 * the lexicographic order of the keys.
 * <h1>Bulding the File</h1>
 * {@link com.basistech.rosette.internal.take5build.Take5Builder} provides three methods that build the results.
 * <ul>
 *     <li>{@link com.basistech.rosette.internal.take5build.Take5Builder#buildArray()} creates the Take5 as a
 *     byte array. This isn't very useful.</li>
 *     <li>{@link com.basistech.rosette.internal.take5build.Take5Builder#buildBuffer()} creates the Take5
 *     as a {@link java.nio.ByteBuffer}. You can construct a {@link com.basistech.rosette.internal.take5.Take5Dictionary}
 *     with this.</li>
 *     <li>{@link com.basistech.rosette.internal.take5build.Take5Builder#buildToSink(com.google.common.io.ByteSink)}
 *     writes the Take5 to an output stream obtained from a Guava {@link com.google.common.io.ByteSink}.</li>
 * </ul>
 * <h1>Examples</h1>
 * See com.basistech.rosette.internal.take5build.examples in src/test/java for example programs.
 */
package com.basistech.rosette.internal.take5build;