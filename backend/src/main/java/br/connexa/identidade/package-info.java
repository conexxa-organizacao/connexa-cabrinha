/**
 * Módulo <b>Identidade</b>: quem é o estudante que está usando o Connexa.
 *
 * <p><b>Sprint 1 — autenticação simulada.</b> A autenticação real (cadastro, comprovação
 * de e-mail, senha) está fora da Sprint 1. Até lá, a API pública deste módulo
 * ({@link br.connexa.identidade.UsuarioAtual}) devolve um estudante simulado,
 * configurado em {@code application.yml}.
 *
 * <p>Os outros módulos dependem apenas de {@code UsuarioAtual}. Quando a autenticação
 * real chegar, só a implementação interna deste módulo muda — nenhum outro módulo
 * precisa ser alterado.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Identidade")
package br.connexa.identidade;
