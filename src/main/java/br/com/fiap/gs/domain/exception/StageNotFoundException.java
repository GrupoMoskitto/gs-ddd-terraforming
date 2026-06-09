package br.com.fiap.gs.domain.exception;

public class StageNotFoundException extends TerraformingException {

    public StageNotFoundException(Long id) {
        super("STAGE_001", "Etapa não encontrada com ID: " + id);
    }

    public StageNotFoundException(String name) {
        super("STAGE_002", "Etapa não encontrada com nome: " + name);
    }
}
