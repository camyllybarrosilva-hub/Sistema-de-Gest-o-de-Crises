package com.gestaocrise.resource;

import com.gestaocrise.bo.CriseBO;
import com.gestaocrise.dto.CriseCreateDTO;
import com.gestaocrise.dto.CriseResponseDTO;
import com.gestaocrise.dto.CriseUpdateDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;

@Path("/crises")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CriseResource {

    @Inject
    CriseBO criseBO;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"ADMIN", "GERENTE", "ANALISTA", "VISUALIZADOR"})
    public Response listarTodas() {
        List<CriseResponseDTO> crises = criseBO.listarTodos();
        ApiResponseDTO<List<CriseResponseDTO>> response = new ApiResponseDTO<>(200, "Crises listadas com sucesso", crises);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "GERENTE", "ANALISTA", "VISUALIZADOR"})
    public Response buscarPorId(@PathParam("id") Long id) {
        CriseResponseDTO crise = criseBO.buscarPorId(id);
        ApiResponseDTO<CriseResponseDTO> response = new ApiResponseDTO<>(200, "Crise encontrada", crise);
        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response criar(CriseCreateDTO dto) {
        Long usuarioId = extrairUsuarioIdDoToken();
        CriseResponseDTO crise = criseBO.criar(dto, usuarioId);
        ApiResponseDTO<CriseResponseDTO> response = new ApiResponseDTO<>(201, "Crise criada com sucesso", crise);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response atualizar(@PathParam("id") Long id, CriseUpdateDTO dto) {
        CriseResponseDTO crise = criseBO.atualizar(id, dto);
        ApiResponseDTO<CriseResponseDTO> response = new ApiResponseDTO<>(200, "Crise atualizada com sucesso", crise);
        return Response.ok(response).build();
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed({"ADMIN", "GERENTE"})
    public Response alterarStatus(@PathParam("id") Long id, @QueryParam("novoStatus") String novoStatus) {
        if (novoStatus == null || novoStatus.trim().isEmpty()) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(400, "Novo status é obrigatório");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        CriseResponseDTO crise = criseBO.alterarStatus(id, novoStatus);
        ApiResponseDTO<CriseResponseDTO> response = new ApiResponseDTO<>(200, "Status alterado com sucesso", crise);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response deletar(@PathParam("id") Long id) {
        criseBO.deletar(id);
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, "Crise deletada com sucesso");
        return Response.ok(response).build();
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
