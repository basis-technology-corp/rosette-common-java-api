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
 * Take5 runtime support. This package is the runtime API to reading Take5 files.
 * There are three classes to use here:
 * <ul>
 *     <li>
 *         {@link com.basistech.rosette.internal.take5.Take5Dictionary} represents the Take5 file itself.
 *         These are constructed over a byte buffer. Typically, we memory-map these, so (using Guava), the code
 *         looks like:
 * <pre>
 *         {@code
 *            MappedByteBuffer mappedDict = Files.map(dictFile, MapMode.READ_ONLY);
 *            dict = Take5Dictionary(mappedDict, mappedDict.capacity(), entryPoint);
 *         }
 * </pre>
 *     </li>
 *     <li>{@link com.basistech.rosette.internal.take5.Take5Match} is the data returned when doing a lookup
 *     in a Take5. While this class has a variety of accessors for payload data, generally applications
 *     end up calling {@link com.basistech.rosette.internal.take5.Take5Match#getOffsetValue()}. This returns
 *     an offset into the overall mapped dictionary. Applications then use the usual NIO functions to retrieve
 *     the payload. {@code com.basistech.rosette.internal.take5.Take5DictionaryTest#testGetOffsetValue()}
 *     is an example of a lookup that examines payload.
 *     </li>
 *     <li>{@link com.basistech.rosette.internal.take5.Take5Walker} is used with
 *     {@link com.basistech.rosette.internal.take5.Take5Dictionary#walk(Take5Walker, Take5Match, char[], int)}
 *     to iterate over all the keys in a Take5. This is supported for the FSA engine and the
 *     PERFHASH engine only when it is configured to store keys.
 *     </li>
 * </ul>
 * <br>
 * These classes will read Take5 files in either byte order. However, it is slower to read from files in the
 * 'wrong' byte order.
 */
package com.basistech.rosette.internal.take5;
