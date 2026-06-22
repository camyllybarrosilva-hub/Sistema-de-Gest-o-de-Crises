package com.gestaocrise.dao;

import com.gestaocrise.entity.PerfilUsuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PerfilUsuarioDAO {

    @Inject
    EntityManager em;

    public void salvar(PerfilUsuario perfil) {
        em.persist(perfil);
    }

    public void atualizar(PerfilUsuario perfil) {
        em.merge(perfil);
    }

    public void deletar(Long id) {
        PerfilUsuario perfil = buscarPorId(id);
        if (perfil != null) {
            em.remove(perfil);
        }
    }

    public PerfilUsuario buscarPorId(Long id) {
        return em.find(PerfilUsuario.class, id);
    }

    public PerfilUsuario buscarPorNome(String nome) {
        try {
            return em.createQuery("SELECT p FROM PerfilUsuario p WHERE p.nome = :nome", PerfilUsuario.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PerfilUsuario> listarTodos() {
        return em.createQuery("SELECT p FROM PerfilUsuario p", PerfilUsuario.class)
                .getResultList();
    }
}
