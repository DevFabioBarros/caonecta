package com.caonecta.model;

import java.util.List;

public class Usuario {

    private Long id;
    private String email;
    private boolean ativo;
    private String nomeCompleto;
    private String cpf;
    private String senhaHash;
    private String telefone;
    private boolean aprovado;
    private TipoUsuario tipoUsuario;
    private Endereco endereco;
    private List<Animal> animaisCadastrados;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getTelefone() {
        return telefone;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Animal> getAnimaisCadastrados() {
        return animaisCadastrados;
    }

    public void setAnimaisCadastrados(List<Animal> animaisCadastrados) {
        this.animaisCadastrados = animaisCadastrados;
    }
}