package com.gestaocrise;

import com.gestaocrise.dao.PerfilUsuarioDAO;
import com.gestaocrise.dao.UsuarioDAO;
import com.gestaocrise.entity.PerfilUsuario;
import com.gestaocrise.entity.Usuario;
import at.favre.lib.crypto.bcrypt.BCrypt;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Arrays;

@ApplicationScoped
public class DataSeeder {

    @Inject
    PerfilUsuarioDAO perfilDAO;

    @Inject
    UsuarioDAO usuarioDAO;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        // Verificar se já existem perfis no banco
        if (perfilDAO.listarTodos().isEmpty()) {
            // Criar perfis
            PerfilUsuario admin = new PerfilUsuario("ADMIN", "Administrador do sistema");
            PerfilUsuario gerente = new PerfilUsuario("GERENTE", "Gerente de crises");
            PerfilUsuario analista = new PerfilUsuario("ANALISTA", "Analista de crises");
            PerfilUsuario visualizador = new PerfilUsuario("VISUALIZADOR", "Visualizador apenas");

            perfilDAO.salvar(admin);
            perfilDAO.salvar(gerente);
            perfilDAO.salvar(analista);
            perfilDAO.salvar(visualizador);

            System.out.println("✓ Perfis criados com sucesso");
        }

        // Verificar se já existe o usuário admin
        if (usuarioDAO.buscarPorEmail("admin@empresa.com") == null) {
            PerfilUsuario perfil = perfilDAO.buscarPorNome("ADMIN");
            String senhaHash = BCrypt.withDefaults().hashToString(12, "admin123".toCharArray());

            Usuario admin = new Usuario("Administrador", "admin@empresa.com", senhaHash, perfil);
            usuarioDAO.salvar(admin);

            System.out.println("✓ Usuário administrador criado: admin@empresa.com / admin123");
        }

        // Criar usuários de exemplo para testes
        if (usuarioDAO.buscarPorEmail("gerente@empresa.com") == null) {
            PerfilUsuario perfil = perfilDAO.buscarPorNome("GERENTE");
            String senhaHash = BCrypt.withDefaults().hashToString(12, "gerente123".toCharArray());

            Usuario gerente = new Usuario("Gerente de Crises", "gerente@empresa.com", senhaHash, perfil);
            usuarioDAO.salvar(gerente);

            System.out.println("✓ Usuário gerente criado: gerente@empresa.com / gerente123");
        }

        if (usuarioDAO.buscarPorEmail("analista@empresa.com") == null) {
            PerfilUsuario perfil = perfilDAO.buscarPorNome("ANALISTA");
            String senhaHash = BCrypt.withDefaults().hashToString(12, "analista123".toCharArray());

            Usuario analista = new Usuario("Analista de Crises", "analista@empresa.com", senhaHash, perfil);
            usuarioDAO.salvar(analista);

            System.out.println("✓ Usuário analista criado: analista@empresa.com / analista123");
        }

        if (usuarioDAO.buscarPorEmail("visualizador@empresa.com") == null) {
            PerfilUsuario perfil = perfilDAO.buscarPorNome("VISUALIZADOR");
            String senhaHash = BCrypt.withDefaults().hashToString(12, "vis123".toCharArray());

            Usuario visualizador = new Usuario("Visualizador", "visualizador@empresa.com", senhaHash, perfil);
            usuarioDAO.salvar(visualizador);

            System.out.println("✓ Usuário visualizador criado: visualizador@empresa.com / vis123");
        }

        System.out.println("\n=== SISTEMA PRONTO PARA USO ===\n");
    }
}
