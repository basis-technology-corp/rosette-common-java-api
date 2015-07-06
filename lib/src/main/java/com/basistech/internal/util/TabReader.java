/******************************************************************************
 * * This data and information is proprietary to, and a valuable trade secret
 * * of, Basis Technology Corp.  It is given in confidence by Basis Technology
 * * and may only be used as permitted under the license agreement under which
 * * it has been distributed, and in no other way.
 * *
 * * Copyright (c) 2000-2012 Basis Technology Corporation All rights reserved.
 * *
 * * The technical data and information provided herein are provided with
 * * `limited rights', and the computer software provided herein is provided
 * * with `restricted rights' as those terms are defined in DAR and ASPR
 * * 7-104.9(a).
 ******************************************************************************/

package com.basistech.internal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A utility class for reading UTF-8 tab-separated files with optional
 * comment-stripping. Column count can be checked or the validation can be left to relying
 * code.  The tab-delimited fields will have any leading or trailing spaces stripped,
 * simplifying cases where a comment is preceded by a space. A line that is not blank after
 * comment stripping <EM>must</EM> contain exactly the specified number of fields, or an
 * exception will be thrown.
 * <P>
 * There is no way to read a single-field line consisting entirely of whitespace.
 * <P>
 * Due to the limitations of the Java iteration protocol, errors encountered while iterating
 * through the file are signaled using the <EM>unchecked</EM> {@link ReadException}
 * exception. The caller must make sure to handle this exception!
 */
public final class TabReader {

    private TabReader() {
    }

    /**
     * Create a line iterator for a character stream. If the reader is null, it will be treated as zero-length.
     * @param r reader to read from (nullable)
     * @param columns exact number of tab-separated fields per non-blank well-formed line, or -1 for unchecked
     * @param lineCommentStart Comment-start sequence, or null to not strip comments.
     */
    public static Iterable<Line> iterate(final Reader r, final int columns, final String lineCommentStart)
            throws IOException {
        return iterate(r, "<character stream>", columns, lineCommentStart);
    }

    /** Main iterator creator sequence, used by both API methods. */
    private static Iterable<Line> iterate(Reader r,
                                          final String sourceName,
                                          final int columns,
                                          final String lineCommentStart)
            throws IOException {
        // Checkstyle will not let you clarify the following horror with parentheses:
        final BufferedReader br = r == null ? null : new BufferedReader(r);
        if (br != null) {
            br.mark(1);
            if (br.read() != '\uFEFF') { // strip UTF-8 BOM
                br.reset();
            }
        }
        return new Iterable<Line>() {
            public Iterator<Line> iterator() {
                return new LineIterator(br, sourceName, columns, lineCommentStart);
            }
        };
    }

    /**
     * Create a line iterator for a specific file. File is assumed to be in UTF-8.
     * @param src file to read from
     * @param columns exact number of tab-separated fields per non-blank well-formed line, or -1 for unchecked
     * @param lineCommentStart Comment-start sequence, or null to not strip comments.
     * @throws IOException if there was a problem opening the file
     */
    public static Iterable<Line> iterate(final File src, final int columns, final String lineCommentStart)
            throws IOException {
        return iterate(new InputStreamReader(new FileInputStream(src), "UTF-8"),
                src.getAbsolutePath(),
                columns,
                lineCommentStart);
    }

    /**
     * Convenience method to return a list of all lines in a character stream, using
     * {@link #iterate(Reader, int, String)}.
     * @throws IOException if there was a problem opening the file
     */
    public static List<Line> getAll(Reader r, int columns, String lineCommentStart) throws IOException {
        List<Line> collect = new LinkedList<Line>();
        for (Line l : iterate(r, columns, lineCommentStart)) {
            collect.add(l);
        }
        return collect;
    }

    /**
     * Convenience method to return a list of all lines in a file, using {@link #iterate(File, int, String)}.
     * @throws IOException if there was a problem opening the file
     */
    public static List<Line> getAll(File f, int columns, String lineCommentStart) throws IOException {
        List<Line> collect = new LinkedList<Line>();
        for (Line l : iterate(f, columns, lineCommentStart)) {
            collect.add(l);
        }
        return collect;
    }

    /**
     * A single data line read from an input file. 
     */
    public static class Line {
        /** The 1-based number of this line. */
        private final int lineNumber;
        /** Original text of this line, without any comment stripping. */
        private final String original;
        /** Parsed fields of the line. */
        private final String[] data;

        Line(int lineNumber, String original, String[] data) {
            this.lineNumber = lineNumber;
            this.original = original;
            this.data = data;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public String getOriginal() {
            return original;
        }

        public String[] getData() {
            return data;
        }
    }

    /**
     * Line-by-line iterator for TabReader.  
     */
    private static class LineIterator implements Iterator<Line> {
        
        /* Iteration */

        private static final Pattern JUST_SPACES = Pattern.compile("^ *$");
        /** Character stream */
        private final BufferedReader src;
        /** Name of data source */
        private final String sourceName;
        /** Exact number of columns for well-formed lines, or -1 to skip check. */
        private final int columns;
        /** If not null, this is the start sequence for stripping single-line comments. */
        private final String lineCommentStart;
        /** if true, we're out of data */
        private boolean isDone;
        /** number of last line read */
        private int lineNumber;
        /** parsed value from last line (or null) */
        private Line nextLine;

        /**
         * Open the input file for reading.
         */
        LineIterator(BufferedReader src, String sourceName, int columns, String lineCommentStart) {
            this.src = src;
            this.sourceName = sourceName;
            this.columns = columns;
            if (lineCommentStart != null && lineCommentStart.length() == 0) {
                throw new IllegalArgumentException("Cannot use empty string as beginning of line comment.");
            }
            this.lineCommentStart = lineCommentStart;

            if (src == null) {
                isDone = true;
                return;
            }
        }

        /**
         * Parse a line of input and return a record of the parsed line, or null for a blank line.
         */
        private Line parseLine(int lineNumber, String line) throws ParseException {
            if (lineCommentStart != null) {
                int cstart = line.indexOf(lineCommentStart);
                if (cstart > -1) {
                    line = line.substring(0, cstart);
                }
            }
            if (JUST_SPACES.matcher(line).matches()) {
                return null;
            }
            String[] cols = line.split("\\t");
            if (columns != -1 && cols.length != columns) {
                String msgF = "Expected %d tab-separated tokens, found %d instead.";
                throw new ParseException(String.format(msgF, columns, cols.length));
            }
            for (int i = 0; i < cols.length; i++) {
                cols[i] = cols[i].trim();
            }
            return new Line(lineNumber, line, cols);
        }

        /** Cache the next line of data if possible. */
        private void readAhead() throws ReadException {
            if (isDone) { // don't try reading
                return;
            }
            if (nextLine != null) { // already read
                return;
            }
            String text = null;
            Line result = null;
            try {
                while ((text = src.readLine()) != null) {
                    lineNumber++;
                    result = parseLine(lineNumber, text);
                    if (result != null) {
                        break;
                    }
                }
            } catch (ParseException pe) {
                isDone = true; // don't try again!
                String f = "Could not read line %d in %s: %s";
                throw new ReadException(String.format(f, lineNumber, sourceName, pe.getMessage()), pe);
            } catch (IOException ioe) {
                isDone = true; // don't try again!
                String f = "Could not read line %d in %s (I/O error)";
                throw new ReadException(String.format(f, lineNumber, sourceName), ioe);
            }

            nextLine = result;
            if (result == null) { // reading failed
                isDone = true;
            }
        }
        
        /*==============*
         * Iterator API *
         *==============*/

        /** {@inheritDoc} */
        public boolean hasNext() throws ReadException {
            readAhead();
            return !isDone;
        }

        /** {@inheritDoc} */
        public Line next() throws ReadException {
            readAhead();
            Line ret = nextLine;
            nextLine = null; // consume
            return ret;
        }

        /** Throws an exception if called. */
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Cannot remove lines from input file.");
        }

    }

    private static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }

    /** Indicates a TSV reading failure. */
    public static class ReadException extends RuntimeException {
        public ReadException(String message) {
            super(message);
        }

        public ReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
