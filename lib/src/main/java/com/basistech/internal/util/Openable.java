/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2013 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.internal.util;

import java.io.IOException;
import java.io.Reader;

/**
 * Something that can be opened to yield a {@link Reader}.  An Openable is almost the same as a
 * {@link java.util.concurrent.Callable}&lt;{@link Reader}&gt;, except an Openable can only
 * throw an IOException, while a Callable&lt;Reader&gt; can throw any Exception whatsoever.
 */
public interface Openable {
    /**
     * Return a {@link Reader}.
     * @return a Reader
     * @throws IOException can't read.
     */
    Reader open() throws IOException;
}
