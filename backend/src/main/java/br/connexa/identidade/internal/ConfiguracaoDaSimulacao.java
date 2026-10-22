package br.connexa.identidade.internal;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Estudantes simulados disponíveis enquanto a autenticação real não existe.
 * Valores em {@code connexa.identidade.simulacao} no {@code application.yml}.
 *
 * @param usuarios estudantes simulados; o primeiro é o padrão
 */
@ConfigurationProperties("connexa.identidade.simulacao")
public record ConfiguracaoDaSimulacao(List<EstudanteSimulado> usuarios) {

    public ConfiguracaoDaSimulacao {
        if (usuarios == null || usuarios.isEmpty()) {
            throw new IllegalStateException(
                    "Configure ao menos um estudante em connexa.identidade.simulacao.usuarios");
        }
        usuarios = List.copyOf(usuarios);
    }

    /**
     * @param chave identificador curto usado no cabeçalho {@code X-Connexa-Usuario-Simulado}
     */
    public record EstudanteSimulado(String chave, UUID id, String nome, String email) {
    }
}
