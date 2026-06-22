package com.gestaocrise;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class AuthResourceTest {

    @Test
    @DisplayName("Deve retornar token JWT para credenciais válidas")
    public void testLoginComCredenciaisValidas() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"admin@empresa.com\", \"senha\": \"admin123\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("dados.token", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 401 para credenciais inválidas")
    public void testLoginComCredenciaisInvalidas() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"nao@existe.com\", \"senha\": \"errada\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    @DisplayName("Deve retornar 400 quando email ou senha estão vazios")
    public void testLoginComCamposVazios() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"\", \"senha\": \"\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(400);
    }
}
