package com.intuso.housemate.client.v1_0.rest;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.view.View;

import javax.ws.rs.*;

/**
 * Created by tomc on 06/04/17.
 */
@Path("/object")
public interface ObjectResource {

    @GET
    @Produces("application/json")
    Object.Data get(@QueryParam("path") String path);

    @DELETE
    @Produces("application/json")
    void delete(@QueryParam("path") String path);

    @Path("/view")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    Tree view(@QueryParam("path") String path, View view);

    @Path("/rename")
    @POST
    @Consumes("application/json")
    void rename(@QueryParam("path") String path, String newName);

    @Path("/start")
    @POST
    @Consumes("application/json")
    void start(@QueryParam("path") String path);

    @Path("/stop")
    @POST
    @Consumes("application/json")
    void stop(@QueryParam("path") String path);

    @Path("/perform")
    @POST
    @Consumes("application/json")
    void perform(@QueryParam("path") String path, Type.InstanceMap values);
}
