package com.gestaocrise.resource;

import com.gestaocrise.bo.AcaoCriseBO;
import com.gestaocrise.dto.AcaoCriseCreateDTO;
import com.gestaocrise.dto.AcaoCriseResponseDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;

@Path("/crises/{criseId}/acoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AcaoCriseResource {

    @Inject
    AcaoCriseBO acaoBO;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"ADMIN", "GERENTE", "ANALISTA", "VISUALIZADOR"})
    public Response listar(@PathParam("criseId") Long criseId) {
        List<AcaoCriseResponseDTO> acoes = acaoBO.listarPorCrise(criseId);
        ApiResponseDTO<List<AcaoCriseResponseDTO>> response = new ApiResponseDTO<>(200, "Ações listadas com sucesso", acoes);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "GERENTE", "ANALISTA", "VISUALIZADOR"})
    public Response buscarPorId(@PathParam("criseId") Long criseId, @PathParam("id") Long id) {
        AcaoCriseResponseDTO acao = acaoBO.buscarPorId(id);
        if (acao.criseId != criseId) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(404, "Ação não encontrada nesta crise");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        }
        ApiResponseDTO<AcaoCriseResponseDTO> response = new ApiResponseDTO<>(200, "Ação encontrada", acao);
        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "GERENTE", "ANALISTA"})
    public Response criar(@PathParam("criseId") Long criseId, AcaoCriseCreateDTO dto) {
        dto.criseId = criseId;
        Long usuarioId = extrairUsuarioIdDoToken();
        AcaoCriseResponseDTO acao = acaoBO.criar(dto, usuarioId);
        ApiResponseDTO<AcaoCriseResponseDTO> response = new ApiResponseDTO<>(201, "Ação registrada com sucesso", acao);
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
