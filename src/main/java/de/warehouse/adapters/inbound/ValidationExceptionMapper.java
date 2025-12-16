package de.warehouse.adapters.inbound;

import de.warehouse.application.ItemService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper
        implements ExceptionMapper<ItemService.ValidationException> {

    @Override
    public Response toResponse(ItemService.ValidationException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(400, msg(e, "Validation failed")))
                .build();
    }

    private static String msg(RuntimeException e, String fallback) {
        return (e.getMessage() == null || e.getMessage().isBlank()) ? fallback : e.getMessage();
    }
}
