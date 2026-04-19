package com.smartcampus.resources;
import com.smartcampus.dao.SensorDAO;
import com.smartcampus.model.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

public class SensorReadingResource {
    private String sid;
    private SensorDAO dao = new SensorDAO();

    public SensorReadingResource(String sid) { this.sid = sid; }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return dao.getById(sid).getReadings();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading r) {
        Sensor s = dao.getById(sid);
        s.getReadings().add(r);
        s.setCurrentValue(r.getValue()); // Side effect for Part 4.2
        return Response.status(201).entity(r).build();
    }
}