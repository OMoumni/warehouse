package de.warehouse.adapters.inbound;

import de.warehouse.domain.service.ItemService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateSkuExceptionMapper
        implements ExceptionMapper<ItemService.DuplicateSkuException> {

    @Override
    public Response toResponse(ItemService.DuplicateSkuException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(409, msg(e, "SKU already exists")))
                .build();
    }

    private static String msg(RuntimeException e, String fallback) {
        return (e.getMessage() == null || e.getMessage().isBlank()) ? fallback : e.getMessage();
    }
}
