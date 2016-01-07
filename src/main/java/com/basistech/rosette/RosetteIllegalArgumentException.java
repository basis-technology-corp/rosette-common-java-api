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

/**
 * Thrown to indicate that a Rosette component has been passed an illegal or
 * inappropriate argument, for example, if a component requires tokens but
 * no tokens were supplied.  Receiving a
 * {@code RosetteIllegalArgumentException} likely means there is a bug in how
 * the Rosette component is being called, whereas receiving
 * {@code IllegalArgumentException} might indicate a bug inside the Rosette
 * component itself.
 *
 * @since 35.5.0
 */
public class RosetteIllegalArgumentException extends IllegalArgumentException {
    /**
     * Constructs a {@code RosetteIllegalArgumentException} with no
     * detail message.
     */
    public RosetteIllegalArgumentException() {
        super();
    }

    /**
     * Constructs a {@code RosetteIllegalArgumentException} with the
     * specified detail message.
     *
     * @param message message
     */
    public RosetteIllegalArgumentException(String message) {
        super(message);
    }

    /**
     * Constructs a {@code RosetteIllegalArgumentException} with the specified
     * detail message and cause.
     *
     * @param  message message
     * @param  cause cause
     */
    public RosetteIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a {@code RosetteIllegalArgumentException} with the specified
     * cause and a default detail message based on the cause.
     *
     * @param cause cause
     */
    public RosetteIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
