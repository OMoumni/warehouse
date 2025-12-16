package de.warehouse.adapters.inbound;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.service.ItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject ItemService service;

    public static record CreateItemDTO(String sku, String name, String unit, String defaultLocation) {}
    public static record UpdateItemDTO(String name, String unit, String defaultLocation) {}

    public static record Links(String self) {}
    public static record ItemResponse(Long id, String sku, String name, String unit, String defaultLocation, Links _links) {}

    @POST
    public Response create(CreateItemDTO dto, @Context UriInfo uri) {
        Item saved = service.create(dto.sku(), dto.name(), dto.unit(), dto.defaultLocation());

        URI self = uri.getAbsolutePathBuilder()
                .path(saved.getId().toString())
                .build();

        return Response.created(self)
                .entity(new ItemResponse(
                        saved.getId(),
                        saved.getSku(),
                        saved.getName(),
                        saved.getUnit(),
                        saved.getDefaultLocation(),
                        new Links(self.toString())
                ))
                .build();
    }


    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.deleteById(id);
        return Response.noContent().build();
    }
    @GET
    @Path("/{id}")
    public ItemResponse getById(@PathParam("id") Long id, @Context UriInfo uri) {
        Item item = service.getById(id);
        URI self = uri.getBaseUriBuilder().path("items").path(item.getId().toString()).build();
        return new ItemResponse(
                item.getId(),
                item.getSku(),
                item.getName(),
                item.getUnit(),
                item.getDefaultLocation(),
                new Links(self.toString())
        );
    }

    @GET
    public List<ItemResponse> list(@QueryParam("offset") @DefaultValue("0") int offset,
                                   @QueryParam("limit")  @DefaultValue("50") int limit,
                                   @Context UriInfo uri) {
        return service.list(offset, limit).stream().map(item -> {
            URI self = uri.getBaseUriBuilder().path("items").path(item.getId().toString()).build();
            return new ItemResponse(
                    item.getId(),
                    item.getSku(),
                    item.getName(),
                    item.getUnit(),
                    item.getDefaultLocation(),
                    new Links(self.toString())
            );
        }).toList();
    }
    @PUT
    @Path("/{id}")
    public ItemResponse update(@PathParam("id") Long id, UpdateItemDTO dto, @Context UriInfo uri) {
        Item updated = service.update(id, dto.name(), dto.unit(), dto.defaultLocation());

        URI self = uri.getBaseUriBuilder().path("items").path(updated.getId().toString()).build();

        return new ItemResponse(
                updated.getId(),
                updated.getSku(),
                updated.getName(),
                updated.getUnit(),
                updated.getDefaultLocation(),
                new Links(self.toString())
        );
    }

}
