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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Scanner implements Iterator<Take5Pair> {
    String inputName;
    int keyNumber;
    BufferedReader in;
    char[] next;
    ReusableTake5Pair pair;
    char[] keybuf;
    byte[] valbuf;
    int valposition;
    int vallimit;
    ByteBuffer bytebuf;
    CharBuffer charbuf;

    public Scanner(BufferedReader source, String inputName) {
        in = source;
        this.inputName = inputName;
        keyNumber = 0;
        advance();
        keybuf = new char[100];
        valposition = 0;
        vallimit = 800;   // XXX multiple of 8
        valbuf = new byte[vallimit];
        bytebuf = ByteBuffer.wrap(valbuf);
        bytebuf.order(ByteOrder.nativeOrder()); // XXX caller should control
        charbuf = bytebuf.asCharBuffer();
        pair = new ReusableTake5Pair(keybuf);
    }

    private void advance() {
        try {
            String s = in.readLine();
            if (s == null) {
                next = null;
            } else {
                next = s.toCharArray();
            }
            
        } catch (IOException e) {
            // You might consider doing
            //    throw new Take5ParseError(e.toString(), inputName, keyNumber);
            // here, but the kind of IOExceptions that are most likely to
            // happen are things like file system errors that the user
            // probably can't handle...
            throw new Take5BuilderException(e);
        }
    }

    public boolean hasNext() {
        return next != null;
    }

    public Take5Pair next() {
        if (next == null) {
            throw new NoSuchElementException();
        }
        // XXX In order to deal with the various kinds of key escaping and
        // payload parsing, we would be better off just parsing our own
        // lines out of a char[] buffer.  We'll make that change soon.

        // XXX Note that the key in a ReusableTake5Pair can be reused as well as
        // the pair itself!
        int len = next.length;
        pair.key = next;
        pair.keyLength = len;
        pair.value = null;
        for (int i = 0; i < len; i++) {
            char c = next[i];
            if (c == '\t') {
                pair.keyLength = i;

                valposition = Utils.alignUp(valposition, 2);
                charbuf.position(valposition / 2);
                charbuf.put(next, i + 1, len - i - 1);
                charbuf.put((char)0);
                int newposition = 2 * charbuf.position();
                pair.setValue(valbuf, 2, valposition, newposition - valposition);
                valposition = newposition;
                break;
            }
        }
        keyNumber++;
        advance();
        return pair;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
