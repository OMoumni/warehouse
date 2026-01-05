package de.warehouse.adapters.inbound;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {

    @Override
    public Response toResponse(IllegalStateException ex) {
        return Response.status(Response.Status.CONFLICT) // 409
                .entity(new ErrorResponse(409, ex.getMessage()))
                .build();
    }
}
