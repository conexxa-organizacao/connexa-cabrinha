# Migrações do banco (Flyway)

O esquema do banco é criado **somente** por migrações. Nunca altere tabelas à mão nem use `ddl-auto: update`.

## Onde e como criar

- Uma pasta por módulo: `db/migration/<modulo>/` (por exemplo, `db/migration/grupos/`).
- Nome do arquivo: `V<AAAAMMDD>_<HHMM>__<modulo>_<o_que_faz>.sql`
  - Exemplo: `V20261020_1430__grupos_cria_tabela_grupo.sql`
  - A data e a hora no número da versão evitam que duas pessoas criem a mesma versão em branches diferentes.
- Toda tabela fica no schema do próprio módulo: `create table grupos.grupo (...)`.
- Na entidade JPA, declare o schema: `@Table(schema = "grupos", name = "grupo")`.

## O que não fazer

- Não edite uma migração que já foi integrada no `main`. Para mudar algo, crie uma nova migração.
- Não crie chave estrangeira para tabela de outro módulo. Guarde apenas o identificador (por exemplo, `estudante_id uuid`).
- Não leia nem escreva tabelas de outro módulo.

Os schemas de todos os módulos já existem: o Flyway os cria a partir da lista em `spring.flyway.schemas` (`application.yml`).
