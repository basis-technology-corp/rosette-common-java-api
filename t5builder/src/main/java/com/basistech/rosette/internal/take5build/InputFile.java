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

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.LineProcessor;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class is an entire input to a Take5 build. It stores a sorted
 * list of key-value pairs, where the keys are UTF-16 and the values are
 * ByteBuffers. It will read in and then deliver as an iteration of
 * {@link com.basistech.rosette.internal.take5build.Take5Pair}
 * objects.
 *
 * This class has a set of bean properties; an application should create the class, set
 * the appropriate properties, and then read the input.
 */
class InputFile {
    private final ByteOrder order;
    private boolean simpleKeys;
    // if false, expect input lines to be purely key.
    private boolean payloads;
    // if true, expect input lines to have payloads, but ignore them.
    private boolean ignorePayloads;
    // payloads are just plain strings.
    private boolean simplePayloads;
    // like !payloads, but fabricate empty payloads. Compatibility with C++.
    private boolean emptyPayloads;

    private String defaultFormat;
    private int maximumItemPayloadSize = PayloadParser.DEFAULT_MAXIMUM_PAYLOAD_BYTES;
    private List<Item> items;

    private class Item implements Take5Pair {
        final String key;
        final Payload payload;

        Item(String key, Payload payload) {
            this.key = key;
            this.payload = payload;
        }

        Item(String key) {
            this(key, null);
        }

        @Override
        public char[] getKey() {
            return key.toCharArray();
        }

        @Override
        public int getKeyLength() {
            return key.length();
        }

        @Override
        public byte[] getValue() {
            if (payload == null) {
                return null;
            } else {
                return payload.bytes;
            }
        }

        @Override
        public int getAlignment() {
            if (payload == null) {
                return 1;
            } else {
                return payload.alignment;
            }
        }

        @Override
        public int getOffset() {
            return 0;
        }

        @Override
        public int getLength() {
            if (payload != null) {
                return payload.bytes.length;
            } else {
                return 0;
            }
        }
    }

    InputFile(ByteOrder order) {
        this.order = order;
    }

    private String packageSimplePayload(String data) {
        return "#2s \"" + data.replace("\"", "\\\"") + "\"";
    }

    private class PayloadsInputLineProcessor implements LineProcessor<List<Item>> {
        private List<Item> items = Lists.newArrayList();
        private int lineCount = 1;
        private boolean first = true;

        @Override
        public boolean processLine(String line) throws IOException {

            if (first) {
                if (line.length() > 0 && line.charAt(0) == '\ufeff') {
                    line = line.substring(1);
                }
                first = false;
            }

            int tabIndex = line.indexOf('\t');

            if (tabIndex == -1 || tabIndex == 0) {
                throw Throwables.propagate(new InputFileException(String.format("No key on line %d", lineCount)));
            }
            if (tabIndex == line.length() - 1) {
                throw Throwables.propagate(new InputFileException(String.format("No key on line %d", lineCount)));
            }

            String keyInput = line.substring(0, tabIndex);
            String key = parseKey(keyInput, lineCount);
            Payload payload = null;

            // for compatibility, support reading key\tpayload but ignore the payload.
            if (!ignorePayloads) {
                String payloadInput = line.substring(tabIndex + 1);
                try {
                    if (simplePayloads) {
                        final String packaged = packageSimplePayload(payloadInput);
                        payload = PayloadParser.newParser(defaultFormat, order).parse(packaged);
                    } else {
                        payload = PayloadParser.newParser(defaultFormat, order).parse(payloadInput);
                    }
                } catch (PayloadParserException e) {
                    throw Throwables.propagate(new InputFileException(String.format("Malformed payload on line %d", lineCount), e));
                }
            }

            items.add(new Item(key, payload));

            lineCount++;
            return true;
        }

        @Override
        public List<Item> getResult() {
            return items;
        }
    }

    /**
     * This is used for key-only files.
     */
    private class SimpleInputLineProcessor implements LineProcessor<List<Item>> {
        private List<Item> items = Lists.newArrayList();
        private int lineCount;
        private boolean first = true;

        @Override
        public boolean processLine(String line) throws IOException {
            if (first) {
                if (line.length() > 0 && line.charAt(0) == '\ufeff') {
                    line = line.substring(1);
                }
                first = false;
            }
            String key = parseKey(line, lineCount);
            if (emptyPayloads) {
                // fabricate a single-byte payload.
                items.add(new Item(key, new Payload(new byte[1], 1)));
            } else {
                items.add(new Item(key));
            }

            lineCount++;
            return true;
        }

        @Override
        public List<Item> getResult() {
            return items;
        }
    }


    // this throws a runtime exception for the convenience of its callers, who have no useful checked exceptions.
    // the IOException will never happen because the IO is on strings.
    private String parseKey(String input, int lineCount) throws IOException {
        if (simpleKeys) {
            return input;
        } else {
            try {
                return new KeyLexer(input).yylex();
            } catch (PayloadLexerException e) {
                throw Throwables.propagate(new InputFileException(String.format("Malformed key on line %d", lineCount), e));
            }
        }
    }

    void read(CharSource charSource) throws IOException, InputFileException {
        try {
            LineProcessor<List<Item>> processor;
            if (payloads) {
                processor = new PayloadsInputLineProcessor();
            } else {
                // end up here for 'emptyPayloads'
                processor = new SimpleInputLineProcessor();
            }
            items = charSource.readLines(processor);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            Throwables.propagateIfInstanceOf(cause, InputFileException.class); // unwrap and throw InputFileException
            throw e;
        }
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.key.compareTo(o2.key); // Hmm, UTF what?
            }
        });
    }



    /**
     * @return an iteration opportunity over the pairs.
     */
    Iterable<Take5Pair> getPairs() {
        // You would think that there was a better way to do this, but ...?
        Iterable<Take5Pair> lazyPairList = Iterables.transform(items, new Function<Item, Take5Pair>() {
            @Override
            public Take5Pair apply(final Item input) {
                return input;
            }
        });

        return lazyPairList;
    }

    public boolean isSimpleKeys() {
        return simpleKeys;
    }

    public void setSimpleKeys(boolean simpleKeys) {
        this.simpleKeys = simpleKeys;
    }

    public boolean isPayloads() {
        return payloads;
    }

    public void setPayloads(boolean payloads) {
        this.payloads = payloads;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }

    public void setDefaultFormat(String defaultFormat) {
        this.defaultFormat = defaultFormat;
    }

    public int getMaximumItemPayloadSize() {
        return maximumItemPayloadSize;
    }

    public void setMaximumItemPayloadSize(int maximumItemPayloadSize) {
        this.maximumItemPayloadSize = maximumItemPayloadSize;
    }

    public boolean isIgnorePayloads() {
        return ignorePayloads;
    }

    public void setIgnorePayloads(boolean ignorePayloads) {
        this.ignorePayloads = ignorePayloads;
    }

    public boolean isSimplePayloads() {
        return simplePayloads;
    }

    public void setSimplePayloads(boolean simplePayloads) {
        this.simplePayloads = simplePayloads;
    }

    public boolean isEmptyPayloads() {
        return emptyPayloads;
    }

    public void setEmptyPayloads(boolean emptyPayloads) {
        this.emptyPayloads = emptyPayloads;
    }
}
