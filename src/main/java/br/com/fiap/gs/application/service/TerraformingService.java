package br.com.fiap.gs.application.service;

import br.com.fiap.gs.application.dto.StageDTO;
import br.com.fiap.gs.domain.entity.Planet;
import br.com.fiap.gs.domain.entity.TerraformingStage;
import br.com.fiap.gs.domain.exception.StageNotFoundException;
import br.com.fiap.gs.domain.repository.StageRepository;
import br.com.fiap.gs.domain.valueobject.Energy;
import br.com.fiap.gs.domain.valueobject.StageType;

import java.util.List;

/**
 * Serviço de aplicação — orquestra regras de negócio usando o repositório via injeção de dependência.
 */
public class TerraformingService {

    private final StageRepository repository;

    public TerraformingService(StageRepository repository) {
        this.repository = repository;
    }

    public StageDTO registerStage(StageType type, String description, double riskFactor) {
        TerraformingStage stage = new TerraformingStage(null, type, description, riskFactor);
        TerraformingStage saved = repository.save(stage);
        return StageDTO.from(saved);
    }

    public StageDTO executeStage(Long stageId, Planet planet) {
        TerraformingStage stage = repository.findById(stageId)
                .orElseThrow(() -> new StageNotFoundException(stageId));

        Energy budget  = planet.getTotalEnergyBudget();
        Energy remaining = stage.execute(budget);
        planet.setTotalEnergyBudget(remaining);

        repository.update(stage);
        return StageDTO.from(stage);
    }

    public List<StageDTO> listAllStages() {
        return repository.findAll().stream()
                .map(StageDTO::from)
                .toList();
    }

    public List<StageDTO> listPendingStages() {
        return repository.findPending().stream()
                .map(StageDTO::from)
                .toList();
    }

    public void removeStage(Long id) {
        repository.findById(id).orElseThrow(() -> new StageNotFoundException(id));
        repository.delete(id);
        System.out.println("  Etapa ID=" + id + " removida.");
    }

    public StageDTO findById(Long id) {
        return repository.findById(id)
                .map(StageDTO::from)
                .orElseThrow(() -> new StageNotFoundException(id));
    }
}
