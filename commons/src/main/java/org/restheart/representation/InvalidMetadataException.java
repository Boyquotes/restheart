/*-
 * ========================LICENSE_START=================================
 * restheart-mongodb
 * %%
 * Copyright (C) 2014 - 2020 SoftInstigate
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package org.restheart.representation;

/**
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public class InvalidMetadataException extends Exception {

    /**
     *
     */
    public InvalidMetadataException() {
        super();
    }

    /**
     *
     * @param message
     */
    public InvalidMetadataException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public InvalidMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
}