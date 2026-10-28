package com.caonecta.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.caonecta.model.Endereco;
import com.caonecta.model.TipoUsuario;
import com.caonecta.model.Usuario;

@Repository
public class UsuarioDAO {

    private final JdbcTemplate jdbcTemplate;
    private final EnderecoDAO enderecoDAO;

    public UsuarioDAO(JdbcTemplate jdbcTemplate, EnderecoDAO enderecoDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.enderecoDAO = enderecoDAO;
    }

    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario " +
                "(email, ativo, nome_completo, cpf, senha_hash, telefone, aprovado, tipo_usuario, endereco_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                usuario.getEmail(),
                usuario.isAtivo(),
                usuario.getNomeCompleto(),
                usuario.getCpf(),
                usuario.getSenhaHash(),
                usuario.getTelefone(),
                usuario.isAprovado(),
                usuario.getTipoUsuario().name(),
                usuario.getEndereco().getId()
        );
    }

    public void atualizarPerfil(Usuario usuario) {
        String sql = "UPDATE usuario SET nome_completo = ?, telefone = ?, email = ? WHERE id = ?";

        jdbcTemplate.update(
                sql,
                usuario.getNomeCompleto(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getId()
        );
    }

    public Usuario buscarPorId(Long id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setAtivo(rs.getBoolean("ativo"));
            usuario.setNomeCompleto(rs.getString("nome_completo"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setSenhaHash(rs.getString("senha_hash"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setAprovado(rs.getBoolean("aprovado"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));

            Endereco endereco = enderecoDAO.buscarPorId(rs.getLong("endereco_id"));
            usuario.setEndereco(endereco);

            return usuario;
        }, id);
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setAtivo(rs.getBoolean("ativo"));
            usuario.setNomeCompleto(rs.getString("nome_completo"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setSenhaHash(rs.getString("senha_hash"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setAprovado(rs.getBoolean("aprovado"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));

            Endereco endereco = enderecoDAO.buscarPorId(rs.getLong("endereco_id"));
            usuario.setEndereco(endereco);

            return usuario;
        }, email);
    }

    public List<Usuario> listarPendentes() {
        String sql = "SELECT * FROM usuario WHERE aprovado = false AND ativo = true";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setAtivo(rs.getBoolean("ativo"));
            usuario.setNomeCompleto(rs.getString("nome_completo"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setSenhaHash(rs.getString("senha_hash"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setAprovado(rs.getBoolean("aprovado"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));

            Endereco endereco = enderecoDAO.buscarPorId(rs.getLong("endereco_id"));
            usuario.setEndereco(endereco);

            return usuario;
        });
    }

    public void aprovar(Long id) {
        String sql = "UPDATE usuario SET aprovado = true, ativo = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void rejeitar(Long id) {
        String sql = "UPDATE usuario SET aprovado = false, ativo = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void desativar(Long id) {
        String sql = "UPDATE usuario SET ativo = false WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Usuario> listarTodosNaoAdmin() {
        String sql = "SELECT * FROM usuario WHERE tipo_usuario <> 'ADMINISTRADOR' ORDER BY id DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();

            usuario.setId(rs.getLong("id"));
            usuario.setEmail(rs.getString("email"));
            usuario.setAtivo(rs.getBoolean("ativo"));
            usuario.setNomeCompleto(rs.getString("nome_completo"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setSenhaHash(rs.getString("senha_hash"));
            usuario.setTelefone(rs.getString("telefone"));
            usuario.setAprovado(rs.getBoolean("aprovado"));
            usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));

            usuario.setEndereco(
                    enderecoDAO.buscarPorId(rs.getLong("endereco_id"))
            );

            return usuario;
        });
    }
}