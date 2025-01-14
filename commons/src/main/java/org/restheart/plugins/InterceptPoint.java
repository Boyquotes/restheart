/*-
 * ========================LICENSE_START=================================
 * restheart-commons
 * %%
 * Copyright (C) 2019 - 2023 SoftInstigate
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
package org.restheart.plugins;

/**
 * Defines the intercept point of an Interceptor
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public enum InterceptPoint {
    /**
     * intercept the request before the exchange is initialized
     *
     * The interceptor must implement WildcardInterceptor
     *
     * the Interceptor.handle(request, response) receives the request as UninitializedRequest
     * and UninitializedResponse response.
     *
     * The Interceptor.handle(request, response) receives the request as `UninitializedRequest`
     * and the response as `UninitializedResponse`.
     *
     * It can provide a custom initializer with PluginUtils.attachCustomRequestInitializer()
     * or can modify the raw request content using Request.setRawContent()
     *
     *
     */
    REQUEST_BEFORE_EXCHANGE_INIT,

    /**
     * intercept the request before authentication occurs
     */
    REQUEST_BEFORE_AUTH,

    /**
     * intercept the request after authentication occurs
     */
    REQUEST_AFTER_AUTH,

    /**
     * intercept the response and executes blocking the response
     */
    RESPONSE,

    /**
     * intercept the response and executes asynchronously with the response
     */
    RESPONSE_ASYNC
}
