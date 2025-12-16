package de.warehouse.adapters.inbound;

import de.warehouse.application.ItemService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ItemNotFoundExceptionMapper
        implements ExceptionMapper<ItemService.ItemNotFoundException> {

    @Override
    public Response toResponse(ItemService.ItemNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(404, msg(e, "Item not found")))
                .build();
    }

    private static String msg(RuntimeException e, String fallback) {
        return (e.getMessage() == null || e.getMessage().isBlank()) ? fallback : e.getMessage();
    }
}
