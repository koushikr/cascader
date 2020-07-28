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
package io.github.serviceproxy.utils;

import com.codahale.metrics.MetricRegistry;
import com.raskasa.metrics.okhttp.InstrumentedOkHttpClients;
import io.github.serviceproxy.config.ProxyConfiguration;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OkhttpUtils {

    public static String bodyString(Response response) throws IOException {
        try(final ResponseBody body = response.body()) {
            return body.string();
        }
    }

    public static OkHttpClient getClient(ProxyConfiguration proxyConfiguration, MetricRegistry metricRegistry){
        int connections = proxyConfiguration.getConnections();
        connections = connections == 0 ? 10 : connections;

        int idleTimeOutSeconds = proxyConfiguration.getIdleTimeoutSeconds();
        idleTimeOutSeconds = idleTimeOutSeconds == 0 ? 30 : idleTimeOutSeconds;

        int connTimeout = proxyConfiguration.getConnectionTimeoutMs();
        connTimeout = connTimeout == 0 ? 10000 : connTimeout;

        int opTimeout = proxyConfiguration.getOperationTimeoutMs();
        opTimeout = opTimeout == 0 ? 10000 : opTimeout;

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(connections);
        dispatcher.setMaxRequestsPerHost(connections);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(connections, idleTimeOutSeconds, TimeUnit.SECONDS))
                .connectTimeout(connTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(opTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(opTimeout, TimeUnit.MILLISECONDS)
                .dispatcher(dispatcher);

        return (metricRegistry != null)
                ? InstrumentedOkHttpClients.create(metricRegistry, clientBuilder.build(), proxyConfiguration.getName())
                : clientBuilder.build();

    }
}
