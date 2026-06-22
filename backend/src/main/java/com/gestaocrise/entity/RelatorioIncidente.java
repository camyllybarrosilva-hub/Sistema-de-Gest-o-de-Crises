package com.gestaocrise.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorio_incidente")
public class RelatorioIncidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(nullable = false)
    private LocalDateTime dataGeracao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "crise_id", nullable = false)
    private Crise crise;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gerado_por_id", nullable = false)
    private Usuario geradoPor;

    public RelatorioIncidente() {
    }

    public RelatorioIncidente(String titulo, String conteudo, Crise crise, Usuario geradoPor) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.crise = crise;
        this.geradoPor = geradoPor;
        this.dataGeracao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public Crise getCrise() {
        return crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Usuario getGeradoPor() {
        return geradoPor;
    }

    public void setGeradoPor(Usuario geradoPor) {
        this.geradoPor = geradoPor;
    }
}
