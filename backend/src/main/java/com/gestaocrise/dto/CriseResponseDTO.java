package com.gestaocrise.dto;

import java.time.LocalDateTime;

public class CriseResponseDTO {
    public Long id;
    public String titulo;
    public String descricao;
    public String nivel;
    public String status;
    public LocalDateTime dataCriacao;
    public LocalDateTime dataAtualizacao;
    public String responsavelNome;
    public Long responsavelId;
    public String criadoPorNome;
    public Long criadoPorId;

    public CriseResponseDTO() {
    }
}
