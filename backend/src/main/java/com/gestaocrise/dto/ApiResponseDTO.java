package com.gestaocrise.dto;

public class ApiResponseDTO<T> {
    public int status;
    public String mensagem;
    public T dados;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(int status, String mensagem, T dados) {
        this.status = status;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public ApiResponseDTO(int status, String mensagem) {
        this.status = status;
        this.mensagem = mensagem;
        this.dados = null;
    }
}
