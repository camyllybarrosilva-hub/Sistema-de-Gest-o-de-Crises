package com.gestaocrise;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class ConexaoBancoTest {

    @Inject
    EntityManager em;

    @Test
    @DisplayName("Deve conectar ao PostgreSQL e executar query simples")
    public void testConexaoComBanco() {
        assertNotNull(em, "EntityManager não deve ser nulo");
        Object resultado = em.createNativeQuery("SELECT 1").getSingleResult();
        assertEquals(1, ((Number) resultado).intValue());
    }
}
