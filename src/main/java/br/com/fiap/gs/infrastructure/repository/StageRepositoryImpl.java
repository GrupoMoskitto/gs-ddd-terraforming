package br.com.fiap.gs.infrastructure.repository;

import br.com.fiap.gs.domain.entity.TerraformingStage;
import br.com.fiap.gs.domain.exception.TerraformingException;
import br.com.fiap.gs.domain.repository.StageRepository;
import br.com.fiap.gs.domain.valueobject.StageType;
import br.com.fiap.gs.infrastructure.database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositório com persistência no Oracle Database.
 * Implementa StageRepository via injeção de dependência.
 */
public class StageRepositoryImpl implements StageRepository {

    private Connection getConn() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public TerraformingStage save(TerraformingStage stage) {
        String sql = "INSERT INTO TB_STAGE (name, type, description, risk_factor, completed, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql,
                new String[]{"ID"})) {
            ps.setString(1, stage.getName());
            ps.setString(2, stage.getType().name());
            ps.setString(3, stage.getDescription());
            ps.setDouble(4, stage.getRiskFactor());
            ps.setInt(5, stage.isCompleted() ? 1 : 0);
            ps.setTimestamp(6, Timestamp.valueOf(stage.getCreatedAt()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) stage.setId(keys.getLong(1));
            }
            return stage;
        } catch (SQLException e) {
            throw new TerraformingException("REPO_001", "Erro ao salvar: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<TerraformingStage> findById(Long id) {
        String sql = "SELECT * FROM TB_STAGE WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new TerraformingException("REPO_002", "Erro ao buscar: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TerraformingStage> findByType(StageType type) {
        String sql = "SELECT * FROM TB_STAGE WHERE type = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, type.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new TerraformingException("REPO_003", "Erro ao buscar por tipo: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<TerraformingStage> findAll() {
        List<TerraformingStage> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_STAGE ORDER BY id";
        try (Statement st = getConn().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new TerraformingException("REPO_004", "Erro ao listar: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<TerraformingStage> findCompleted() {
        return findByStatus(1);
    }

    @Override
    public List<TerraformingStage> findPending() {
        return findByStatus(0);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM TB_STAGE";
        try (Statement st = getConn().createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new TerraformingException("REPO_008", "Erro ao limpar tabela: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM TB_STAGE WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TerraformingException("REPO_005", "Erro ao deletar: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(TerraformingStage stage) {
        String sql = "UPDATE TB_STAGE SET completed = ?, completed_at = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, stage.isCompleted() ? 1 : 0);
            ps.setTimestamp(2, stage.getCompletedAt() != null
                    ? Timestamp.valueOf(stage.getCompletedAt()) : null);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(4, stage.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TerraformingException("REPO_006", "Erro ao atualizar: " + e.getMessage(), e);
        }
    }

    private List<TerraformingStage> findByStatus(int completed) {
        List<TerraformingStage> list = new ArrayList<>();
        String sql = "SELECT * FROM TB_STAGE WHERE completed = ? ORDER BY id";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, completed);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new TerraformingException("REPO_007", "Erro ao filtrar: " + e.getMessage(), e);
        }
        return list;
    }

    private TerraformingStage mapRow(ResultSet rs) throws SQLException {
        TerraformingStage stage = new TerraformingStage(
                rs.getLong("id"),
                StageType.valueOf(rs.getString("type")),
                rs.getString("description"),
                rs.getDouble("risk_factor")
        );
        if (rs.getInt("completed") == 1) {
            Timestamp completedAt = rs.getTimestamp("completed_at");
            stage.forceComplete(completedAt != null ? completedAt.toLocalDateTime() : null);
        }
        return stage;
    }
}
