/*-
 * ========================LICENSE_START=================================
 * restheart-commons
 * %%
 * Copyright (C) 2019 - 2020 SoftInstigate
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

package org.restheart.exchange;


/**
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public class QueryVariableNotBoundException extends Exception {

    /**
	 *
	 */
	private static final long serialVersionUID = -8291349884609864832L;

	/**
     *
     */
    public QueryVariableNotBoundException() {
        super();
    }

    /**
     *
     * @param message
     */
    public QueryVariableNotBoundException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public QueryVariableNotBoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
