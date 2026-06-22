package com.gestaocrise.dao;

import com.gestaocrise.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UsuarioDAO {

    @Inject
    EntityManager em;

    public void salvar(Usuario usuario) {
        em.persist(usuario);
    }

    public void atualizar(Usuario usuario) {
        em.merge(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public Usuario buscarPorEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.ativo = true", Usuario.class)
                .getResultList();
    }

    public List<Usuario> listarPorPerfil(Long perfilId) {
        return em.createQuery("SELECT u FROM Usuario u WHERE u.perfil.id = :perfilId AND u.ativo = true", Usuario.class)
                .setParameter("perfilId", perfilId)
                .getResultList();
    }
}
