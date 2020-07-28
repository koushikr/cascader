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

package io.github.serviceproxy.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.PATCH;
import io.github.serviceproxy.models.RequestMethod;
import io.github.serviceproxy.processors.ProxyProcessor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Slf4j
@Singleton
@Path("")
public class ProxyResource {

    private ProxyProcessor proxyProcessor;

    public ProxyResource(ProxyProcessor proxyProcessor){
        this.proxyProcessor = proxyProcessor;
    }

    @HEAD
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response head(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) throws Exception {
        return proxyProcessor.process(
                RequestMethod.HEAD,
                path,
                httpHeaders,
                uriInfo,
                null
        );
    }

    @GET
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response get(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) throws Exception {
        return proxyProcessor.process(
                RequestMethod.GET,
                path,
                httpHeaders,
                uriInfo,
                null
        );
    }


    @POST
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response post(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, byte[] body) throws Exception {
        return proxyProcessor.process(
                RequestMethod.POST,
                path,
                httpHeaders,
                uriInfo,
                body
        );
    }

    @PUT
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response put(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, byte[] body) throws Exception {
        return proxyProcessor.process(
                RequestMethod.PUT,
                path,
                httpHeaders,
                uriInfo,
                body
        );
    }

    @OPTIONS
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response options(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, byte[] body) throws Exception {
        return proxyProcessor.process(
                RequestMethod.OPTIONS,
                path,
                httpHeaders,
                uriInfo,
                body
        );
    }

    @PATCH
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response patch(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo, byte[] body) throws Exception {
        return proxyProcessor.process(
                RequestMethod.PATCH,
                path,
                httpHeaders,
                uriInfo,
                body
        );
    }

    @DELETE
    @Path(value = "/{path: .*}")
    @ExceptionMetered
    @Timed
    public Response delete(@PathParam("path") String path, @Context HttpHeaders httpHeaders, @Context UriInfo uriInfo) throws Exception {
        return proxyProcessor.process(
                RequestMethod.DELETE,
                path,
                httpHeaders,
                uriInfo,
                null
        );
    }

}
