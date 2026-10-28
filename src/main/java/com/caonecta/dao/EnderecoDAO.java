package com.caonecta.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.caonecta.model.Endereco;

@Repository
public class EnderecoDAO {

    private final JdbcTemplate jdbcTemplate;

    public EnderecoDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long salvar(Endereco endereco) {
        String sql = "INSERT INTO endereco " +
                "(cep, rua, numero, bairro, cidade, complemento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getComplemento()
        );

        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    public void atualizar(Endereco endereco) {
        String sql = "UPDATE endereco SET cep = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, complemento = ? WHERE id = ?";

        jdbcTemplate.update(
                sql,
                endereco.getCep(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getComplemento(),
                endereco.getId()
        );
    }

    public Endereco buscarPorId(Long id) {
        String sql = "SELECT * FROM endereco WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Endereco endereco = new Endereco();
            endereco.setId(rs.getLong("id"));
            endereco.setCep(rs.getString("cep"));
            endereco.setRua(rs.getString("rua"));
            endereco.setNumero(rs.getString("numero"));
            endereco.setBairro(rs.getString("bairro"));
            endereco.setCidade(rs.getString("cidade"));
            endereco.setComplemento(rs.getString("complemento"));
            return endereco;
        }, id);
    }
}