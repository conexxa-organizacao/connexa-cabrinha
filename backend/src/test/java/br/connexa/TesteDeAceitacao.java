package br.connexa;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Marca uma classe de testes de aceitação no nível da API.
 *
 * <ul>
 *   <li>Sobe a aplicação inteira, com um {@code MockMvc} para fazer as requisições.</li>
 *   <li>Usa o perfil {@code teste}, com o banco {@code connexa_teste} recriado pelo Flyway.</li>
 *   <li>Cada teste roda em uma transação desfeita ao final: um teste nunca vê os dados de outro.</li>
 * </ul>
 *
 * <p>Convenção: uma classe por história, um método por cenário Gherkin, com
 * {@code @DisplayName("Cenário: <nome exato do cenário>")}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
@Import(BancoDeTesteLimpo.class)
@Transactional
public @interface TesteDeAceitacao {
}
