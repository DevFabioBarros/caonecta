package com.caonecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.caonecta.dao.AnimalDAO;
import com.caonecta.dao.SolicitacaoAdocaoDAO;
import com.caonecta.dao.UsuarioDAO;
import com.caonecta.model.Animal;
import com.caonecta.model.SolicitacaoAdocao;
import com.caonecta.model.Usuario;

@Service
public class AdminService {

    private final UsuarioDAO usuarioDAO;
    private final AnimalDAO animalDAO;
    private final SolicitacaoAdocaoDAO solicitacaoAdocaoDAO;

    public AdminService(
            UsuarioDAO usuarioDAO,
            AnimalDAO animalDAO,
            SolicitacaoAdocaoDAO solicitacaoAdocaoDAO
    ) {
        this.usuarioDAO = usuarioDAO;
        this.animalDAO = animalDAO;
        this.solicitacaoAdocaoDAO = solicitacaoAdocaoDAO;
    }

    public List<Usuario> listarUsuariosPendentes() {
        return usuarioDAO.listarPendentes();
    }

    public List<Animal> listarAnimaisPendentes() {
        return animalDAO.listarPendentes();
    }

    public List<SolicitacaoAdocao> listarSolicitacoesPendentes() {
        return solicitacaoAdocaoDAO.listarPendentes();
    }

    public void aprovarUsuario(Long usuarioId) {
        usuarioDAO.aprovar(usuarioId);
    }

    public void rejeitarUsuario(Long usuarioId) {
        usuarioDAO.rejeitar(usuarioId);
    }

    public void aprovarAnimal(Long animalId) {
        animalDAO.aprovar(animalId);
    }

    public void rejeitarAnimal(Long animalId) {
        animalDAO.rejeitar(animalId);
    }

    public void aprovarSolicitacao(Long solicitacaoId) {
        Long animalId = solicitacaoAdocaoDAO.buscarAnimalIdPorSolicitacaoId(solicitacaoId);

        solicitacaoAdocaoDAO.aprovar(solicitacaoId);
        animalDAO.marcarComoAdotado(animalId);
        solicitacaoAdocaoDAO.rejeitarOutrasSolicitacoesDoAnimal(animalId, solicitacaoId);
    }

    public void rejeitarSolicitacao(Long solicitacaoId) {
        solicitacaoAdocaoDAO.rejeitar(solicitacaoId);
    }

    public Animal buscarAnimalPorId(Long animalId) {
        return animalDAO.buscarPorId(animalId);
    }

    public Usuario buscarUsuarioPorId(Long usuarioId) {
        return usuarioDAO.buscarPorId(usuarioId);
    }

    public SolicitacaoAdocao buscarSolicitacaoPorId(Long solicitacaoId) {
        return solicitacaoAdocaoDAO.buscarPorId(solicitacaoId);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioDAO.listarTodosNaoAdmin();
    }

    public List<Animal> listarTodosAnimais() {
        return animalDAO.listarTodos();
    }

    public List<SolicitacaoAdocao> listarTodasSolicitacoes() {
        return solicitacaoAdocaoDAO.listarTodas();
    }
}