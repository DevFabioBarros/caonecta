package com.caonecta.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.caonecta.model.Animal;
import com.caonecta.model.SolicitacaoAdocao;
import com.caonecta.model.StatusSolicitacao;
import com.caonecta.model.Usuario;

@Repository
public class SolicitacaoAdocaoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioDAO usuarioDAO;
    private final AnimalDAO animalDAO;

    public SolicitacaoAdocaoDAO(
            JdbcTemplate jdbcTemplate,
            UsuarioDAO usuarioDAO,
            AnimalDAO animalDAO
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioDAO = usuarioDAO;
        this.animalDAO = animalDAO;
    }

    public void salvar(SolicitacaoAdocao solicitacaoAdocao) {
        String sql = "INSERT INTO solicitacao_adocao " +
                "(usuario_id, animal_id, data_solicitacao, justificativa, status_solicitacao) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                solicitacaoAdocao.getUsuario().getId(),
                solicitacaoAdocao.getAnimal().getId(),
                solicitacaoAdocao.getDataSolicitacao(),
                solicitacaoAdocao.getJustificativa(),
                solicitacaoAdocao.getStatusSolicitacao().name()
        );
    }

    public List<SolicitacaoAdocao> listarPendentes() {
        String sql = "SELECT * FROM solicitacao_adocao WHERE status_solicitacao = 'PENDENTE'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearSolicitacao(rs));
    }

    public List<SolicitacaoAdocao> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT * FROM solicitacao_adocao WHERE usuario_id = ? ORDER BY data_solicitacao DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearSolicitacao(rs), usuarioId);
    }

    public SolicitacaoAdocao buscarPorId(Long solicitacaoId) {
        String sql = "SELECT * FROM solicitacao_adocao WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapearSolicitacao(rs), solicitacaoId);
    }

    public boolean existeSolicitacaoDoUsuarioParaAnimal(Long usuarioId, Long animalId) {
        String sql = "SELECT COUNT(*) FROM solicitacao_adocao " +
                "WHERE usuario_id = ? " +
                "AND animal_id = ? " +
                "AND status_solicitacao IN ('PENDENTE', 'APROVADA')";

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class, usuarioId, animalId);

        return total != null && total > 0;
    }

    public void aprovar(Long id) {
        String sql = "UPDATE solicitacao_adocao SET status_solicitacao = 'APROVADA' WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void rejeitar(Long id) {
        String sql = "UPDATE solicitacao_adocao SET status_solicitacao = 'REJEITADA' WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void cancelar(Long id) {
        String sql = "UPDATE solicitacao_adocao SET status_solicitacao = 'CANCELADA' WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long buscarAnimalIdPorSolicitacaoId(Long solicitacaoId) {
        String sql = "SELECT animal_id FROM solicitacao_adocao WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, solicitacaoId);
    }

    public void rejeitarOutrasSolicitacoesDoAnimal(Long animalId, Long solicitacaoAprovadaId) {
        String sql = "UPDATE solicitacao_adocao " +
                "SET status_solicitacao = 'REJEITADA' " +
                "WHERE animal_id = ? AND id <> ?";

        jdbcTemplate.update(sql, animalId, solicitacaoAprovadaId);
    }

    private SolicitacaoAdocao mapearSolicitacao(java.sql.ResultSet rs) throws java.sql.SQLException {
        Usuario usuario = usuarioDAO.buscarPorId(rs.getLong("usuario_id"));
        Animal animal = animalDAO.buscarPorId(rs.getLong("animal_id"));

        SolicitacaoAdocao solicitacao = new SolicitacaoAdocao();
        solicitacao.setId(rs.getLong("id"));
        solicitacao.setUsuario(usuario);
        solicitacao.setAnimal(animal);
        solicitacao.setDataSolicitacao(rs.getTimestamp("data_solicitacao").toLocalDateTime());
        solicitacao.setJustificativa(rs.getString("justificativa"));
        solicitacao.setStatusSolicitacao(StatusSolicitacao.valueOf(rs.getString("status_solicitacao")));

        return solicitacao;
    }

    public List<SolicitacaoAdocao> listarTodas() {
        String sql = "SELECT * FROM solicitacao_adocao ORDER BY data_solicitacao DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapearSolicitacao(rs));
    }
}