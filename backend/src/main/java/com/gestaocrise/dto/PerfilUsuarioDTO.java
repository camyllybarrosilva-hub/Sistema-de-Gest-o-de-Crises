package com.gestaocrise.dto;

public class PerfilUsuarioDTO {
    public Long id;
    public String nome;
    public String descricao;
    public String permissoes;

    public PerfilUsuarioDTO() {
    }

    public PerfilUsuarioDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }
}
