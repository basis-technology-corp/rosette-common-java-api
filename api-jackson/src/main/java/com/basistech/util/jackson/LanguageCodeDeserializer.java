/*
* Copyright 2014 Basis Technology Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.basistech.util.jackson;

import com.basistech.util.LanguageCode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;

import java.io.IOException;

/**
 * Arrange for {@link com.basistech.util.LanguageCode} to serialize as its ISO-639-3 code.
 */
public class LanguageCodeDeserializer extends FromStringDeserializer<LanguageCode> {

    public LanguageCodeDeserializer() {
        super(LanguageCode.class);
    }

    //CHECKSTYLE:OFF
    @Override
    protected LanguageCode _deserialize(String value, DeserializationContext ctxt) throws IOException {
        try {
            return LanguageCode.lookupByISO639(value);
        } catch (IllegalArgumentException e) {
            throw ctxt.weirdKeyException(LanguageCode.class, value, "Undefined ISO-639 language code");
        }
    }
}
