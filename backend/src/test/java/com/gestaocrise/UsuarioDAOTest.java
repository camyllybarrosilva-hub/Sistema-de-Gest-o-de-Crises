package com.gestaocrise;

import com.gestaocrise.dao.PerfilUsuarioDAO;
import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.entity.PerfilUsuario;
import com.gestaocrise.entity.Usuario;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UsuarioDAOTest {

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    PerfilUsuarioDAO perfilDAO;

    @Test
    @DisplayName("Deve salvar e recuperar usuario do banco")
    @Transactional
    public void testSalvarEBuscarUsuario() {
        // Criar perfil
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setNome("TESTE_" + System.nanoTime());
        perfilDAO.salvar(perfil);

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome("Maria Silva");
        usuario.setEmail("maria_" + System.nanoTime() + "@empresa.com");
        usuario.setSenhaHash(BCrypt.withDefaults().hashToString(12, "senha123".toCharArray()));
        usuario.setAtivo(true);
        usuario.setPerfil(perfil);
        usuarioDAO.salvar(usuario);

        assertNotNull(usuario.getId());

        // Buscar usuário
        Usuario encontrado = usuarioDAO.buscarPorId(usuario.getId());
        assertNotNull(encontrado);
        assertEquals("Maria Silva", encontrado.getNome());
        assertEquals(perfil.getNome(), encontrado.getPerfil().getNome());
    }

    @Test
    @DisplayName("Deve buscar usuário por email")
    @Transactional
    public void testBuscarPorEmail() {
        // Criar perfil
        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setNome("TESTE_EMAIL_" + System.nanoTime());
        perfilDAO.salvar(perfil);

        // Criar usuário
        String email = "joao_" + System.nanoTime() + "@empresa.com";
        Usuario usuario = new Usuario();
        usuario.setNome("João Santos");
        usuario.setEmail(email);
        usuario.setSenhaHash(BCrypt.withDefaults().hashToString(12, "senha123".toCharArray()));
        usuario.setAtivo(true);
        usuario.setPerfil(perfil);
        usuarioDAO.salvar(usuario);

        // Buscar por email
        Usuario encontrado = usuarioDAO.buscarPorEmail(email);
        assertNotNull(encontrado);
        assertEquals("João Santos", encontrado.getNome());
    }
}
