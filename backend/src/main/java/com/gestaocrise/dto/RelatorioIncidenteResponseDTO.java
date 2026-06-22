package com.gestaocrise.dto;

import java.time.LocalDateTime;

public class RelatorioIncidenteResponseDTO {
    public Long id;
    public String titulo;
    public String conteudo;
    public LocalDateTime dataGeracao;
    public Long criseId;
    public String criseTitulo;
    public String geradoPorNome;
    public Long geradoPorId;

    public RelatorioIncidenteResponseDTO() {
    }
}
