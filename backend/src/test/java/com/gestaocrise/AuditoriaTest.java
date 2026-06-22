package com.gestaocrise;

import com.gestaocrise.dao.LogAuditoriaDAO;
import com.gestaocrise.entity.LogAuditoria;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AuditoriaTest {

    @Inject
    LogAuditoriaDAO logDAO;

    @Test
    @DisplayName("Deve registrar log automaticamente após requisição de login")
    @Transactional
    public void testRegistroDeLogAposLogin() {
        // Fazer login
        String token = given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"admin@empresa.com\", \"senha\": \"admin123\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getString("dados.token");

        assertNotNull(token, "Token deve ser obtido");

        // Verificar logs
        List<LogAuditoria> logs = logDAO.listarTodos();
        assertFalse(logs.isEmpty(), "Deve existir pelo menos um log");
        assertTrue(logs.stream()
            .anyMatch(l -> l.getEndpoint() != null && l.getEndpoint().contains("/auth/login")),
            "Deve existir log de login"
        );
    }

    @Test
    @DisplayName("Deve registrar log ao acessar endpoint protegido")
    public void testRegistroDeLogAoPedirCrises() {
        // Obter token
        String token = obterTokenAdmin();

        // Fazer requisição
        given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
        .when()
            .get("/api/crises")
        .then()
            .statusCode(200);

        // Verificar logs
        List<LogAuditoria> logs = logDAO.listarTodos();
        assertFalse(logs.isEmpty(), "Deve existir pelo menos um log");
        assertTrue(logs.stream()
            .anyMatch(l -> l.getEndpoint() != null && l.getEndpoint().contains("/crises")),
            "Deve existir log de acesso a crises"
        );
    }

    private String obterTokenAdmin() {
        return given()
            .contentType(ContentType.JSON)
            .body("{\"email\": \"admin@empresa.com\", \"senha\": \"admin123\"}")
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getString("dados.token");
    }
}
