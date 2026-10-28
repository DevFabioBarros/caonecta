package com.caonecta.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.caonecta.model.Animal;
import com.caonecta.model.Usuario;

@Repository
public class AnimalDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioDAO usuarioDAO;

    public AnimalDAO(JdbcTemplate jdbcTemplate, UsuarioDAO usuarioDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioDAO = usuarioDAO;
    }

    public void salvar(Animal animal) {
        String sql = "INSERT INTO animal " +
                "(nome, especie, raca, idade, descricao, url_foto, aprovado, adotado, usuario_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                animal.getNome(),
                animal.getEspecie(),
                animal.getRaca(),
                animal.getIdade(),
                animal.getDescricao(),
                animal.getUrlFoto(),
                animal.isAprovado(),
                animal.isAdotado(),
                animal.getUsuario().getId()
        );
    }

    public Animal buscarPorId(Long id) {
        String sql = "SELECT * FROM animal WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapearAnimal(rs), id);
    }

    public List<Animal> pesquisar(String nome, String especie, String raca) {
        String sql = "SELECT * FROM animal " +
                "WHERE aprovado = true " +
                "AND adotado = false " +
                "AND nome LIKE ? " +
                "AND especie LIKE ? " +
                "AND raca LIKE ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> mapearAnimal(rs),
                "%" + nome + "%",
                "%" + especie + "%",
                "%" + raca + "%"
        );
    }

    public List<Animal> listarPendentes() {
        String sql = "SELECT * FROM animal WHERE aprovado = false";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearAnimal(rs));
    }

    public List<Animal> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT * FROM animal WHERE usuario_id = ? ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearAnimal(rs), usuarioId);
    }

    public List<Animal> listarTodos() {
        String sql = "SELECT * FROM animal ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearAnimal(rs));
    }

    public void aprovar(Long id) {
        String sql = "UPDATE animal SET aprovado = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void rejeitar(Long id) {
        String sql = "DELETE FROM animal WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void cancelarCadastro(Long animalId, Long usuarioId) {
        String sql = "DELETE FROM animal WHERE id = ? AND usuario_id = ? AND aprovado = false";
        jdbcTemplate.update(sql, animalId, usuarioId);
    }

    public void marcarComoAdotado(Long id) {
        String sql = "UPDATE animal SET adotado = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Animal mapearAnimal(java.sql.ResultSet rs) throws java.sql.SQLException {
        Animal animal = new Animal();

        animal.setId(rs.getLong("id"));
        animal.setNome(rs.getString("nome"));
        animal.setEspecie(rs.getString("especie"));
        animal.setRaca(rs.getString("raca"));
        animal.setIdade((Integer) rs.getObject("idade"));
        animal.setDescricao(rs.getString("descricao"));
        animal.setUrlFoto(rs.getString("url_foto"));
        animal.setAprovado(rs.getBoolean("aprovado"));
        animal.setAdotado(rs.getBoolean("adotado"));

        Long usuarioId = rs.getLong("usuario_id");

        if (!rs.wasNull()) {
            Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
            animal.setUsuario(usuario);
        }

        return animal;
    }
}