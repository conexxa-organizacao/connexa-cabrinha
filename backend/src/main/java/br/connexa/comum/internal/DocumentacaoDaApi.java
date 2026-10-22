package br.connexa.comum.internal;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadados da documentação OpenAPI.
 *
 * <p>Contrato em JSON: {@code /v3/api-docs} · Interface navegável: {@code /swagger-ui.html}.
 */
@Configuration(proxyBeanMethods = false)
class DocumentacaoDaApi {

    @Bean
    OpenAPI documentacaoDoConnexa(@Value("${connexa.versao}") String versao) {
        return new OpenAPI()
                .addServersItem(new Server().url("/").description("Servidor atual"))
                .info(new Info()
                        .title("Connexa — API")
                        .version(versao)
                        .description("API do Connexa. Cada operação pertence a um módulo; o prefixo da rota indica qual."));
    }
}
