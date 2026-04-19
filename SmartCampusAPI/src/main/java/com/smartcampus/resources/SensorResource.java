package com.smartcampus.resources;

import com.smartcampus.dao.*;
import com.smartcampus.model.Sensor;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/sensors")
public class SensorResource {
    private SensorDAO sDao = new SensorDAO();
    private RoomDAO rDao = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> fetch(@QueryParam("type") String type) {
        List<Sensor> all = sDao.getAll();
        if (type == null) return all;
        return all.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(Sensor s) {
        if (rDao.getById(s.getRoomId()) == null) {
            return Response.status(422).entity("Invalid Room ID: Target room does not exist").build();
        }
        sDao.create(s);
        return Response.status(201).entity(s).build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadings(@PathParam("id") String id) {
        // Ensure SensorReadingResource.java exists in this package to avoid errors
        return new SensorReadingResource(id);
    }
}