package com.caonecta.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.caonecta.model.Animal;
import com.caonecta.service.AnimalService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("/cadastrar-animal")
    public String exibirFormularioCadastroAnimal(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) {
            return "redirect:/login";
        }

        return "cadastrar-animal";
    }

    @PostMapping("/cadastrar-animal")
    public String cadastrarAnimal(
            @RequestParam("nome") String nome,
            @RequestParam("especie") String especie,
            @RequestParam("raca") String raca,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam("descricao") String descricao,
            @RequestParam("foto") MultipartFile foto,
            HttpSession session,
            Model model
    ) {

        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        try {
            animalService.cadastrarAnimal(usuarioId, nome, especie, raca, idade, descricao, foto);
            return "redirect:/cadastro-animal-sucesso";

        } catch (IOException | IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastrar-animal";
        }
    }

    @GetMapping("/cadastro-animal-sucesso")
    public String exibirSucessoCadastroAnimal() {
        return "cadastro-animal-sucesso";
    }

    @GetMapping("/pesquisar-animal")
    public String pesquisarAnimal(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "especie", required = false) String especie,
            @RequestParam(value = "raca", required = false) String raca,
            Model model,
            HttpSession session
    ) {
        List<Animal> animais = animalService.pesquisarAnimais(nome, especie, raca);

        model.addAttribute("animais", animais);
        model.addAttribute("nome", nome);
        model.addAttribute("especie", especie);
        model.addAttribute("raca", raca);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioId") != null);

        return "pesquisar-animal";
    }

    @GetMapping("/visualizar-animal")
    public String visualizarAnimal(
            @RequestParam("animalId") Long animalId,
            Model model,
            HttpSession session
    ) {
        Animal animal = animalService.buscarPorId(animalId);

        model.addAttribute("animal", animal);
        model.addAttribute("usuarioLogado", session.getAttribute("usuarioId") != null);

        return "visualizar-animal";
    }

    @GetMapping("/meus-cadastros-animais")
    public String meusCadastrosAnimais(
            HttpSession session,
            Model model
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        model.addAttribute("animais", animalService.listarCadastrosDoUsuario(usuarioId));

        return "meus-cadastros-animais";
    }

    @PostMapping("/meus-cadastros-animais/cancelar")
    public String cancelarCadastroAnimal(
            @RequestParam("animalId") Long animalId,
            HttpSession session
    ) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        animalService.cancelarCadastroAnimal(animalId, usuarioId);

        return "redirect:/meus-cadastros-animais";
    }
}