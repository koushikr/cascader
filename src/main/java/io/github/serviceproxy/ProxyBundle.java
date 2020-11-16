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
package io.github.serviceproxy;

import com.codahale.metrics.MetricRegistry;
import com.hystrix.configurator.core.HystrixConfigurationFactory;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.serviceproxy.config.ProxyConfiguration;
import io.github.serviceproxy.processors.ProxyProcessor;
import io.github.serviceproxy.resources.ProxyResource;
import lombok.val;

public abstract class ProxyBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public abstract ProxyConfiguration getProxy(T configuration);

    public abstract MetricRegistry getRegistry(T configuration);

    @Override
    public void run(T configuration, Environment environment){
        //Get the relevant configuration
        val proxyConfiguration = getProxy(configuration);
        val metricRegistry = getRegistry(configuration);

        //Initialize the proxyProcessor
        ProxyProcessor proxyProcessor = ProxyProcessor.builder()
                .proxyConfiguration(proxyConfiguration)
                .metricRegistry(metricRegistry)
                .build();

        //Register the resource
        environment.jersey().register(new ProxyResource(proxyProcessor));

        //Initialize hystrix
        HystrixConfigurationFactory.init(proxyConfiguration.getHystrix());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

}
