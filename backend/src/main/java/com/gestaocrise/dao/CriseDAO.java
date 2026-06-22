package com.gestaocrise.dao;

import com.gestaocrise.entity.Crise;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CriseDAO {

    @Inject
    EntityManager em;

    public void salvar(Crise crise) {
        em.persist(crise);
    }

    public void atualizar(Crise crise) {
        em.merge(crise);
    }

    public void deletar(Long id) {
        Crise crise = buscarPorId(id);
        if (crise != null) {
            em.remove(crise);
        }
    }

    public Crise buscarPorId(Long id) {
        return em.find(Crise.class, id);
    }

    public List<Crise> listarTodos() {
        return em.createQuery("SELECT c FROM Crise c ORDER BY c.dataCriacao DESC", Crise.class)
                .getResultList();
    }

    public List<Crise> buscarPorStatus(String status) {
        return em.createQuery("SELECT c FROM Crise c WHERE c.status = :status ORDER BY c.dataCriacao DESC", Crise.class)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Crise> buscarPorNivel(String nivel) {
        return em.createQuery("SELECT c FROM Crise c WHERE c.nivel = :nivel ORDER BY c.dataCriacao DESC", Crise.class)
                .setParameter("nivel", nivel)
                .getResultList();
    }

    public List<Crise> buscarPorResponsavel(Long responsavelId) {
        return em.createQuery("SELECT c FROM Crise c WHERE c.responsavel.id = :responsavelId ORDER BY c.dataCriacao DESC", Crise.class)
                .setParameter("responsavelId", responsavelId)
                .getResultList();
    }
}
