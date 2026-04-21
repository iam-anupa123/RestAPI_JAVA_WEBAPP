package com.smartcampus.resources;

import com.smartcampus.dao.*;
import com.smartcampus.model.Sensor;
import com.smartcampus.exception.ErrorMessage;
import com.smartcampus.model.Room;
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
        if (type == null) {
            return all;
        }
        return all.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Sensor s) {
        Room r = rDao.getById(s.getRoomId());
        if (r == null) {

            return Response.status(422).entity(new ErrorMessage("Invalid Room ID", 422)).build();
        }

        r.getSensorIds().add(s.getId());

        sDao.create(s);
        return Response.status(201).entity(s).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Sensor s = sDao.getById(id);
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Sensor not found", 404))
                    .build();
        }

        
        Room r = rDao.getById(s.getRoomId());
        if (r != null) {
            r.getSensorIds().remove(id);
        }

        sDao.delete(id);
        return Response.noContent().build(); 
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadings(@PathParam("id") String id) {
        
        return new SensorReadingResource(id);
    }
}
