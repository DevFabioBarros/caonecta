package com.caonecta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.caonecta.model.TipoUsuario;
import com.caonecta.model.Usuario;
import com.caonecta.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/pesquisar-animal";
    }

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String realizarLogin(
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            HttpSession session,
            Model model
    ) {
        try {
            Long animalIdPosLogin = (Long) session.getAttribute("animalIdPosLogin");

            Usuario usuario = usuarioService.realizarLogin(email, senha);

            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("tipoUsuario", usuario.getTipoUsuario());

            if (usuario.getTipoUsuario() == TipoUsuario.ADMINISTRADOR) {
                return "redirect:/admin";
            }

            if (animalIdPosLogin != null) {
                session.removeAttribute("animalIdPosLogin");
                return "redirect:/solicitar-adocao?animalId=" + animalIdPosLogin;
            }

            return "redirect:/pesquisar-animal";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/cadastro-usuario")
    public String exibirCadastroUsuario() {
        return "cadastro-usuario";
    }

    @PostMapping("/cadastro-usuario")
    public String cadastrarUsuario(
            @RequestParam("nomeCompleto") String nomeCompleto,
            @RequestParam("email") String email,
            @RequestParam("cpf") String cpf,
            @RequestParam("telefone") String telefone,
            @RequestParam("senha") String senha,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("numero") String numero,
            @RequestParam("bairro") String bairro,
            @RequestParam("cidade") String cidade,
            @RequestParam("complemento") String complemento
    ) {
        usuarioService.cadastrarUsuario(
                nomeCompleto,
                email,
                cpf,
                telefone,
                senha,
                cep,
                rua,
                numero,
                bairro,
                cidade,
                complemento
        );

        return "redirect:/cadastro-usuario-sucesso";
    }

    @GetMapping("/cadastro-usuario-sucesso")
    public String exibirSucessoCadastroUsuario() {
        return "cadastro-usuario-sucesso";
    }

    @GetMapping("/perfil")
    public String exibirPerfil(HttpSession session, Model model) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        model.addAttribute("usuario", usuario);

        return "perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(
            @RequestParam("nomeCompleto") String nomeCompleto,
            @RequestParam("telefone") String telefone,
            @RequestParam("email") String email,
            HttpSession session
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        usuarioService.atualizarPerfil(usuarioId, nomeCompleto, telefone, email);

        return "redirect:/perfil?sucesso=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/perfil/desativar")
    public String desativarPerfil(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        usuarioService.desativarPerfil(usuarioId);
        session.invalidate();

        return "redirect:/login?perfilExcluido=true";
    }
}