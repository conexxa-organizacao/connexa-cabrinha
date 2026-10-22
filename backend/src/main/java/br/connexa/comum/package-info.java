/**
 * Módulo <b>Comum</b>: infraestrutura compartilhada, <b>sem regra de negócio</b>.
 *
 * <p>Contém o padrão de erros da API ({@link br.connexa.comum.ErroDeNegocio}),
 * a consulta de situação do sistema e a documentação OpenAPI.
 *
 * <p>Não coloque aqui entidades, serviços ou regras de nenhum território do Connexa.
 * "Pode ser útil para outros módulos no futuro" não é motivo para algo morar em {@code comum}.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Comum")
package br.connexa.comum;
