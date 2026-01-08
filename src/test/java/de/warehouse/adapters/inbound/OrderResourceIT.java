package de.warehouse.adapters.inbound;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class OrderResourceIT {

    @Test
    void create_order_returns_201_and_location() {
        String storeCode = "DE-BE-" + UUID.randomUUID();

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "storeCode": "%s",
                  "priority": "HIGH"
                }
                """.formatted(storeCode))
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }

    @Test
    void create_then_get_order_works() {
        String storeCode = "DE-BE-" + UUID.randomUUID();

        String location =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                        {
                          "storeCode": "%s",
                          "priority": "HIGH"
                        }
                        """.formatted(storeCode))
                        .when()
                        .post("/orders")
                        .then()
                        .statusCode(201)
                        .extract().header("Location");

        given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body("storeCode", equalTo(storeCode))
                .body("priority", equalTo("HIGH"))
                .body("status", equalTo("CREATED"));
    }

    @Test
    void add_item_with_negative_qtyRequired_returns_400() {
        // 0) create an item so itemId exists
        String sku = "SKU-" + UUID.randomUUID();

        String itemLocation =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                        {
                          "sku": "%s",
                          "name": "TestItem",
                          "unit": "Box",
                          "defaultLocation": "R1-A"
                        }
                        """.formatted(sku))
                        .when()
                        .post("/items")
                        .then()
                        .statusCode(201)
                        .header("Location", notNullValue())
                        .extract().header("Location");

        String itemId = itemLocation.substring(itemLocation.lastIndexOf('/') + 1);

        // 1) create fresh order (unique storeCode)
        String storeCode = "TEST-" + UUID.randomUUID();

        String orderLocation =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                        { "storeCode": "%s", "priority": "HIGH" }
                        """.formatted(storeCode))
                        .when()
                        .post("/orders")
                        .then()
                        .statusCode(201)
                        .extract()
                        .header("Location");

        String orderId = orderLocation.substring(orderLocation.lastIndexOf('/') + 1);

        // 2) negative qtyRequired
        given()
                .contentType(ContentType.JSON)
                .body("""
                { "itemId": %s, "qtyRequired": -1, "location": "R1-A" }
                """.formatted(itemId))
                .when()
                .post("/orders/" + orderId + "/items")
                .then()
                .statusCode(400)
                .body("message", containsString("qtyRequired"));
    }
}
