# Connexa — modelo de repositório (Java + React)

Repositório-modelo para as equipes que escolheram **Java** no back-end do Connexa. A partir dele, cada equipe cria, na organização do GitHub da própria equipe, o seu `<repositorio-da-equipe>` — com nome escolhido pela equipe — e já recebe a infraestrutura pronta: arquitetura modular, banco de dados, testes, CI, ambiente no GitHub Codespaces e a conexão do agente de IA com o Azure Boards.

> **O que este modelo não traz:** nenhuma funcionalidade do Connexa. Os módulos estão vazios e são preenchidos sprint a sprint, a partir das Tasks do Azure Boards. As duas fatias incluídas (situação do sistema e estudante simulado) são exemplos de estrutura e apoio ao desenvolvimento.

| Parte | Tecnologia |
|---|---|
| Back-end | Java 21 · Spring Boot 4.1 · Spring Modulith 2.1 · Spring Data JPA · Flyway · springdoc-openapi |
| Banco de dados | PostgreSQL 17, em contêiner |
| Front-end | React 19 · TypeScript · Vite · React Router · TanStack Query · React Hook Form + Zod |
| Testes | JUnit 5 + MockMvc (aceitação na API) · Spring Modulith (fronteiras) · Vitest + Testing Library |
| Ambiente | GitHub Codespaces (Dev Container) ou máquina local com Docker |
| Integração contínua | GitHub Actions |

---

## Sumário

1. [Estrutura do repositório](#1-estrutura-do-repositório)
2. [Começar a trabalhar](#2-começar-a-trabalhar)
3. [Comandos do dia a dia](#3-comandos-do-dia-a-dia)
4. [Arquitetura](#4-arquitetura)
5. [Banco de dados](#5-banco-de-dados)
6. [Estudante simulado](#6-estudante-simulado)
7. [Testes e verificação](#7-testes-e-verificação)
8. [Agente de IA e Azure Boards](#8-agente-de-ia-e-azure-boards)
9. [Configuração depois de criar o repositório da equipe](#9-configuração-depois-de-criar-o-repositório-da-equipe)
10. [Decisões deste modelo](#10-decisões-deste-modelo)
11. [Solução de problemas](#11-solução-de-problemas)

---

## 1. Estrutura do repositório

```
<repositorio-da-equipe>/
├── AGENTS.md                      instruções para agentes de IA (Codex e outros)
├── .github/
│   ├── copilot-instructions.md    cópia idêntica do AGENTS.md (GitHub Copilot)
│   ├── pull_request_template.md   evidências exigidas em todo pull request
│   └── workflows/ci.yml           CI: back-end, front-end e instruções sincronizadas
├── .devcontainer/                 ambiente do GitHub Codespaces (Java, Node e PostgreSQL)
├── .vscode/mcp.json               conexão do GitHub Copilot com o Azure Boards (MCP remoto)
├── .codex/config.toml             conexão do Codex com o Azure Boards (MCP local)
├── docker-compose.yml             PostgreSQL para desenvolvimento e testes
├── verificar · verificar.ps1      rodam localmente tudo o que o CI roda
├── backend/                       API — monolito modular em Spring Boot
├── frontend/                      SPA em React, organizada por features
├── docs/
│   ├── adr/                       registros de decisão de arquitetura
│   └── exemplo/                   cenários Gherkin das fatias de exemplo
└── infra/postgres/init/           cria o banco de teste na primeira inicialização
```

---

## 2. Começar a trabalhar

### Opção A — GitHub Codespaces (recomendada)

Nada precisa ser instalado na máquina: Java 21, Node.js 24, Maven (pelo wrapper) e PostgreSQL já vêm no ambiente.

1. No repositório da equipe, clique em **Code › Codespaces › Create codespace on main**.
2. Aguarde a criação. Na primeira vez, o script `.devcontainer/pos-criacao.sh` baixa as dependências, o que leva alguns minutos.
3. No terminal do Codespace, rode `./verificar`. Tudo deve terminar com **"Verificação concluída sem falhas."**
4. Suba a aplicação em dois terminais:
   ```bash
   cd backend && ./mvnw spring-boot:run
   ```
   ```bash
   cd frontend && npm run dev
   ```
5. A aba **Ports** do VS Code mostra os endereços. A interface (porta 5173) abre no navegador.

> **Cota do Codespaces.** Contas com o GitHub Student Developer Pack têm uma cota mensal de horas de uso. Pare o Codespace quando não estiver usando (**Codespaces › Stop current codespace**); ele também para sozinho depois de um período sem atividade.

### Opção B — máquina local (Windows, Linux ou macOS)

**Pré-requisitos:** JDK 21, Node.js 24, Docker Desktop (ou Docker Engine) e VS Code com as extensões recomendadas (o VS Code oferece instalá-las ao abrir a pasta).

```powershell
docker compose up -d db          # sobe o PostgreSQL
.\verificar.ps1                  # Windows (no Linux/macOS: ./verificar)
cd backend; .\mvnw.cmd spring-boot:run
cd frontend; npm install; npm run dev
```

### Endereços

| Endereço | O que é |
|---|---|
| <http://localhost:5173> | Interface (o Vite repassa `/api` para o back-end) |
| <http://localhost:8080/swagger-ui.html> | Contrato da API, navegável |
| <http://localhost:8080/v3/api-docs> | Contrato da API em JSON (OpenAPI) |
| `localhost:5432` | PostgreSQL — usuário `connexa`, senha `connexa`, bancos `connexa` e `connexa_teste` |

---

## 3. Comandos do dia a dia

| Ação | Comando |
|---|---|
| Verificar tudo (o mesmo que o CI) | `./verificar` · Windows: `.\verificar.ps1` |
| Só o back-end / só o front-end | `./verificar backend` · `./verificar frontend` |
| Subir a API | `cd backend && ./mvnw spring-boot:run` |
| Subir a interface | `cd frontend && npm run dev` |
| Testes do front-end em modo contínuo | `cd frontend && npm run test:observar` |
| Diagramas dos módulos | `./verificar backend` e depois abra `backend/target/spring-modulith-docs/` |
| Apagar os dados e recomeçar o banco | `docker compose down -v && docker compose up -d db` |

> **`./verificar` é o `<comando-de-verificacao>`** citado no roteiro da Atividade de Sprint e nas Tasks prontas para agente.

---

## 4. Arquitetura

**Monolito modular com fatias verticais + SPA**, conforme a proposta de arquitetura da disciplina. O ADR-001 de cada equipe registra a adoção ou a adaptação.

### Módulos do back-end

| Módulo | Território | Situação no modelo |
|---|---|---|
| `identidade` | Quem é o estudante atual | Estudante **simulado** (ver seção 6) |
| `perfil` | Perfil acadêmico | Vazio |
| `grupos` | Grupos de estudo e participação | Vazio |
| `encontros` | Encontros dos grupos | Vazio |
| `acompanhamento` | Atividade e inatividade dos grupos | Vazio |
| `painel` | Visões consolidadas | Vazio |
| `materiais` | Materiais compartilhados | Vazio |
| `comum` | Infraestrutura sem regra de negócio: erros, situação, OpenAPI | Pronto |

### Regras de fronteira

1. Cada subpacote de `br.connexa` é um módulo. O **pacote raiz** do módulo é a sua API pública; tudo em `internal/` é privado.
2. Nenhum módulo usa tipos internos de outro, e não há dependência circular. O teste `ArquiteturaModularTest` (Spring Modulith) verifica isso **em todo build**.
3. Cada módulo é dono dos seus dados: um schema por módulo e nenhum acesso às tabelas de outro.
4. Reações entre módulos acontecem por **eventos**, não por chamadas que atravessam os dados de outro módulo.

No front-end vale a mesma ideia: cada pasta de `src/features/` só é usada pelo seu `index.ts`, e o lint bloqueia importações de arquivos internos de outra feature.

### O caminho de uma fatia vertical

```
Cenário Gherkin (Azure Boards)
   ↓
Teste de aceitação     backend/src/test/java/br/connexa/<modulo>/internal/<Historia>AceitacaoTest.java
   ↓
Migração               backend/src/main/resources/db/migration/<modulo>/V<data>__<modulo>_<...>.sql
   ↓
Entidade, repositório, serviço e controller em br.connexa.<modulo>.internal
   ↓
Contrato visível em    /swagger-ui.html
   ↓
Feature no front-end   frontend/src/features/<feature>/ (consultas.ts, componentes, testes)
```

As duas fatias de exemplo seguem exatamente esse caminho. Estude-as antes da primeira Task:

| Fatia | Cenários | Back-end | Front-end |
|---|---|---|---|
| Situação do sistema | `docs/exemplo/situacao-do-sistema.feature` | `comum/internal/SituacaoController` | `features/situacao/` |
| Sessão do estudante simulado | `docs/exemplo/sessao-simulada.feature` | `identidade/internal/` | `features/sessao/` |

### Erros da API

Toda resposta de erro segue o formato *Problem Detail* (RFC 9457) e traz a propriedade `codigo`:

```json
{ "status": 422, "title": "Unprocessable Content", "detail": "O grupo já atingiu o limite de participantes.", "codigo": "GRUPO_LOTADO" }
```

- **Regra de negócio violada:** `throw new ErroDeNegocio(HttpStatus.UNPROCESSABLE_CONTENT, "GRUPO_LOTADO", "...")`
- **Dados inválidos** (`@Valid`): 400, `codigo: "DADOS_INVALIDOS"` e o mapa `campos` com a mensagem de cada campo.
- No front-end, `requisitar` converte a resposta em `ErroDaApi`, com `status`, `codigo` e `campos`.

---

## 5. Banco de dados

| Banco | Uso | Quem cria |
|---|---|---|
| `connexa` | Desenvolvimento (`spring-boot:run`) | variável `POSTGRES_DB` do contêiner |
| `connexa_teste` | Testes automatizados — apagado e recriado a cada execução da suíte | `infra/postgres/init/01-cria-banco-de-teste.sql` |

- Os schemas dos módulos são criados pelo Flyway, a partir da lista em `spring.flyway.schemas` (`application.yml`).
- Tabelas são criadas **somente por migrações**. As convenções estão em `backend/src/main/resources/db/migration/LEIA-ME.md`.
- O Hibernate só confere se as entidades batem com as tabelas (`ddl-auto: validate`); ele nunca altera o banco.
- Conexão: as variáveis `CONNEXA_DB_HOST`, `CONNEXA_DB_PORTA`, `CONNEXA_DB_NOME`, `CONNEXA_DB_USUARIO` e `CONNEXA_DB_SENHA` substituem os padrões.

---

## 6. Estudante simulado

A autenticação real está **fora da Sprint 1**. Enquanto isso, o módulo `identidade` simula o estudante "logado".

- Outros módulos usam apenas a interface `br.connexa.identidade.UsuarioAtual`: `usuarioAtual.obter().id()`.
- Há três estudantes fictícios em `application.yml`: `alice` (padrão), `bento` e `cecilia`.
- O estudante é escolhido pelo cabeçalho `X-Connexa-Usuario-Simulado: <chave>`:
  - na interface, pelo seletor no topo da página;
  - nos testes, com `.header("X-Connexa-Usuario-Simulado", "bento")`;
  - no Swagger, informando o cabeçalho na requisição.

Quando a autenticação real chegar, só a implementação interna de `identidade` muda; os outros módulos continuam usando `UsuarioAtual`.

---

## 7. Testes e verificação

| Verificação | Onde | Ferramenta |
|---|---|---|
| Testes de aceitação na API (um por cenário Gherkin) | `backend/src/test/java/.../internal/*AceitacaoTest.java` | JUnit 5 + MockMvc, anotação `@TesteDeAceitacao` |
| Fronteiras entre módulos | `ArquiteturaModularTest` | Spring Modulith |
| Um schema por módulo | `SchemasDosModulosTest` | JUnit 5 + JDBC |
| Lint, tipos e fronteiras entre features | `frontend/eslint.config.js`, `tsconfig.app.json` | ESLint + TypeScript |
| Testes de componente | `frontend/src/**/*.test.tsx` | Vitest + Testing Library |
| Instruções do agente sincronizadas | `AGENTS.md` = `.github/copilot-instructions.md` | `cmp` |

**`@TesteDeAceitacao`** sobe a aplicação inteira contra o banco `connexa_teste` e executa cada teste dentro de uma transação desfeita ao final, de modo que um teste nunca vê os dados de outro.

**Convenção de nomes:** uma classe por história e um método por cenário, com `@DisplayName("Cenário: <nome exato do cenário>")`. No front-end, `it('Cenário: <nome exato>')`.

> **Nunca desative uma verificação** para o build passar. Teste ignorado, asserção removida ou regra de lint desligada transformam a evidência em mentira e invalidam o trabalho da Task.

---

## 8. Agente de IA e Azure Boards

O agente lê as Tasks do Azure Boards por MCP. As instruções que ele segue estão em `AGENTS.md` (Codex) e `.github/copilot-instructions.md` (Copilot) — mantenha os dois idênticos; o CI confere.

| Caminho | Agente | Servidor MCP | Arquivo |
|---|---|---|---|
| 1 | GitHub Copilot no VS Code | Remoto, com login da conta institucional | `.vscode/mcp.json` — conexões `ado-planejamento` (escrita) e `ado-leitura` (somente leitura) |
| 2 | Codex no VS Code | Local, via `npx` (requer Node.js 20+) | `.codex/config.toml` |

Os detalhes de cada caminho, os prompts de referência e o protocolo de falha estão no roteiro da Atividade de Sprint.

---

## 9. Configuração depois de criar o repositório da equipe

A Parte 1 do roteiro da Atividade de Sprint (Etapas 3 e 4) detalha estes passos. Os nomes entre `< >` são escolhidos pela equipe; só o projeto do Azure DevOps tem nome fixo: `Connexa`.

1. **Nome da organização do Azure DevOps:** substitua `<organizacao-azure-devops>` em `.vscode/mcp.json` e `.codex/config.toml`. A organização deve ter sido criada com a conta institucional (Microsoft Entra ID).
2. **Proteção do `main`** (**Settings › Rules › Rulesets** ou **Settings › Branches**):
   - exigir pull request antes do merge, com **1 aprovação**;
   - exigir os checks **Back-end (testes e fronteiras)**, **Front-end (lint, testes e build)** e **Instruções do agente sincronizadas** — eles aparecem na lista depois da primeira execução do CI;
   - bloquear *force push* e exclusão do branch.
3. **Azure Boards ↔ GitHub:** conecte o projeto `Connexa` a `<repositorio-da-equipe>`, em `<organizacao-github>` (**Project Settings › Boards › GitHub connections**), para que `AB#<id>` em commits e pull requests crie o vínculo com o work item.
4. **ADR-001:** registre em `docs/adr/` a arquitetura e a stack adotadas, usando `docs/adr/modelo-adr.md`.

---

## 10. Decisões deste modelo

| Decisão | Motivo |
|---|---|
| Testes usam o PostgreSQL do Compose, em um banco separado (`connexa_teste`) recriado pelo Flyway a cada execução | Funciona igual no Codespaces, no CI e na máquina local sem exigir Docker dentro do Codespace. A proposta de arquitetura sugere Testcontainers; a equipe pode adotá-lo em um ADR |
| `network_mode: service:db` no Codespaces | O banco responde em `localhost:5432` dentro e fora do Codespaces, e nenhuma configuração muda entre os ambientes |
| Sem Spring Security na Sprint 1 | A autenticação está fora da sprint. Com o starter, todas as rotas ficariam bloqueadas por padrão |
| `UsuarioAtual` como API do módulo `identidade` | Os outros módulos não dependem de como a autenticação será feita |
| Dependências da sprint já incluídas (validação, JPA, Flyway, React Hook Form, Zod) | As Tasks proíbem o agente de incluir dependências; o que a equipe precisa já está disponível |
| Sem testes de ponta a ponta no navegador (Playwright) | Fora do escopo da Sprint 1; o roteiro da Atividade de Sprint exige verificação manual dos cenários na interface |
| Versões fixas no `package.json` e no `pom.xml` | O build é reprodutível; atualizar é uma decisão registrada em ADR |

---

## 11. Solução de problemas

| Sintoma | O que fazer |
|---|---|
| Testes do back-end falham com `Connection refused` | O PostgreSQL não está rodando. Fora do Codespaces: `docker compose up -d db` |
| `database "connexa_teste" does not exist` | O volume foi criado antes do script de inicialização. Rode `docker compose down -v` e `docker compose up -d db` (apaga os dados de desenvolvimento) |
| `./mvnw: Permission denied` | `chmod +x backend/mvnw verificar` |
| `ArquiteturaModularTest` falha | Um módulo usou um tipo interno de outro ou há dependência circular. A mensagem indica as classes. Refaça a mudança respeitando a fronteira — nunca altere o teste |
| O lint acusa importação restrita | Importe a feature pelo `index.ts` (`@/features/<feature>`) e use o alias `@/` |
| A interface mostra "O back-end não está respondendo" | Suba a API em outro terminal: `cd backend && ./mvnw spring-boot:run` |
| Porta 5432 ocupada na máquina local | Outro PostgreSQL está instalado. Pare-o ou troque a porta publicada no `docker-compose.yml` e use `CONNEXA_DB_PORTA` |

---

*Modelo de repositório — Disciplina de Engenharia de Software — Projeto Connexa.*
