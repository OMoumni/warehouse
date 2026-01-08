package de.warehouse.adapters.inbound;

import de.warehouse.application.OrderService;
import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService service;

    // ---------- DTOs ----------
    public record PickItemDTO(Long itemId, int quantity) {}

    public record CreateOrderDTO(String storeCode, String priority) {}

    public record AddItemDTO(Long itemId, int qtyRequired, String location) {}

    public record OrderLineResponse(
            Long itemId,
            int qtyRequired,
            int qtyPicked,
            String location
    ) {}

    public record Links(String self) {}

    public record OrderResponse(
            Long id,
            String storeCode,
            Priority priority,
            OrderStatus status,
            List<OrderLineResponse> items,
            Links _links
    ) {
        public static OrderResponse from(Order order, UriInfo uri) {
            URI self = uri.getBaseUriBuilder()
                    .path("orders")
                    .path(order.getId().toString())
                    .build();

            return new OrderResponse(
                    order.getId(),
                    order.getStoreCode(),
                    order.getPriority(),
                    order.getStatus(),
                    order.getLines().stream()
                            .map(line -> new OrderLineResponse(
                                    line.getItemId(),
                                    line.getQtyRequired(),
                                    line.getQtyPicked(),
                                    line.getLocation()
                            ))
                            .toList(),
                    new Links(self.toString())
            );
        }
    }

    // ---------- Endpoints ----------

    @POST
    public Response create(CreateOrderDTO dto, @Context UriInfo uri) {
        Order order = service.create(
                dto.storeCode(),
                Priority.valueOf(dto.priority().toUpperCase())
        );

        URI self = uri.getAbsolutePathBuilder()
                .path(order.getId().toString())
                .build();

        return Response.created(self)
                .entity(OrderResponse.from(order, uri))
                .build();
    }

    @GET
    @Path("/{id}")
    public OrderResponse getById(@PathParam("id") Long id, @Context UriInfo uri) {
        return OrderResponse.from(service.getById(id), uri);
    }

    @POST
    @Path("/{id}/items")
    public Response addItem(@PathParam("id") Long orderId, AddItemDTO dto) {
        service.addItem(orderId, dto.itemId(), dto.qtyRequired(), dto.location());
        return Response.noContent().build();
    }

    @GET
    public List<OrderResponse> getByStore(@QueryParam("storeCode") String storeCode,
                                          @Context UriInfo uri) {
        return service.getOrdersByStore(storeCode)
                .stream()
                .map(order -> OrderResponse.from(order, uri))
                .toList();
    }

    @PUT
    @Path("/{id}/complete")
    public OrderResponse complete(@PathParam("id") Long id, @Context UriInfo uri) {
        return OrderResponse.from(service.complete(id), uri);
    }

    @POST
    @Path("/{id}/pick")
    public Response pick(@PathParam("id") Long orderId, PickItemDTO dto) {
        service.pickItem(orderId, dto.itemId(), dto.quantity());
        return Response.noContent().build();
    }
}
