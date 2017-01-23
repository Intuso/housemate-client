package com.intuso.housemate.client.v1_0.rest;

import com.intuso.housemate.client.v1_0.api.object.Device;
import com.intuso.housemate.client.v1_0.rest.model.Page;

import javax.ws.rs.*;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/power")
public interface PowerResource {

    @GET
    @Produces("application/json")
    Page<Device.Data> list(@QueryParam("offset") int offset, @QueryParam("limit") int limit);

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
