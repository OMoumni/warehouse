package de.warehouse.adapters.inbound;

import de.warehouse.application.OrderService;
import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject OrderService service;

    public static record CreateOrderDTO(String storeCode, String priority) {}
    public static record OrderResponse(
            Long id,
            String storeCode,
            Priority priority,
            OrderStatus status,
            Links _links
    ) {}

    public static record Links(String self) {}

    @POST
    public Response create(CreateOrderDTO dto, @Context UriInfo uri) {
        Order order = service.create(
                dto.storeCode(),
                Priority.valueOf(dto.priority())
        );

        URI self = uri.getAbsolutePathBuilder()
                .path(order.getId().toString())
                .build();

        return Response.created(self).build();
    }
    @GET
    @Path("/{id}")
    public OrderResponse getById(@PathParam("id") Long id, @Context UriInfo uri) {
        Order order = service.getById(id);

        URI self = uri.getBaseUriBuilder()
                .path("orders")
                .path(order.getId().toString())
                .build();

        return new OrderResponse(
                order.getId(),
                order.getStoreCode(),
                order.getPriority(),
                order.getStatus(),
                new Links(self.toString())
        );
    }

}
