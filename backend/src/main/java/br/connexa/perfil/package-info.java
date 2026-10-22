/**
 * Módulo <b>Perfil</b>.
 *
 * <p>Perfil acadêmico do estudante: curso, disciplinas de interesse, disponibilidade e preferências de estudo.
 *
 * <p>Regras deste módulo (valem para todos):
 * <ul>
 *   <li>Somente os tipos públicos deste pacote formam a API do módulo; subpacotes
 *       (por exemplo, {@code internal}) não podem ser usados por outros módulos.</li>
 *   <li>Os dados do módulo ficam no schema {@code perfil} do PostgreSQL e só este módulo os acessa.</li>
 *   <li>Reações a fatos de outros módulos acontecem por eventos, nunca por acesso direto aos dados.</li>
 * </ul>
 *
 * <p>O módulo começa vazio. As fatias verticais são criadas a partir das Tasks do Azure Boards.
 */
@org.springframework.modulith.ApplicationModule(displayName = "Perfil")
package br.connexa.perfil;
