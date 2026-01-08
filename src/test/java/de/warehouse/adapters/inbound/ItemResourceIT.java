package de.warehouse.adapters.inbound;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ItemResourceIT {

    @Test
    void create_then_getById_works() {
        String sku = "S-" + UUID.randomUUID();

        String location =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\"sku\":\"" + sku + "\",\"name\":\"Test\",\"unit\":\"Box\",\"defaultLocation\":\"R1-A\"}")
                        .when()
                        .post("/items")
                        .then()
                        .statusCode(201)
                        .header("Location", notNullValue())
                        .extract().header("Location");


        given()
                .when()
                .get(location)
                .then()
                .statusCode(200)
                .body("sku", equalTo(sku))
                .body("name", equalTo("Test"))
                .body("_links.self", equalTo(location));
    }

    @Test
    void list_contains_created_items() {
        String sku = "S-" + UUID.randomUUID();

        given()
                .contentType(ContentType.JSON)
                .body("{\"sku\":\"" + sku + "\",\"name\":\"Test\",\"unit\":\"Box\",\"defaultLocation\":\"R1-A\"}")
                .when()
                .post("/items")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/items?offset=0&limit=50")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("sku", hasItem(sku));
    }

    @Test
    void getById_missing_returns_404() {
        given()
                .when()
                .get("/items/999999")
                .then()
                .statusCode(404)
                .body("code", equalTo(404));
    }
}
