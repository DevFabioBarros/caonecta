package com.caonecta.model;

import java.time.LocalDateTime;

public class SolicitacaoAdocao {

    private Long id;
    private LocalDateTime dataSolicitacao;
    private String justificativa;
    private Usuario usuario;
    private Animal animal;
    private StatusSolicitacao statusSolicitacao;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Animal getAnimal() {
        return animal;
    }

    public StatusSolicitacao getStatusSolicitacao() {
        return statusSolicitacao;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setStatusSolicitacao(StatusSolicitacao statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }
}