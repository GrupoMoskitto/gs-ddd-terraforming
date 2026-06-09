-- =============================================================
--  TerraPath — Schema Oracle
--  GS DDD | FIAP 2026
-- João Vitor de Matos - RM559246
-- Gabriel Kato – RM560000
-- Gabriel Couto – RM559579
-- Marcelo affonso – RM559790
-- Belton Lee Car - RM560760
-- =============================================================

-- Tabela de etapas de terraformação
CREATE TABLE TB_STAGE (
    id           NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR2(100)  NOT NULL,
    type         VARCHAR2(50)   NOT NULL,
    description  VARCHAR2(500),
    risk_factor  NUMBER(5,4)    DEFAULT 0,
    completed    NUMBER(1)      DEFAULT 0 CHECK (completed IN (0,1)),
    created_at   TIMESTAMP      DEFAULT SYSTIMESTAMP,
    updated_at   TIMESTAMP      DEFAULT SYSTIMESTAMP,
    completed_at TIMESTAMP
);

-- Tabela de planetas
CREATE TABLE TB_PLANET (
    id                  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                VARCHAR2(100) NOT NULL,
    distance_au         NUMBER(10,4),
    energy_budget_tj    NUMBER(12,2)  DEFAULT 0,
    habitable           NUMBER(1)     DEFAULT 0 CHECK (habitable IN (0,1))
);

-- Inserir Marte como planeta padrão
INSERT INTO TB_PLANET (name, distance_au, energy_budget_tj, habitable)
VALUES ('Marte', 1.52, 5000.00, 0);

COMMIT;
