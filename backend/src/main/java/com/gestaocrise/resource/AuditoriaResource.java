package com.gestaocrise.resource;

import com.gestaocrise.dao.LogAuditoriaDAO;
import com.gestaocrise.dto.LogAuditoriaResponseDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import com.gestaocrise.entity.LogAuditoria;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/auditoria")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditoriaResource {

    @Inject
    LogAuditoriaDAO logDAO;

    @GET
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response listarTodos() {
        List<LogAuditoria> logs = logDAO.listarTodos();
        List<LogAuditoriaResponseDTO> dtos = logs.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        ApiResponseDTO<List<LogAuditoriaResponseDTO>> response = new ApiResponseDTO<>(200, "Logs listados com sucesso", dtos);
        return Response.ok(response).build();
    }

    @GET
    @Path("/usuario/{email}")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response buscarPorUsuario(@PathParam("email") String email) {
        List<LogAuditoria> logs = logDAO.buscarPorUsuario(email);
        List<LogAuditoriaResponseDTO> dtos = logs.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        ApiResponseDTO<List<LogAuditoriaResponseDTO>> response = new ApiResponseDTO<>(200, "Logs listados com sucesso", dtos);
        return Response.ok(response).build();
    }

    @GET
    @Path("/endpoint")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response buscarPorEndpoint(@QueryParam("endpoint") String endpoint) {
        List<LogAuditoria> logs = logDAO.buscarPorEndpoint(endpoint);
        List<LogAuditoriaResponseDTO> dtos = logs.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        ApiResponseDTO<List<LogAuditoriaResponseDTO>> response = new ApiResponseDTO<>(200, "Logs listados com sucesso", dtos);
        return Response.ok(response).build();
    }

    private LogAuditoriaResponseDTO converterParaDTO(LogAuditoria log) {
        LogAuditoriaResponseDTO dto = new LogAuditoriaResponseDTO();
        dto.id = log.getId();
        dto.acao = log.getAcao();
        dto.endpoint = log.getEndpoint();
        dto.usuarioEmail = log.getUsuarioEmail();
        dto.dataHora = log.getDataHora();
        dto.detalhes = log.getDetalhes();
        return dto;
    }
}
