package com.smartcampus.resources;

import com.smartcampus.dao.SensorDAO;
import com.smartcampus.model.*;
import com.smartcampus.exception.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Sub-resource for handling readings of a specific sensor. Demonstrates
 * Architectural Mastery (Part 4.1).
 */
public class SensorReadingResource {

    private String sid;
    private SensorDAO dao = new SensorDAO();

    public SensorReadingResource(String sid) {
        this.sid = sid;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistory() {
        Sensor s = dao.getById(sid);

        // Safety check to prevent NullPointerException
        if (s == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Sensor ID " + sid + " not found.", 404))
                    .build();
        }

        return Response.ok(s.getReadings()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response addReading(SensorReading r) {
        Sensor s = dao.getById(sid);
        if (s == null) {
            return Response.status(404).entity(new ErrorMessage("Sensor not found", 404)).build();
        }

        s.getReadings().add(r);
        
        s.setCurrentValue(r.getValue());

        return Response.status(201).entity(r).build();
    }
}
