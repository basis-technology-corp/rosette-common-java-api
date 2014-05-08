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

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.primitives.UnsignedInts;
import com.google.common.primitives.UnsignedLongs;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Given a sequence of lexical tokens for a single payload, produce a ByteBuffer containing the goods.
 * Make a new instance of this class (via the static method) for each payload.
 */
public final class PayloadParser {

    static class Mode {
        int intLength = 4;
        boolean intSigned = true;
        int floatLength = 8;
        int charlength = 2;
        boolean stringsNullTerminated = true;
    }

    // this could be replaced by making bigger and bigger buffers if we want to support Really Big payloads.
    static final int DEFAULT_MAXIMUM_PAYLOAD_BYTES = 16384; // is the enough?
    private static final int FLOAT_LENGTH = 4;
    private static final int DOUBLE_LENGTH = 8;

    private Mode currentMode = new Mode();
    private int temporaryModeLength = -1;
    private char temporaryModeChar;
    private int used;
    private int alignment = 1;

    private ByteBuffer buffer;
    private CharBuffer charView;
    private ShortBuffer shortView;
    private IntBuffer intView;
    private LongBuffer longView;
    private FloatBuffer floatView;
    private DoubleBuffer doubleView;

    private PayloadParser() {
        this(DEFAULT_MAXIMUM_PAYLOAD_BYTES, new Mode());
    }

    private PayloadParser(int maxSize, Mode initialMode) {
        buffer = ByteBuffer.allocate(maxSize);
        currentMode = initialMode;
        charView = buffer.asCharBuffer();
        shortView = buffer.asShortBuffer();
        intView = buffer.asIntBuffer();
        longView = buffer.asLongBuffer();
        floatView = buffer.asFloatBuffer();
        doubleView = buffer.asDoubleBuffer();
    }

    private PayloadParser(int maxSize, String initialModeString) throws PayloadParserException {
        buffer = ByteBuffer.allocate(maxSize);
        if (initialModeString != null) {
            parseMode(initialModeString, 0);
        }
        charView = buffer.asCharBuffer();
        shortView = buffer.asShortBuffer();
        intView = buffer.asIntBuffer();
        longView = buffer.asLongBuffer();
        floatView = buffer.asFloatBuffer();
        doubleView = buffer.asDoubleBuffer();
    }

    Payload parse(String text) throws PayloadParserException {
        PayloadLexer lexer = new PayloadLexer(text);
        PayloadToken token;
        try {
            while ((token = lexer.lex()) != null) {
                boolean resetTempMode = process(token);
                if (resetTempMode) {
                    temporaryModeLength = -1;
                    temporaryModeChar = 0;
                }
            }
        } catch (PayloadLexerException e) {
            throw new PayloadParserException("Lexical error", e, e.getColumn());
        }
        buffer.position(0);
        byte[] results = new byte[used];
        buffer.get(results);
        return new Payload(results, alignment);
    }

    private boolean process(PayloadToken token) throws PayloadParserException {
        switch (token.type) {
        case STRING:
            processString(token);
            return true;
        case MODE:
            processMode(token);
            return false;
        case INTNUM:
            processIntnum(token);
            return true;
        case FLONUM:
            processFlonum(token);
            return true;
        case HEXDUMPSTART:
            processHexStart(token);
            return true;
        case HEXDUMPITEM:
            processHexItem(token);
            return true;
        case HEXDUMPEND:
            processHexEnd(token);
            return true;
        default:
            throw new RuntimeException("checkstyle is an idiot;");
        }
    }

    private void checkAlignment(int required, int column) throws PayloadParserException {
        if ((used % required) != 0) {
            throw new PayloadParserException(String.format("Invalid alignment: item size %d but current offset %d", required, used), column);
        }
        // note the largest alignment we use
        alignment = Math.max(alignment, required);
    }

    private void processHexEnd(PayloadToken token) {
        // nothing needed.
    }

    private void processHexItem(PayloadToken token) throws PayloadParserException {
        if (0 != (token.text.length() % 2)) {
            throw new PayloadParserException("Odd-length hexdump item " + token.text, token.column);
        }
        switch (token.text.length()) {
        case 2:
            appendByte(UnsignedBytes.parseUnsignedByte(token.text, 16), token.column);
            break;
        case 4:
            appendShort(parseUnsignedShort(token.text, 16, token.column), token.column);
            break;
        case 8:
            appendInt(UnsignedInts.parseUnsignedInt(token.text, 16), token.column);
            break;
        case 16:
            appendLong(UnsignedLongs.parseUnsignedLong(token.text, 16), token.column);
            break;
        default:
            throw new PayloadParserException("Too-long hexdump item " + token.text, token.column);
        }

    }

    private short parseUnsignedShort(String text, int radix, int column) throws PayloadParserException {
        int intVal = UnsignedInts.parseUnsignedInt(text, radix);
        if (UnsignedInts.compare(intVal, 0xffff) > 0) {
            throw new PayloadParserException(String.format("%s is too large for an unsigned short.", text), column);
        }
        return (short)(intVal & 0xffff);
    }

    private void appendLong(long l, int column) throws PayloadParserException {
        checkAlignment(8, column);
        longView.put(used / 8, l);
        used += 8;
    }

    private void appendInt(int l, int column) throws PayloadParserException {
        checkAlignment(4, column);
        intView.put(used / 4, (int)l);
        used += 4;

    }

    private void appendByte(byte l, int column) {
        buffer.put(used, (byte)(l & 0xff));
        used++;
    }

    private void appendShort(short l, int column) throws PayloadParserException {
        checkAlignment(2, column);
        shortView.put(used / 2, (short)(l & 0xffff));
        used += 2;
    }

    private void processHexStart(PayloadToken token) {
        //
    }

    private void processFlonum(PayloadToken token) throws PayloadParserException {
        int length;
        if (temporaryModeLength > 0) {
            length = temporaryModeLength;
        } else {
            length = currentMode.floatLength;
        }

        checkAlignment(length, token.column);
        if (length == FLOAT_LENGTH) {
            final float f;
            if ("inf".equals(token.text)) {
                f = Float.POSITIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else if ("+inf".equals(token.text)) {
                f = Float.POSITIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else if ("-inf".equals(token.text)) {
                f = Float.NEGATIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else {
                f = Float.parseFloat(token.text);
            }
            floatView.put(used / FLOAT_LENGTH, f);
        } else if (length == DOUBLE_LENGTH) {
            final double d;
            if ("inf".equals(token.text)) {
                d = Double.POSITIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else if ("+inf".equals(token.text)) {
                d = Double.POSITIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else if ("-inf".equals(token.text)) {
                d = Double.NEGATIVE_INFINITY; // I think this is the only case in the current code, but just in case ...
            } else {
                d = Double.parseDouble(token.text);
            }
            doubleView.put(used / DOUBLE_LENGTH, d);
        } else {
            throw new PayloadParserException("Invalid float length " + length, token.column);
        }

        used += currentMode.floatLength;
    }

    // should the lexer have given us a hint, here?
    private int getRadix(String text, int cur, int[] used) {
        int length = text.length() - cur;
        used[0] = 0;
        if (length <= 2) {
            return 10; // default radix
        }
        if (text.charAt(cur) != '0') {
            return 10;
        }
        if (text.charAt(cur + 1) == 'x' || text.charAt(cur + 1) == 'X') {
            used[0] = 2;
            return 16;
        }
        if (text.charAt(cur + 1) == 'b' || text.charAt(cur + 1) == 'B') {
            used[0] = 2;
            return 2;
        }
        return 10;
    }

    private void processIntnum(PayloadToken token) throws PayloadParserException {
        String text = token.text;
        boolean signed = false;
        boolean negative = false;
        int cur = 0;
        if (text.charAt(0) == '+') {
            signed = true;
            cur++;
        } else if (text.charAt(0) == '-') {
            signed = true;
            negative = true;
            cur++;
        }
        if (signed && !currentMode.intSigned) {
            throw new PayloadParserException("Invalid sign on unsigned number", token.column);
        }
        int[] usedForRadix = new int[1];
        int radix = getRadix(text, cur, usedForRadix);
        // construct a string that we can parse with a standard parser.

        boolean signedMode = currentMode.intSigned;
        if (temporaryModeChar == '!')  {
            signedMode = false;
        } else if (temporaryModeChar == '*') {
            signedMode = true;
        } else if (temporaryModeChar != 0) {
            throw new PayloadParserException("Invalid number flag " + temporaryModeChar, token.column);
        }

        int curLength = currentMode.intLength;
        if (temporaryModeLength != -1) {
            curLength = temporaryModeLength;
        }

        if (signedMode) {
            String withoutRadix = (negative ? "-" : "") + text.substring(cur + usedForRadix[0]);
            switch (curLength) {
            case 1:
                appendByte(Byte.parseByte(withoutRadix, radix), token.column);
                break;
            case 2:
                appendShort(Short.parseShort(withoutRadix, radix), token.column);
                break;
            case 4:
                appendInt(Integer.parseInt(withoutRadix, radix), token.column);
                break;
            case 8:
                appendLong(Long.parseLong(withoutRadix, radix), token.column);
                break;
            default:
                throw new PayloadParserException("Number size set to impossible length.", token.column);
            }
        } else {
            String withoutRadix = text.substring(cur + usedForRadix[0]);
            switch (curLength) {
            case 1:
                appendByte(UnsignedBytes.parseUnsignedByte(withoutRadix, radix), token.column);
                break;
            case 2:
                appendShort(parseUnsignedShort(withoutRadix, radix, token.column), token.column);
                break;
            case 4:
                appendInt(UnsignedInts.parseUnsignedInt(withoutRadix, radix), token.column);
                break;
            case 8:
                appendLong(UnsignedLongs.parseUnsignedLong(withoutRadix, radix), token.column);
                break;
            default:
                throw new PayloadParserException("Number size set to impossible length.", token.column);
            }

        }
    }

    private void processMode(PayloadToken token) throws PayloadParserException {
        parseMode(token.text, token.column);
    }

    /**
     *  A mode change item consists of a "#", followed by an optional decimal
     * number, an optional flag (either "!" or "*"), and ends with one of the
     * four characters ":", "i", "f" or "s".  If the terminal character is ":",
     * then the mode is changed for -only- the following value.  If the terminal
     * character is "i", "f" or "s", then the mode for (respectively), integers,
     * floating-point numbers and strings is set for the remainder of the
     * current structure.  The decimal number specifies a new size for the
     * indicated values.  The flag turns on or off either whether integers are
     * signed, or whether strings are zero-terminated.
     * @param text to parse
     * @param column for diagnostics
     */
    void parseMode(String text, int column) throws PayloadParserException {
        int cur = 1; // skip the #
        char c = text.charAt(cur);
        int num = -1;
        char flag = 0;
        // assume single-char lengths.
        if (Character.isDigit(c)) {
            num = Integer.parseInt(Character.toString(c));
            cur++;
            c = text.charAt(cur);
        }

        if (c == '!' || c == '*') { // flag
            flag = c;
            cur++;
            c = text.charAt(cur);
        }

        char which = text.charAt(cur);

        switch (which) {
        case ':':
            // temporary case
            temporaryModeChar = flag;
            temporaryModeLength = num;
            break;
        case 'i':
            if (flag == '!') {
                currentMode.intSigned = false;
            } else if (flag == '*') {
                currentMode.intSigned = true;
            } else if (flag != 0) {
                throw new PayloadParserException("Invalid flag for number " + text, column);
            }
            if (num != -1) {
                switch (num) {
                case 1:
                case 2:
                case 4:
                case 8:
                    currentMode.intLength = num;
                    break;
                default:
                    throw new PayloadParserException("Invalid flag for number " + text, column);
                }
                break;
            }
            break;
        case 'f':
            if (flag != 0) {
                throw new PayloadParserException("Invalid flag for float " + text, column);
            }
            if (num != 4 && num != 8) {
                throw new PayloadParserException("Invalid length for float " + text, column);
            }
            currentMode.floatLength = num;
            break;
        case 's':
            if (flag == '!') {
                currentMode.stringsNullTerminated = false;
            } else if (flag == '*') {
                currentMode.stringsNullTerminated = true;
            } else if (flag != 0) {
                throw new PayloadParserException("Invalid flag for string " + text, column);
            }

            if (num != -1) {
                if (num != 1 && num != 2) {
                    throw new PayloadParserException("Invalid length for string " + text, column);
                }
            }
            currentMode.charlength = num;
            break;
        default:
            throw new PayloadParserException("Invalid last char (not :/i/f/s) " + text, column);
        }
    }

    private void processString(PayloadToken token) throws PayloadParserException {
        int length = currentMode.charlength;
        if (temporaryModeLength == 1) {
            length = 1;
        } else if (temporaryModeLength == 2) {
            length = 2;
        } else if (temporaryModeLength != -1) {
            throw new PayloadParserException("Invalid character length " + temporaryModeLength, token.column);
        }

        boolean nullTerminated = currentMode.stringsNullTerminated;
        if (temporaryModeChar == '!') {
            nullTerminated = false;
        } else if (temporaryModeChar == '*') {
            nullTerminated = true;
        } else if (temporaryModeChar != 0) {
            throw new PayloadParserException("Invalid string flag " + temporaryModeChar, token.column);
        }

        if (length == 1) {
            byte[] bytes = token.text.getBytes(Charsets.US_ASCII);
            buffer.position(used);
            buffer.put(bytes);
            if (nullTerminated) {
                buffer.put((byte)0);
            }
            buffer.position(0);
            used += bytes.length;
            if (nullTerminated) {
                used += 1;
            }
        } else {
            checkAlignment(2, token.column);
            char[] chars = token.text.toCharArray();
            charView.position(used / 2);
            charView.put(chars);
            if (nullTerminated) {
                charView.put((char)0);
            }
            charView.position(0);
            used += chars.length * 2;
            if (nullTerminated) {
                used += 2;
            }
        }
    }

    public static PayloadParser newParser() {
        return new PayloadParser();
    }

    public static PayloadParser newParser(int maxLength, String initialMode) throws PayloadParserException {
        return new PayloadParser(maxLength, initialMode);
    }

    public static PayloadParser newParser(int maxLength, Mode initialMode) {
        return new PayloadParser(maxLength, initialMode);
    }
}
