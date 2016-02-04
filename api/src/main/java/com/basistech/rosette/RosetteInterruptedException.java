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
 * Unchecked exception thrown when Rosette detects that {@link java.lang.Thread#interrupted()} returns <tt>true</tt>.
 * Several Rosette components check for interrupts during long-running computations to allow applications
 * to follow the interrupt pattern documented in the {@link java.lang.Thread} class. Because most Rosette applications
 * are not concerned with thread interrupts, Rosette throws this unchecked exception rather than include
 * {@link InterruptedException} on all method signatures.
 */
public class RosetteInterruptedException extends RuntimeException {

    public RosetteInterruptedException() {
    }

    public RosetteInterruptedException(String message) {
        super(message);
    }

    public RosetteInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RosetteInterruptedException(Throwable cause) {
        super(cause);
    }
}
