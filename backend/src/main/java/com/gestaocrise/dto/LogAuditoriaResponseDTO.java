package com.gestaocrise.dto;

import java.time.LocalDateTime;

public class LogAuditoriaResponseDTO {
    public Long id;
    public String acao;
    public String endpoint;
    public String usuarioEmail;
    public LocalDateTime dataHora;
    public String detalhes;

    public LogAuditoriaResponseDTO() {
    }
}
