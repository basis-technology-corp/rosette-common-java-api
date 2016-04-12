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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleKeyDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

/**
 * Module for common enum types when not embedded in the rest of the ADM.
 * This provides specialized serialization when the enum constants themselves
 * are not desirable.  For example, {@code LanguageCode} is usually
 * deserialized from "ARABIC", but this module deserializes it from "ara"
 * instead.
 */
public class EnumModule extends SimpleModule {


    public EnumModule() {
        super(ModuleVersion.VERSION);
    }

    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(LanguageCode.class, LanguageCodeMixin.class);
        SimpleSerializers keySerializers = new SimpleSerializers();
        keySerializers.addSerializer(new LanguageCodeKeySerializer());
        keySerializers.addSerializer(Object.class, new DynamicKeySerializer());
        context.addKeySerializers(keySerializers);
        SimpleKeyDeserializers keyDeserializers = new SimpleKeyDeserializers();
        keyDeserializers.addDeserializer(LanguageCode.class, new LanguageCodeKeyDeserializer());
    }

    /**
     * Register a Jackson module for Rosette's top-level enums an {@link ObjectMapper}.
     * @param mapper the mapper.
     * @return the same mapper, for convenience.
     */
    public static ObjectMapper setupObjectMapper(ObjectMapper mapper) {
        final EnumModule module = new EnumModule();
        mapper.registerModule(module);
        return mapper;
    }
}
