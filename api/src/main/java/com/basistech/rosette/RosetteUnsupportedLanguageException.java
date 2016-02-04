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

package com.basistech.rosette;

import com.basistech.util.LanguageCode;

/**
 * Exception resulting from attempting to use an unsupported language.
 */
public class RosetteUnsupportedLanguageException extends RosetteRuntimeException {
 
    private static final long serialVersionUID = 1L;
    private final LanguageCode language;

    /**
     * @deprecated As of version 35.5.0.  Use constructors with {@link com.basistech.util.LanguageCode}
     */
    public RosetteUnsupportedLanguageException() {
        super();
        this.language = null;
    }

    /**
     * @deprecated As of version 35.5.0.  Use constructors with {@link com.basistech.util.LanguageCode}
     */
    @Deprecated
    public RosetteUnsupportedLanguageException(String message, Throwable cause) {
        super(message, cause);
        this.language = null;
    }

    /**
     * @deprecated As of version 35.5.0.  Use constructors with {@link com.basistech.util.LanguageCode}
     */
    @Deprecated
    public RosetteUnsupportedLanguageException(String message) {
        super(message);
        this.language = null;
    }

    /**
     * @deprecated As of version 35.5.0.  Use constructors with {@link com.basistech.util.LanguageCode}
     */
    @Deprecated
    public RosetteUnsupportedLanguageException(Throwable cause) {
        super(cause);
        this.language = null;
    }

    /**
     * Constructs an {@code RosetteUnsupportedLanguageException} with the
     * specified language.
     *
     * @param language language
     * @since 35.5.0
     */
    public RosetteUnsupportedLanguageException(LanguageCode language) {
        this(language, null, null);
    }

    /**
     * Constructs an {@code RosetteUnsupportedLanguageException} with the
     * specified language and detail message.
     *
     * @param language language
     * @param message message
     * @since 35.5.0
     */
    public RosetteUnsupportedLanguageException(LanguageCode language, String message) {
        this(language, message, null);
    }

    /**
     * Constructs an {@code RosetteUnsupportedLanguageException} with the
     * specified language and cause.
     *
     * @param language language
     * @param cause case
     * @since 35.5.0
     */
    public RosetteUnsupportedLanguageException(LanguageCode language, Throwable cause) {
        this(language, null, cause);
    }

    /**
     * Constructs an {@code RosetteUnsupportedLanguageException} with the
     * specified language, detail message, and cause.
     *
     * @param language language
     * @param message message
     * @param cause cause
     * @since 35.5.0
     */
    public RosetteUnsupportedLanguageException(LanguageCode language, String message, Throwable cause) {
        super(String.format("%s (%s)%s", language, language.ISO639_3(),
            message == null ? "" : ": " + message), cause);
        this.language = language;
    }

    /**
     * Returns the language associated with this exception.
     *
     * @return language associated with this exception
     * @since 35.5.0
     */
    public LanguageCode getLanguage() {
        return language;
    }
}
