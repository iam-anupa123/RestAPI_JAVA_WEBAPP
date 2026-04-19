package com.smartcampus.resources;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscovery() {
        Map<String, Object> api = new HashMap<>();
        api.put("version", "1.0.0");
        api.put("collections", Map.of("rooms", "/api/v1/rooms", "sensors", "/api/v1/sensors"));
        return Response.ok(api).build();
    }
}