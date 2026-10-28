package com.caonecta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.caonecta.model.Animal;
import com.caonecta.service.SolicitacaoAdocaoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SolicitacaoAdocaoController {

    private final SolicitacaoAdocaoService solicitacaoAdocaoService;

    public SolicitacaoAdocaoController(SolicitacaoAdocaoService solicitacaoAdocaoService) {
        this.solicitacaoAdocaoService = solicitacaoAdocaoService;
    }

    @GetMapping("/solicitar-adocao")
    public String exibirPaginaSolicitacao(
            @RequestParam("animalId") Long animalId,
            Model model,
            HttpSession session
    ) {
        if (session.getAttribute("usuarioId") == null) {
            session.setAttribute("animalIdPosLogin", animalId);
            return "redirect:/login";
        }

        model.addAttribute("animal", solicitacaoAdocaoService.buscarDadosAnimalPorId(animalId));

        return "solicitar-adocao";
    }

    @PostMapping("/solicitar-adocao")
    public String solicitarAdocao(
            @RequestParam("animalId") Long animalId,
            @RequestParam("justificativa") String justificativa,
            HttpSession session,
            Model model
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        try {
            solicitacaoAdocaoService.solicitarAdocao(usuarioId, animalId, justificativa);
            return "redirect:/solicitacao-adocao-sucesso";

        } catch (Exception e) {
            Animal animal = solicitacaoAdocaoService.buscarDadosAnimalPorId(animalId);
            model.addAttribute("animal", animal);
            model.addAttribute("erro", e.getMessage());
            return "solicitar-adocao";
        }
    }

    @GetMapping("/solicitacao-adocao-sucesso")
    public String exibirSucessoSolicitacao() {
        return "solicitacao-adocao-sucesso";
    }

    @GetMapping("/minhas-solicitacoes")
    public String minhasSolicitacoes(
            HttpSession session,
            Model model
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "solicitacoes",
                solicitacaoAdocaoService.listarSolicitacoesDoUsuario(usuarioId)
        );

        return "minhas-solicitacoes";
    }

    @PostMapping("/minhas-solicitacoes/cancelar")
    public String cancelarSolicitacao(
            @RequestParam("solicitacaoId") Long solicitacaoId,
            HttpSession session
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        solicitacaoAdocaoService.cancelarSolicitacao(solicitacaoId, usuarioId);

        return "redirect:/minhas-solicitacoes";
    }
}