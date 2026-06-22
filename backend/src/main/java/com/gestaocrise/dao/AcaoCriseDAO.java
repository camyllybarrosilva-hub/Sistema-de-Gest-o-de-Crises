package com.gestaocrise.dao;

import com.gestaocrise.entity.AcaoCrise;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class AcaoCriseDAO {

    @Inject
    EntityManager em;

    public void salvar(AcaoCrise acao) {
        em.persist(acao);
    }

    public void atualizar(AcaoCrise acao) {
        em.merge(acao);
    }

    public void deletar(Long id) {
        AcaoCrise acao = buscarPorId(id);
        if (acao != null) {
            em.remove(acao);
        }
    }

    public AcaoCrise buscarPorId(Long id) {
        return em.find(AcaoCrise.class, id);
    }

    public List<AcaoCrise> listarTodos() {
        return em.createQuery("SELECT a FROM AcaoCrise a ORDER BY a.dataRegistro DESC", AcaoCrise.class)
                .getResultList();
    }

    public List<AcaoCrise> buscarPorCrise(Long criseId) {
        return em.createQuery("SELECT a FROM AcaoCrise a WHERE a.crise.id = :criseId ORDER BY a.dataRegistro DESC", AcaoCrise.class)
                .setParameter("criseId", criseId)
                .getResultList();
    }

    public List<AcaoCrise> buscarPorTipo(String tipo) {
        return em.createQuery("SELECT a FROM AcaoCrise a WHERE a.tipo = :tipo ORDER BY a.dataRegistro DESC", AcaoCrise.class)
                .setParameter("tipo", tipo)
                .getResultList();
    }
}
