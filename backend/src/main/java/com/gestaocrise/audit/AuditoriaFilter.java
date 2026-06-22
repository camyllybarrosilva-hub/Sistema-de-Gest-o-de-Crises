package com.gestaocrise.audit;

import com.gestaocrise.dao.LogAuditoriaDAO;
import com.gestaocrise.entity.LogAuditoria;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.time.LocalDateTime;

@Provider
@ApplicationScoped
public class AuditoriaFilter implements ContainerRequestFilter {

    @Inject
    LogAuditoriaDAO logDAO;

    @Inject(optional = true)
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        try {
            String metodo = requestContext.getMethod();
            String endpoint = requestContext.getUriInfo().getPath();
            String usuarioEmail = null;

            if (jwt != null && jwt.getName() != null) {
                usuarioEmail = jwt.getClaim("email");
            }

            String acao = metodo + " " + endpoint;

            LogAuditoria log = new LogAuditoria();
            log.setAcao(acao);
            log.setEndpoint(endpoint);
            log.setUsuarioEmail(usuarioEmail);
            log.setDataHora(LocalDateTime.now());

            logDAO.salvar(log);
        } catch (Exception e) {
            // Não falhar a requisição por erro na auditoria
            e.printStackTrace();
        }
    }
}
