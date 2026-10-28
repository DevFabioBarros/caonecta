package com.caonecta.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caonecta.dao.AnimalDAO;
import com.caonecta.dao.UsuarioDAO;
import com.caonecta.model.Animal;
import com.caonecta.model.Usuario;

@Service
public class AnimalService {

    private final AnimalDAO animalDAO;
    private final UsuarioDAO usuarioDAO;

    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024;
    private static final int LARGURA_MINIMA = 300;
    private static final int ALTURA_MINIMA = 300;

    public AnimalService(AnimalDAO animalDAO, UsuarioDAO usuarioDAO) {
        this.animalDAO = animalDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public void cadastrarAnimal(
            Long usuarioId,
            String nome,
            String especie,
            String raca,
            Integer idade,
            String descricao,
            MultipartFile foto
    ) throws IOException {

        if (idade != null && (idade < 0 || idade > 25)) {
            throw new IllegalArgumentException("A idade deve estar entre 0 e 25.");
        }

        validarFoto(foto);

        String urlFoto = salvarFoto(foto);

        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        Animal animal = new Animal();
        animal.setNome(nome);
        animal.setEspecie(especie);
        animal.setRaca(raca);
        animal.setIdade(idade);
        animal.setDescricao(descricao);
        animal.setUrlFoto(urlFoto);
        animal.setAprovado(false);
        animal.setAdotado(false);
        animal.setUsuario(usuario);

        animalDAO.salvar(animal);
    }

    public Animal buscarPorId(Long id) {
        return animalDAO.buscarPorId(id);
    }

    public List<Animal> pesquisarAnimais(String nome, String especie, String raca) {
        return animalDAO.pesquisar(
                nome == null ? "" : nome,
                especie == null ? "" : especie,
                raca == null ? "" : raca
        );
    }

    public List<Animal> listarCadastrosDoUsuario(Long usuarioId) {
        return animalDAO.listarPorUsuario(usuarioId);
    }

    public void cancelarCadastroAnimal(Long animalId, Long usuarioId) {
        animalDAO.cancelarCadastro(animalId, usuarioId);
    }

    private void validarFoto(MultipartFile foto) throws IOException {
        if (foto == null || foto.isEmpty()) {
            throw new IllegalArgumentException("A foto do animal é obrigatória.");
        }

        if (foto.getSize() > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("A foto deve ter no máximo 5MB.");
        }

        String tipo = foto.getContentType();

        if (tipo == null || !tipo.startsWith("image/")) {
            throw new IllegalArgumentException("O arquivo enviado deve ser uma imagem.");
        }

        BufferedImage imagem = ImageIO.read(foto.getInputStream());

        if (imagem == null) {
            throw new IllegalArgumentException("Imagem inválida.");
        }

        if (imagem.getWidth() < LARGURA_MINIMA || imagem.getHeight() < ALTURA_MINIMA) {
            throw new IllegalArgumentException("A imagem deve ter no mínimo 300x300 pixels.");
        }
    }

    private String salvarFoto(MultipartFile foto) throws IOException {
        Path pastaUploads = Path.of(System.getProperty("user.dir"), "uploads", "animais")
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(pastaUploads);

        String nomeOriginal = foto.getOriginalFilename();
        String extensao = "";

        if (nomeOriginal != null && nomeOriginal.contains(".")) {
            extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        }

        String nomeArquivo = UUID.randomUUID() + extensao;
        Path caminhoArquivo = pastaUploads.resolve(nomeArquivo);

        Files.copy(foto.getInputStream(), caminhoArquivo);

        return "/animais/" + nomeArquivo;
    }
}