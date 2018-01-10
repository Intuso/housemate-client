package com.intuso.housemate.client.v1_0.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("")
@Consumes("application/json")
@Produces("application/json")
public interface ListenerSocket {
    @GET
    Response listen(@Context HttpServletRequest request);
}
