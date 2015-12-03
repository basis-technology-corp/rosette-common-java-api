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
package com.basistech.util;

/**
 * Exception class when a Basis Technology runtime component cannot locate the necessary data or library files
 * at runtime. This results when an application fails to set the bt.root system property or call
 * com.basistech.util.Pathnames.setBTRootDirectory.
 */
public class NoRootDirectoryException extends RuntimeException {

    /**
     * Create an exception with a default message.
     */
    public NoRootDirectoryException() {
        super(
              "No Basis product root directory. Set the bt.root system property or call "
              + "com.basistech.util.Pathnames.setBTRootDirectory.");
    }

}
