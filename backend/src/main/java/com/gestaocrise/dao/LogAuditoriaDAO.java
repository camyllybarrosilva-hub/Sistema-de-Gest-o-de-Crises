package com.gestaocrise.dao;

import com.gestaocrise.entity.LogAuditoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class LogAuditoriaDAO {

    @Inject
    EntityManager em;

    public void salvar(LogAuditoria log) {
        em.persist(log);
    }

    public void deletar(Long id) {
        LogAuditoria log = buscarPorId(id);
        if (log != null) {
            em.remove(log);
        }
    }

    public LogAuditoria buscarPorId(Long id) {
        return em.find(LogAuditoria.class, id);
    }

    public List<LogAuditoria> listarTodos() {
        return em.createQuery("SELECT l FROM LogAuditoria l ORDER BY l.dataHora DESC", LogAuditoria.class)
                .getResultList();
    }

    public List<LogAuditoria> buscarPorUsuario(String usuarioEmail) {
        return em.createQuery("SELECT l FROM LogAuditoria l WHERE l.usuarioEmail = :email ORDER BY l.dataHora DESC", LogAuditoria.class)
                .setParameter("email", usuarioEmail)
                .getResultList();
    }

    public List<LogAuditoria> buscarPorEndpoint(String endpoint) {
        return em.createQuery("SELECT l FROM LogAuditoria l WHERE l.endpoint = :endpoint ORDER BY l.dataHora DESC", LogAuditoria.class)
                .setParameter("endpoint", endpoint)
                .getResultList();
    }

    public List<LogAuditoria> buscarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return em.createQuery("SELECT l FROM LogAuditoria l WHERE l.dataHora BETWEEN :inicio AND :fim ORDER BY l.dataHora DESC", LogAuditoria.class)
                .setParameter("inicio", dataInicio)
                .setParameter("fim", dataFim)
                .getResultList();
    }
}
