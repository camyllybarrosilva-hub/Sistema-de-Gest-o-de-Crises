package com.gestaocrise.dto;

public class UsuarioResponseDTO {
    public Long id;
    public String nome;
    public String email;
    public Boolean ativo;
    public PerfilUsuarioDTO perfil;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String nome, String email, Boolean ativo, PerfilUsuarioDTO perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
        this.perfil = perfil;
    }
}
