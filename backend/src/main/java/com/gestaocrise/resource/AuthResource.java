package com.gestaocrise.resource;

import com.gestaocrise.bo.UsuarioBO;
import com.gestaocrise.dto.LoginDTO;
import com.gestaocrise.dto.LoginResponseDTO;
import com.gestaocrise.dto.ApiResponseDTO;
import com.gestaocrise.entity.Usuario;
import com.gestaocrise.security.JwtTokenProvider;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioBO usuarioBO;

    @Inject
    JwtTokenProvider jwtTokenProvider;

    @POST
    @Path("/login")
    public Response login(LoginDTO loginDTO) {
        if (loginDTO.email == null || loginDTO.email.trim().isEmpty() ||
            loginDTO.senha == null || loginDTO.senha.trim().isEmpty()) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(400, "Email e senha são obrigatórios");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        Usuario usuario = usuarioBO.buscarPorEmailEntity(loginDTO.email);
        if (usuario == null || !usuarioBO.verificarSenha(loginDTO.senha, usuario.getSenhaHash())) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(401, "Credenciais inválidas");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }

        if (!usuario.getAtivo()) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(401, "Usuário inativo");
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build();
        }

        String token = jwtTokenProvider.gerarToken(usuario);
        LoginResponseDTO loginResponse = new LoginResponseDTO(token, usuarioBO.buscarPorEmail(usuario.getEmail()));

        ApiResponseDTO<LoginResponseDTO> response = new ApiResponseDTO<>(200, "Login realizado com sucesso", loginResponse);
        return Response.ok(response).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, "Logout realizado com sucesso");
        return Response.ok(response).build();
    }
}
