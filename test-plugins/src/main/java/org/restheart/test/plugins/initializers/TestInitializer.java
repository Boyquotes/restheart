/*-
 * ========================LICENSE_START=================================
 * restheart-test-plugins
 * %%
 * Copyright (C) 2020 SoftInstigate
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * =========================LICENSE_END==================================
 */

package org.restheart.test.plugins.initializers;

import io.undertow.predicate.Predicate;
import io.undertow.server.HttpServerExchange;
import org.restheart.exchange.JsonProxyRequest;
import org.restheart.plugins.Initializer;
import org.restheart.plugins.Inject;
import org.restheart.plugins.OnInit;
import org.restheart.plugins.PluginsRegistry;
import org.restheart.plugins.RegisterPlugin;
import static org.restheart.utils.URLUtils.removeTrailingSlashes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just an example initializer. It is not enabledByDefault; to enable it add to
 * configuration file:<br>
 * <pre>
 * plugins-args:
 *     testInitializer:
 *         enabled: true
 * </pre>
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
@RegisterPlugin(
        name = "testInitializer",
        priority = 100,
        description = "The initializer used to test interceptors and global predicates",
        enabledByDefault = false)
public class TestInitializer implements Initializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInitializer.class);

    @Inject("registry")
    private PluginsRegistry registry;

    @OnInit
    public void onInit() {
        registry.getServices();
    }

    @Override
    public void init() {
        LOGGER.info("Testing initializer");
        LOGGER.info("\tdenies GET /secho/foo using a Global Permission Predicate");
        LOGGER.info("\tadds a request and a response interceptors for /iecho and /siecho");

        // add a global security predicate
        this.registry.getGlobalSecurityPredicates()
                .add((Predicate) (HttpServerExchange exchange) -> {
                    var request = JsonProxyRequest.of(exchange);
                    return !(request.isGet() && "/secho/foo".equals(removeTrailingSlashes(request.getPath())));
                });
    }
}
