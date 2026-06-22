package com.gestaocrise.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "acao_crise")
public class AcaoCrise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(length = 100, nullable = false)
    private String tipo;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "executado_por_id", nullable = false)
    private Usuario executadoPor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crise_id", nullable = false)
    private Crise crise;

    public AcaoCrise() {
    }

    public AcaoCrise(String descricao, String tipo, Usuario executadoPor, Crise crise) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.executadoPor = executadoPor;
        this.crise = crise;
        this.dataRegistro = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Usuario getExecutadoPor() {
        return executadoPor;
    }

    public void setExecutadoPor(Usuario executadoPor) {
        this.executadoPor = executadoPor;
    }

    public Crise getCrise() {
        return crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }
}
