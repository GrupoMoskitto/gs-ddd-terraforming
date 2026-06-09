# TerraPath — Sistema de Terraformação Marciana

**Global Solution 2026 | FIAP | DDD em Java**

## Integrantes

| Nome | RM |
|---|---|
| João Vitor de Matos | RM559246 |
| Gabriel Kato | RM560000 |
| Gabriel Couto | RM559579 |
| Marcelo Affonso | RM559790 |
| Belton Lee Car | RM560760 |

> Baseado no projeto [TerraPath](https://grupomoskitto.github.io/dynamic-programming-gs-2026-1s/index.html) — sequenciador de etapas de terraformação com **Dijkstra**.

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 17+
- Maven 3.8+
- IntelliJ IDEA

### Rodar sem configurar nada (banco em memória)

1. Clone o repositório
```bash
git clone https://github.com/SEU_USUARIO/gs-ddd-terraforming.git
```
2. Abra no IntelliJ → aguarde o Maven baixar as dependências
3. Clique com botão direito em `Main.java` → **Run 'Main.main()'**

> O projeto detecta automaticamente se o Oracle não está disponível e usa banco H2 em memória como fallback — nenhuma configuração necessária.

---

## Estrutura do Projeto

```
src/main/java/br/com/fiap/gs/
├── domain/
│   ├── entity/
│   │   ├── Stage.java               ← Classe abstrata (herança / polimorfismo)
│   │   ├── TerraformingStage.java   ← Etapa concreta (extends Stage)
│   │   └── Planet.java              ← Entidade planeta
│   ├── valueobject/
│   │   ├── Energy.java              ← Value Object imutável (TeraJoules)
│   │   └── StageType.java           ← Enum dos tipos de etapa
│   ├── repository/
│   │   └── StageRepository.java     ← Interface do repositório
│   └── exception/
│       ├── TerraformingException.java
│       ├── InsufficientEnergyException.java
│       └── StageNotFoundException.java
├── application/
│   ├── dto/
│   │   ├── StageDTO.java
│   │   └── PathResultDTO.java
│   └── service/
│       ├── TerraformingService.java
│       └── PathCalculatorService.java  ← Dijkstra
├── infrastructure/
│   ├── database/
│   │   └── DatabaseConnection.java  ← Singleton, lê db.properties
│   └── repository/
│       └── StageRepositoryImpl.java
└── Main.java
src/main/resources/
└── db.properties                    ← ✏️ EDITE AQUI suas credenciais
sql/
└── schema.sql                       ← Execute no Oracle antes de rodar
```

---

## Elementos DDD

| Elemento | Onde |
|---|---|
| Herança | `TerraformingStage extends Stage` |
| Polimorfismo | `execute()`, `getRequiredEnergy()`, `getSummary()` |
| Classe abstrata | `Stage.java` |
| Interface | `StageRepository.java` |
| Injeção de Dependência | `TerraformingService(StageRepository)` |
| Value Object | `Energy.java` (imutável) |
| DTO | `StageDTO`, `PathResultDTO` |
| Exceções | `TerraformingException` → `InsufficientEnergyException`, `StageNotFoundException` |
| DateTime | `createdAt`, `updatedAt`, `completedAt` |
| Banco de dados | Oracle + `DatabaseConnection` (Singleton) + `StageRepositoryImpl` |
