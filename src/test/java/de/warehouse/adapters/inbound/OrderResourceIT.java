package de.warehouse.adapters.inbound;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class OrderResourceIT {
    @Test
    void create_order_returns_201_and_location() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                  "storeCode": "DE-BE-05",
                  "priority": "HIGH"
                }
            """)
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .header("Location", notNullValue());
    }
    @Test
    void create_then_get_order_works() {
        String location =
                given()
                        .contentType(ContentType.JSON)
                        .body("""
                {
                  "storeCode": "DE-BE-05",
                  "priority": "HIGH"
                }
            """)
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
                .body("storeCode", equalTo("DE-BE-05"))
                .body("priority", equalTo("HIGH"))
                .body("status", equalTo("CREATED"));
    }
    @Test
    void add_item_with_negative_quantity_returns_400() {
        given()
                .contentType(ContentType.JSON)
                .body("""
            {
              "itemId": 1,
              "quantity": -1
            }
        """)
                .when()
                .post("/orders/1/items")
                .then()
                .statusCode(400)
                .body("message", containsString("Quantity"));
    }



}
