package br.connexa;

import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Antes da suíte de testes, apaga os schemas do banco de teste e aplica todas
 * as migrações do zero. Assim, sobras de execuções anteriores (ou de outro branch)
 * nunca interferem no resultado.
 *
 * <p>Só existe no código de teste: o banco de desenvolvimento nunca é apagado.
 */
@TestConfiguration(proxyBeanMethods = false)
public class BancoDeTesteLimpo {

    @Bean
    FlywayMigrationStrategy apagarERecriarOBancoDeTeste() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
