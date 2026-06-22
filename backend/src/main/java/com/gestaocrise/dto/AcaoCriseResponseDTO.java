package com.gestaocrise.dto;

import java.time.LocalDateTime;

public class AcaoCriseResponseDTO {
    public Long id;
    public String descricao;
    public String tipo;
    public LocalDateTime dataRegistro;
    public String executadoPorNome;
    public Long executadoPorId;
    public Long criseId;

    public AcaoCriseResponseDTO() {
    }
}
