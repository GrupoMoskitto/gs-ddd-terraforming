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

---

## Sobre o Projeto

O **TerraPath** é um sistema de simulação e gerenciamento das etapas de terraformação de Marte, O processo de transformar o planeta vermelho em um lugar habitável para os seres humanos. A documentacão com mais detalhes está disponível em: https://grupomoskitto.github.io/dynamic-programming-gs-2026-1s/index.html

A ideia central é simples: terraformar Marte não é algo que se faz em qualquer ordem. Existe uma sequência lógica de dependências — por exemplo, você não pode introduzir **Fauna** em um planeta sem antes ter **Oxigênio** e uma **Atmosfera** estável. Pular etapas significa desperdiçar recursos ou colocar toda a missão em risco.

O sistema modela essas dependências e calcula automaticamente a **rota mais eficiente** entre as etapas, levando em conta o consumo de energia de cada intervenção (medido em TeraJoules). Para isso, utiliza o **algoritmo de Dijkstra**, que encontra o caminho de menor custo dentro do grafo de dependências.

### As etapas da terraformação

```
Marte Inicial
    └── Campo Magnético     (protege contra radiação solar)
         └── Atmosfera      (cria efeito estufa e retenção de calor)
              ├── Água Líquida   (derrete as calotas polares)
              └── Oxigênio       (cianobactérias produtoras)
                   └── Solo Fértil    (microorganismos no solo)
                        └── Fauna          (introdução de vida animal)
                             └── Marte Habitável ✓
```

Cada etapa tem um **custo energético** e um **fator de risco**. O sistema registra tudo no banco de dados Oracle, acompanha o orçamento de energia disponível e marca cada etapa como concluída conforme a missão avança.

### O que o sistema faz

- Cadastra e gerencia as etapas da missão no banco de dados
- Calcula o caminho de menor gasto energético usando Dijkstra
- Executa as etapas descontando energia do orçamento do planeta
- Trata erros como energia insuficiente ou etapa inexistente
- Exibe o status completo da missão ao final

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 17+
- Maven 3.8+
- IntelliJ IDEA

### Passo a passo

1. Clone o repositório
```bash
git clone https://github.com/GrupoMoskitto/gs-ddd-terraforming.git
```
2. Abra no IntelliJ → aguarde o Maven baixar as dependências
3. Clique com botão direito em `Main.java` → **Run 'Main.main()'**

> O projeto detecta automaticamente se o Oracle não está disponível e usa banco H2 em memória como fallback — nenhuma configuração necessária para rodar.

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
└── db.properties                    ← Credenciais do banco
sql/
└── schema.sql                       ← Script de criação das tabelas
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
| Banco de dados | Oracle Cloud + `DatabaseConnection` (Singleton) + `StageRepositoryImpl` |
