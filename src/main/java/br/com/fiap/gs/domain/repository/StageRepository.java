package br.com.fiap.gs.domain.repository;

import br.com.fiap.gs.domain.entity.TerraformingStage;
import br.com.fiap.gs.domain.valueobject.StageType;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório — desacopla domínio de infraestrutura (Injeção de Dependência).
 */
public interface StageRepository {

    TerraformingStage save(TerraformingStage stage);

    Optional<TerraformingStage> findById(Long id);

    Optional<TerraformingStage> findByType(StageType type);

    List<TerraformingStage> findAll();

    List<TerraformingStage> findCompleted();

    List<TerraformingStage> findPending();

    void delete(Long id);

    void deleteAll();

    void update(TerraformingStage stage);
}
