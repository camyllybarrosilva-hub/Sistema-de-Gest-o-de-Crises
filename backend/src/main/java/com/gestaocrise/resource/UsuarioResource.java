package com.gestaocrise.resource;

import com.gestaocrise.bo.UsuarioBO;
import com.gestaocrise.dto.UsuarioCreateDTO;
import com.gestaocrise.dto.UsuarioResponseDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioBO usuarioBO;

    @GET
    @RolesAllowed({"ADMIN"})
    public Response listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioBO.listarTodos();
        ApiResponseDTO<List<UsuarioResponseDTO>> response = new ApiResponseDTO<>(200, "Usuários listados com sucesso", usuarios);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response buscarPorId(@PathParam("id") Long id) {
        UsuarioResponseDTO usuario = usuarioBO.buscarPorId(id);
        ApiResponseDTO<UsuarioResponseDTO> response = new ApiResponseDTO<>(200, "Usuário encontrado", usuario);
        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    public Response criar(UsuarioCreateDTO dto) {
        UsuarioResponseDTO usuario = usuarioBO.criar(dto);
        ApiResponseDTO<UsuarioResponseDTO> response = new ApiResponseDTO<>(201, "Usuário criado com sucesso", usuario);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response atualizar(@PathParam("id") Long id, UsuarioCreateDTO dto) {
        UsuarioResponseDTO usuario = usuarioBO.atualizar(id, dto);
        ApiResponseDTO<UsuarioResponseDTO> response = new ApiResponseDTO<>(200, "Usuário atualizado com sucesso", usuario);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response desativar(@PathParam("id") Long id) {
        usuarioBO.desativar(id);
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, "Usuário desativado com sucesso");
        return Response.ok(response).build();
    }
}
