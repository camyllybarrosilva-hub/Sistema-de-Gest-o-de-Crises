package com.gestaocrise.dto;

public class LoginResponseDTO {
    public String token;
    public String tipoToken = "Bearer";
    public UsuarioResponseDTO usuario;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, UsuarioResponseDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }
}
