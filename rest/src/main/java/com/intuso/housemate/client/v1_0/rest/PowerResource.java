package com.intuso.housemate.client.v1_0.rest;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.rest.model.Page;

import javax.ws.rs.*;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/util/ability/power")
public interface PowerResource {

    @GET
    @Produces("application/json")
    Page<Object.Data> list(@QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("limit") @DefaultValue("20") int limit);

    @GET
    @Path("/{id}")
    boolean isOn(@PathParam("id") String id);

    @POST
    @Path("/{id}/on")
    void turnOn(@PathParam("id") String id);

    @POST
    @Path("/{id}/off")
    void turnOff(@PathParam("id") String id);
}
