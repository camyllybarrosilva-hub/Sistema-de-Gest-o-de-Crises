package com.gestaocrise.resource;

import com.gestaocrise.bo.RelatorioIncidenteBO;
import com.gestaocrise.dto.RelatorioIncidenteCreateDTO;
import com.gestaocrise.dto.RelatorioIncidenteResponseDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;

@Path("/relatorios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelatorioIncidenteResource {

    @Inject
    RelatorioIncidenteBO relatorioBO;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response listarTodos() {
        List<RelatorioIncidenteResponseDTO> relatorios = relatorioBO.listarTodos();
        ApiResponseDTO<List<RelatorioIncidenteResponseDTO>> response = new ApiResponseDTO<>(200, "Relatórios listados com sucesso", relatorios);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response buscarPorId(@PathParam("id") Long id) {
        RelatorioIncidenteResponseDTO relatorio = relatorioBO.buscarPorId(id);
        ApiResponseDTO<RelatorioIncidenteResponseDTO> response = new ApiResponseDTO<>(200, "Relatório encontrado", relatorio);
        return Response.ok(response).build();
    }

    @GET
    @Path("/crise/{criseId}")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response listarPorCrise(@PathParam("criseId") Long criseId) {
        List<RelatorioIncidenteResponseDTO> relatorios = relatorioBO.listarPorCrise(criseId);
        ApiResponseDTO<List<RelatorioIncidenteResponseDTO>> response = new ApiResponseDTO<>(200, "Relatórios listados com sucesso", relatorios);
        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response criar(RelatorioIncidenteCreateDTO dto) {
        Long usuarioId = extrairUsuarioIdDoToken();
        RelatorioIncidenteResponseDTO relatorio = relatorioBO.criar(dto, usuarioId);
        ApiResponseDTO<RelatorioIncidenteResponseDTO> response = new ApiResponseDTO<>(201, "Relatório gerado com sucesso", relatorio);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    private Long extrairUsuarioIdDoToken() {
        try {
            String subject = jwt.getSubject();
            return Long.parseLong(subject);
        } catch (Exception e) {
            throw new BadRequestException("Erro ao extrair ID do usuário do token");
        }
    }
}
