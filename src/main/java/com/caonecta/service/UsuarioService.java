package com.caonecta.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.caonecta.dao.EnderecoDAO;
import com.caonecta.dao.UsuarioDAO;
import com.caonecta.model.Endereco;
import com.caonecta.model.TipoUsuario;
import com.caonecta.model.Usuario;
import com.caonecta.util.HashUtil;

@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final EnderecoDAO enderecoDAO;

    public UsuarioService(UsuarioDAO usuarioDAO, EnderecoDAO enderecoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.enderecoDAO = enderecoDAO;
    }

    public void cadastrarUsuario(
            String nomeCompleto,
            String email,
            String cpf,
            String telefone,
            String senha,
            String cep,
            String rua,
            String numero,
            String bairro,
            String cidade,
            String complemento
    ) {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setRua(rua);
        endereco.setNumero(numero);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setComplemento(complemento);

        Long enderecoId = enderecoDAO.salvar(endereco);
        endereco.setId(enderecoId);

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(nomeCompleto);
        usuario.setEmail(email);
        usuario.setCpf(cpf);
        usuario.setTelefone(telefone);
        usuario.setSenhaHash(HashUtil.gerarHash(senha));
        usuario.setAtivo(true);
        usuario.setAprovado(false);
        usuario.setTipoUsuario(TipoUsuario.USUARIO);
        usuario.setEndereco(endereco);

        usuarioDAO.salvar(usuario);
    }

    public Usuario realizarLogin(String email, String senha) {
        Usuario usuario;

        try {
            usuario = usuarioDAO.buscarPorEmail(email);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Conta não encontrada. Verifique o email ou crie uma conta.");
        }

        String senhaHash = HashUtil.gerarHash(senha);

        if (!usuario.getSenhaHash().equals(senhaHash)) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }

        if (!usuario.isAtivo() && usuario.isAprovado()) {
            throw new IllegalArgumentException(
                    "Seu perfil foi excluído. Se deseja recuperá-lo, contate o administrador: admin@caonecta.com."
            );
        }

        if (!usuario.isAtivo() && !usuario.isAprovado()) {
            throw new IllegalArgumentException(
                    "Sentimos muito, mas seu cadastro foi negado. Em caso de dúvidas, contate o SAC: sac@caonecta.com."
            );
        }

        if (!usuario.isAprovado()) {
            throw new IllegalArgumentException(
                    "Seu cadastro ainda está em análise. Quando o administrador aprovar sua solicitação, ele entrará em contato via telefone ou email."
            );
        }

        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    public void atualizarPerfil(Long usuarioId, String nomeCompleto, String telefone, String email) {
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        usuario.setNomeCompleto(nomeCompleto);
        usuario.setTelefone(telefone);
        usuario.setEmail(email);

        usuarioDAO.atualizarPerfil(usuario);
    }

    public void desativarPerfil(Long usuarioId) {
        usuarioDAO.desativar(usuarioId);
    }
}