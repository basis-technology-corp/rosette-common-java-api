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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;

/**
 * A key serializer that we can register against 'Object.class' to notice the type of the key and respect
 * custom serializers.
 * This is part of a workaround to https://github.com/FasterXML/jackson-databind/issues/943, in which
 * Jackson does not examine the specific type of each Map key for possible custom serialization
 * when it has no field/TypeReference type information. We hope for a fix in 2.6.3 but there are no guarantees.
 * This slows down all map serialization for Map&lt;?, ?&gt;, since each key has to be looked up.
 */
class DynamicKeySerializer extends JsonSerializer<Object> {
    private final StdKeySerializer stdKeySerializer = new StdKeySerializer();
    private PropertySerializerMap serializerCache = PropertySerializerMap.emptyForRootValues();

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Class<?> valueClass = value.getClass();
        JsonSerializer<Object> ser = serializerCache.serializerFor(valueClass);
        if (ser == null) {
            JavaType type = serializers.getTypeFactory().constructType(valueClass);
            ser = serializers.findKeySerializer(type, null);
            if (ser.getClass() == DynamicKeySerializer.class) {
                // oops, we found ourself. This happens for any type that isn't specially registered.
                ser = stdKeySerializer;
            }
            serializerCache.addSerializer(valueClass, ser);
        }
        ser.serialize(value, gen, serializers);
    }

    @Override
    public Class<Object> handledType() {
        return Object.class;
    }
}
