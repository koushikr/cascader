/*
 * Copyright 2020 Koushik R <rkoushik.14@gmail.com>.
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
package io.github.dropwizard.serviceproxy;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.dropwizard.serviceproxy.config.ProxyConfiguration;


public class App extends Application<AppConfiguration> {

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(new ProxyBundle<AppConfiguration>() {
            @Override
            public ProxyConfiguration getProxy(AppConfiguration configuration) {
                return configuration.getProxy();
            }

            @Override
            public MetricRegistry getRegistry(AppConfiguration configuration) {
                return null;
            }
        });
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) {

    }

    @Override
    public String getName() {
        return "serviceProxy";
    }
}
