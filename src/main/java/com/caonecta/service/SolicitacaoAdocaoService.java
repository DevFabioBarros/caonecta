package com.caonecta.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.caonecta.dao.AnimalDAO;
import com.caonecta.dao.SolicitacaoAdocaoDAO;
import com.caonecta.dao.UsuarioDAO;
import com.caonecta.model.Animal;
import com.caonecta.model.SolicitacaoAdocao;
import com.caonecta.model.StatusSolicitacao;
import com.caonecta.model.Usuario;

@Service
public class SolicitacaoAdocaoService {

    private final UsuarioDAO usuarioDAO;
    private final AnimalDAO animalDAO;
    private final SolicitacaoAdocaoDAO solicitacaoAdocaoDAO;

    public SolicitacaoAdocaoService(
            UsuarioDAO usuarioDAO,
            AnimalDAO animalDAO,
            SolicitacaoAdocaoDAO solicitacaoAdocaoDAO
    ) {
        this.usuarioDAO = usuarioDAO;
        this.animalDAO = animalDAO;
        this.solicitacaoAdocaoDAO = solicitacaoAdocaoDAO;
    }

    public Animal buscarDadosAnimalPorId(Long animalId) {
        return animalDAO.buscarPorId(animalId);
    }

    public void solicitarAdocao(Long usuarioId, Long animalId, String justificativa) {
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
        Animal animal = animalDAO.buscarPorId(animalId);

        if (animal.getUsuario() != null && animal.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você não pode solicitar adoção de um animal cadastrado por você.");
        }

        if (animal.isAdotado()) {
            throw new IllegalArgumentException("Este animal já foi adotado.");
        }

        if (!animal.isAprovado()) {
            throw new IllegalArgumentException("Este animal ainda não foi aprovado.");
        }

        if (solicitacaoAdocaoDAO.existeSolicitacaoDoUsuarioParaAnimal(usuarioId, animalId)) {
            throw new IllegalArgumentException("Você já enviou uma solicitação para este animal.");
        }

        SolicitacaoAdocao solicitacaoAdocao = new SolicitacaoAdocao();
        solicitacaoAdocao.setUsuario(usuario);
        solicitacaoAdocao.setAnimal(animal);
        solicitacaoAdocao.setDataSolicitacao(LocalDateTime.now());
        solicitacaoAdocao.setJustificativa(justificativa);
        solicitacaoAdocao.setStatusSolicitacao(StatusSolicitacao.PENDENTE);

        solicitacaoAdocaoDAO.salvar(solicitacaoAdocao);
    }

    public List<SolicitacaoAdocao> listarSolicitacoesDoUsuario(Long usuarioId) {
        return solicitacaoAdocaoDAO.listarPorUsuario(usuarioId);
    }

    public void cancelarSolicitacao(Long solicitacaoId, Long usuarioId) {
        SolicitacaoAdocao solicitacao = solicitacaoAdocaoDAO.buscarPorId(solicitacaoId);

        if (!solicitacao.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você não pode cancelar uma solicitação de outro usuário.");
        }

        if (solicitacao.getStatusSolicitacao() != StatusSolicitacao.PENDENTE) {
            throw new IllegalArgumentException("Apenas solicitações pendentes podem ser canceladas.");
        }

        solicitacaoAdocaoDAO.cancelar(solicitacaoId);
    }
}