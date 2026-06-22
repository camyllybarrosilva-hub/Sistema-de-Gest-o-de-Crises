package com.gestaocrise.dao;

import com.gestaocrise.entity.RelatorioIncidente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class RelatorioIncidenteDAO {

    @Inject
    EntityManager em;

    public void salvar(RelatorioIncidente relatorio) {
        em.persist(relatorio);
    }

    public void atualizar(RelatorioIncidente relatorio) {
        em.merge(relatorio);
    }

    public void deletar(Long id) {
        RelatorioIncidente relatorio = buscarPorId(id);
        if (relatorio != null) {
            em.remove(relatorio);
        }
    }

    public RelatorioIncidente buscarPorId(Long id) {
        return em.find(RelatorioIncidente.class, id);
    }

    public List<RelatorioIncidente> listarTodos() {
        return em.createQuery("SELECT r FROM RelatorioIncidente r ORDER BY r.dataGeracao DESC", RelatorioIncidente.class)
                .getResultList();
    }

    public List<RelatorioIncidente> buscarPorCrise(Long criseId) {
        return em.createQuery("SELECT r FROM RelatorioIncidente r WHERE r.crise.id = :criseId ORDER BY r.dataGeracao DESC", RelatorioIncidente.class)
                .setParameter("criseId", criseId)
                .getResultList();
    }

    public List<RelatorioIncidente> buscarPorGerador(Long geradorId) {
        return em.createQuery("SELECT r FROM RelatorioIncidente r WHERE r.geradoPor.id = :geradorId ORDER BY r.dataGeracao DESC", RelatorioIncidente.class)
                .setParameter("geradorId", geradorId)
                .getResultList();
    }
}
