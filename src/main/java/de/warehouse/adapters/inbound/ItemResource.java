package de.warehouse.adapters.inbound;

import de.warehouse.domain.service.ItemService;
import de.warehouse.domain.service.ItemService.DuplicateSkuException;
import de.warehouse.domain.model.Item;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject ItemService service;
    @Context UriInfo uri;

    public static record CreateItemDTO(String sku, String name, String unit, String defaultLocation) {}
    public static record ItemResponse(String sku, String name, String unit, String defaultLocation, Links _links) {}
    public static record Links(String self) {}
    record ErrorPayload(int code, String message) {}

    @POST
    public Response create(CreateItemDTO dto) {
        try {
            Item saved = service.create(dto.sku(), dto.name(), dto.unit(), dto.defaultLocation());
            String self = uri.getAbsolutePathBuilder().path(saved.getSku()).build().toString();
            return Response.created(URI.create(self))
                    .entity(new ItemResponse(saved.getSku(), saved.getName(), saved.getUnit(),
                            saved.getDefaultLocation(), new Links(self)))
                    .build();
        } catch (DuplicateSkuException ex) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorPayload(409, ex.getMessage())).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorPayload(400, ex.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            service.deleteById(id);
            return Response.noContent().build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorPayload(404, ex.getMessage()))
                    .build();
        }
    }

}
