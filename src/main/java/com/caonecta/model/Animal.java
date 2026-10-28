package com.caonecta.model;

public class Animal {

    private Long id;
    private String nome;
    private String especie;
    private String raca;
    private Integer idade;
    private String descricao;
    private String urlFoto;
    private boolean aprovado;
    private boolean adotado;
    private Usuario usuario;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaca() {
        return raca;
    }

    public Integer getIdade() {
        return idade;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public boolean isAdotado() {
        return adotado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public void setAdotado(boolean adotado) {
        this.adotado = adotado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}