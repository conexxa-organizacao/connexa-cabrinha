package br.connexa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Ponto de entrada do back-end do Connexa.
 *
 * <p>Cada subpacote direto de {@code br.connexa} é um módulo da aplicação
 * (Spring Modulith). As fronteiras entre eles são verificadas pelo teste
 * {@code ArquiteturaModularTest} em todo build.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class ConnexaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnexaApplication.class, args);
    }
}
