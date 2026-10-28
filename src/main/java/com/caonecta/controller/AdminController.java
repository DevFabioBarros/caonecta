package com.caonecta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.caonecta.model.TipoUsuario;
import com.caonecta.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String painelAdmin(HttpSession session, Model model) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        model.addAttribute("usuariosPendentes", adminService.listarUsuariosPendentes());
        model.addAttribute("animaisPendentes", adminService.listarAnimaisPendentes());
        model.addAttribute("solicitacoesPendentes", adminService.listarSolicitacoesPendentes());

        return "admin";
    }

    @PostMapping("/admin/usuario/aprovar")
    public String aprovarUsuario(
            @RequestParam("usuarioId") Long usuarioId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.aprovarUsuario(usuarioId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuario/rejeitar")
    public String rejeitarUsuario(
            @RequestParam("usuarioId") Long usuarioId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.rejeitarUsuario(usuarioId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/animal/aprovar")
    public String aprovarAnimal(
            @RequestParam("animalId") Long animalId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.aprovarAnimal(animalId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/animal/rejeitar")
    public String rejeitarAnimal(
            @RequestParam("animalId") Long animalId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.rejeitarAnimal(animalId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/solicitacao/aprovar")
    public String aprovarSolicitacao(
            @RequestParam("solicitacaoId") Long solicitacaoId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.aprovarSolicitacao(solicitacaoId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/solicitacao/rejeitar")
    public String rejeitarSolicitacao(
            @RequestParam("solicitacaoId") Long solicitacaoId,
            HttpSession session
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        adminService.rejeitarSolicitacao(solicitacaoId);
        return "redirect:/admin";
    }

    private boolean usuarioEhAdministrador(HttpSession session) {
        Object tipoUsuario = session.getAttribute("tipoUsuario");

        return tipoUsuario != null && tipoUsuario == TipoUsuario.ADMINISTRADOR;
    }

    @GetMapping("/admin/animal/detalhes")
public String detalhesAnimal(
        @RequestParam("animalId") Long animalId,
        HttpSession session,
        Model model
) {
    if (!usuarioEhAdministrador(session)) {
        return "redirect:/login";
    }

    model.addAttribute("animal", adminService.buscarAnimalPorId(animalId));
    return "admin-animal-detalhes";
}

    @GetMapping("/admin/usuario/detalhes")
    public String detalhesUsuario(
            @RequestParam("usuarioId") Long usuarioId,
            HttpSession session,
            Model model
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", adminService.buscarUsuarioPorId(usuarioId));
        return "admin-usuario-detalhes";
    }

    @GetMapping("/admin/solicitacao/detalhes")
    public String detalhesSolicitacao(
            @RequestParam("solicitacaoId") Long solicitacaoId,
            HttpSession session,
            Model model
    ) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        model.addAttribute("solicitacao", adminService.buscarSolicitacaoPorId(solicitacaoId));
        return "admin-solicitacao-detalhes";
    }

    @GetMapping("/admin/historico")
    public String historicoAdmin(HttpSession session, Model model) {
        if (!usuarioEhAdministrador(session)) {
            return "redirect:/login";
        }

        model.addAttribute("usuarios", adminService.listarTodosUsuarios());
        model.addAttribute("animais", adminService.listarTodosAnimais());
        model.addAttribute("solicitacoes", adminService.listarTodasSolicitacoes());

        return "admin-historico";
    }
}