package com.smartcampus.exception;

import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

@Provider // Marks as a JAX-RS provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        // Return structured JSON error as per Tutorial Week 09
        ErrorMessage msg = new ErrorMessage(ex.getMessage(), 500);
        return Response.status(500).entity(msg).type(MediaType.APPLICATION_JSON).build();
    }
}