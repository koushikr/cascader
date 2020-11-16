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

package io.github.serviceproxy.processors;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.appform.core.hystrix.CommandFactory;
import io.github.serviceproxy.config.ProxyConfiguration;
import io.github.serviceproxy.models.RequestMethod;
import io.github.serviceproxy.utils.OkhttpUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.*;
import org.apache.commons.text.StringSubstitutor;

import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Objects;

@Singleton
@Slf4j
@Getter
public class ProxyProcessor {

    private final ProxyConfiguration proxyConfiguration;
    private final OkHttpClient okHttpClient;

    @Builder
    public ProxyProcessor(ProxyConfiguration proxyConfiguration,
                          MetricRegistry metricRegistry){
        this.proxyConfiguration = proxyConfiguration;
        this.okHttpClient = OkhttpUtils.getClient(proxyConfiguration, metricRegistry);
    }

    private String resolvePath(String path, UriInfo uriInfo) {
        String uri = null;
        if (Strings.isNullOrEmpty(path)) {
            if (null != uriInfo.getPathParameters()) {
                uri = StringSubstitutor.replace(path, uriInfo.getPathParameters());
            }
        } else {
            uri = path;
        }
        if (Strings.isNullOrEmpty(uri)) {
            uri = path;
        }
        return uri.charAt(0) == '/' ? uri : "/" + uri;
    }

    private HttpUrl url(String path, UriInfo uriInfo){
        val scheme = proxyConfiguration.getScheme().equalsIgnoreCase("https") ? "https" : "http";
        val builder = new HttpUrl.Builder();
        if (null != uriInfo.getQueryParameters()) {
            uriInfo.getQueryParameters().forEach((key, values) -> values
                    .forEach(value -> builder.addQueryParameter(key, value)));
        }

        builder.host(proxyConfiguration.getHost());

        if(Objects.equals(scheme, "https")){
            builder.scheme("https");
            builder.port(443);
        }else{
            builder.scheme("http");
            builder.port(proxyConfiguration.getPort());
        }

        builder.encodedPath(resolvePath(path, uriInfo));
        return builder.build();
    }

    private Request getRequest(RequestMethod requestMethod,
                               HttpHeaders headers,
                               HttpUrl url,
                               byte[] body
    ){
        val contentType = headers != null && headers.getRequestHeaders() != null && !headers.getRequestHeaders().isEmpty() ?
                headers.getRequestHeaders().getFirst(HttpHeaders.CONTENT_TYPE) : null;

        val mediaType = contentType != null ? MediaType.parse(contentType) : MediaType.parse("*/*");
        val httpRequest = new Request.Builder().url(url);

        if(null != headers && null != headers.getRequestHeaders()){
            headers.getRequestHeaders().forEach(
                    (key, values) -> values.forEach(value -> httpRequest.addHeader(key, value)));
        }

        val requestBody = null != body ? body : new byte[0];

        requestMethod.accept(new RequestMethod.RequestMethodVisitor<Void>() {
            @Override
            public Void visitHead() {
                httpRequest.head();
                return null;
            }

            @Override
            public Void visitGet() {
                httpRequest.get();
                return null;
            }

            @Override
            public Void visitPost() {
                httpRequest.post(
                        RequestBody.create(
                                mediaType, requestBody
                        )
                );
                return null;
            }

            @Override
            public Void visitPut() {
                httpRequest.put(
                        RequestBody.create(
                                mediaType, requestBody
                        )
                );
                return null;
            }

            @Override
            public Void visitOptions() {
                httpRequest.method("OPTIONS", null);
                return null;
            }

            @Override
            public Void visitPatch() {
                httpRequest.patch(
                        RequestBody.create(
                                mediaType, requestBody
                        )
                );
                return null;
            }

            @Override
            public Void visitDelete() {
                httpRequest.delete();
                return null;
            }
        });

        return httpRequest.build();
    }



    public Response process(
            RequestMethod requestMethod,
            String path,
            HttpHeaders httpHeaders,
            UriInfo uriInfo,
            byte[] body
    ) throws Exception{
        Preconditions.checkNotNull(okHttpClient);

        Request request = getRequest(requestMethod, httpHeaders, url(path, uriInfo), body);
        val name = uriInfo.getBaseUri() != null ? uriInfo.getBaseUri().toString() : "default";

        log.info("Proxying the request to host {}, url {} with method {}", request.url().host(), request.url().encodedPath(), requestMethod);

        try {
            val response = CommandFactory.<okhttp3.Response>create(
                    name, "process", null
            ).executor(() -> okHttpClient.newCall(request).execute()).execute();
            String responseString = OkhttpUtils.bodyString(response);
            return Response
                    .status(response.code())
                    .type(httpHeaders.getMediaType())
                    .entity(responseString)
                    .build();
        } catch (Exception e) {
            log.error("Error in making the call: {} with baseUri {} and methodType {}", e,
                    uriInfo.getBaseUri().toString(), requestMethod);
            throw e;
        }
    }

}
