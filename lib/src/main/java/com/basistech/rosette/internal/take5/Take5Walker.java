/******************************************************************************
 ** This data and information is proprietary to, and a valuable trade secret
 ** of, Basis Technology Corp.  It is given in confidence by Basis Technology
 ** and may only be used as permitted under the license agreement under which
 ** it has been distributed, and in no other way.
 **
 ** Copyright (c) 2000-2008 Basis Technology Corporation All rights reserved.
 **
 ** The technical data and information provided herein are provided with
 ** `limited rights', and the computer software provided herein is provided
 ** with `restricted rights' as those terms are defined in DAR and ASPR
 ** 7-104.9(a).
 ******************************************************************************/

package com.basistech.rosette.internal.take5;

/**
 * Interface for walking the states of a Take5.  For use with {@link
 * Take5Dictionary#walk Take5Dictionary.walk}.
 */
public interface Take5Walker {

    /**
     * A match was found within the given depth of the starting state.
     * <P>
     * Note that if you wish to use the value of <CODE>match</CODE> after
     * this method call has returned, you must make a copy of it yourself,
     * for it will be reused.
     *
     * @param match a Take5Match for a matched word
     * @param buffer the same buffer that was passed to walk
     * @param buflen the same length that was passed to walk
     */
    void foundAccept(Take5Match match, char[] buffer, int buflen);

    /**
     * A state at the depth limit of the walk was reached, and there are
     * additional states beyond it.  The <CODE>match</CODE> and
     * <CODE>buffer</CODE> are such that you could pass them to {@link
     * Take5Dictionary#walk Take5Dictionary.walk} right now (with a larger
     * value for <CODE>buflen</CODE>) in order to explore beyond this
     * state.
     * <P>
     * Note that if you wish to use the value of <CODE>match</CODE> after
     * this method call has returned, you must make a copy of it yourself,
     * for it will be reused.
     *
     * @param match a Take5Match for further exploration
     * @param buffer the same buffer that was passed to walk
     * @param buflen the same length that was passed to walk
     */
    void foundLimit(Take5Match match, char[] buffer, int buflen);

    /**
     * A match was found <EM>and</EM> we are at the depth limit and there
     * are additional states beyond.  Both the description of {@link
     * #foundAccept foundAccept} and {@link #foundLimit foundLimit} apply.
     *
     * @param match a Take5Match for both a matched word <EM>and</EM> for further exploration
     * @param buffer the same buffer that was passed to walk
     * @param buflen the same length that was passed to walk
     */
    void foundBoth(Take5Match match, char[] buffer, int buflen);
}
