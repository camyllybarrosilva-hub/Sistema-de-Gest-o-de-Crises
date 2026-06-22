package com.gestaocrise;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class ControleAcessoTest {

    @Test
    @DisplayName("VISUALIZADOR não deve criar crise — deve retornar 403")
    public void testVisualizadorNaoPodeCriarCrise() {
        String token = obterToken("visualizador@empresa.com", "vis123");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"titulo\": \"Crise Teste\", \"nivel\": \"ALTO\", \"descricao\": \"Teste\"}")
        .when()
            .post("/api/crises")
        .then()
            .statusCode(403);
    }

    @Test
    @DisplayName("ANALISTA não deve criar crise — deve retornar 403")
    public void testAnalistaNaoPodeCriarCrise() {
        String token = obterToken("analista@empresa.com", "analista123");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"titulo\": \"Crise Teste\", \"nivel\": \"ALTO\", \"descricao\": \"Teste\"}")
        .when()
            .post("/api/crises")
        .then()
            .statusCode(403);
    }

    @Test
    @DisplayName("GERENTE deve criar crise — deve retornar 201")
    public void testGerentePodeCriarCrise() {
        String token = obterToken("gerente@empresa.com", "gerente123");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"titulo\": \"Crise Producao\", \"nivel\": \"CRITICO\", \"descricao\": \"Sistema fora do ar\", \"responsavelId\": 1}")
        .when()
            .post("/api/crises")
        .then()
            .statusCode(201)
            .body("dados.id", notNullValue());
    }

    @Test
    @DisplayName("ADMIN deve criar crise — deve retornar 201")
    public void testAdminPodeCriarCrise() {
        String token = obterToken("admin@empresa.com", "admin123");

        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body("{\"titulo\": \"Crise Admin\", \"nivel\": \"CRITICO\", \"descricao\": \"Sistema crítico\", \"responsavelId\": 1}")
        .when()
            .post("/api/crises")
        .then()
            .statusCode(201)
            .body("dados.id", notNullValue());
    }

    @Test
    @DisplayName("Todos devem poder ler crises — deve retornar 200")
    public void testTodosPodemlerlcrises() {
        String[] usuarios = {
            "admin@empresa.com",
            "gerente@empresa.com",
            "analista@empresa.com",
            "visualizador@empresa.com"
        };
        String[] senhas = {
            "admin123",
            "gerente123",
            "analista123",
            "vis123"
        };

        for (int i = 0; i < usuarios.length; i++) {
            String token = obterToken(usuarios[i], senhas[i]);
            given()
                .header("Authorization", "Bearer " + token)
            .when()
                .get("/api/crises")
            .then()
                .statusCode(200);
        }
    }

    private String obterToken(String email, String senha) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"" + email + "\", \"senha\": \"" + senha + "\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getString("dados.token");
    }
}
