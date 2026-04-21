package com.smartcampus.resources;

import com.smartcampus.dao.RoomDAO;
import com.smartcampus.model.Room;
import com.smartcampus.exception.ErrorMessage;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/rooms")
public class SensorRoomResource {

    private RoomDAO dao = new RoomDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        Room r = dao.getById(id);
        if (r == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(r).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) // Add this line!
    public Response create(Room r) {
        if (r == null) {
            return Response.status(400).entity(new ErrorMessage("Invalid Room data", 400)).build();
        }
        dao.create(r);
        return Response.status(201).entity(r).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        Room r = dao.getById(id);
        if (r != null && !r.getSensorIds().isEmpty()) {
            return Response.status(409).entity("Conflict: Room has active sensors").build();
        }
        dao.delete(id);
        return Response.noContent().build();
    }
}
