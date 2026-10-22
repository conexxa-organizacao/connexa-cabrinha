package br.connexa.comum.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Fatia de exemplo do modelo: informa se a aplicação e o banco estão disponíveis.
 *
 * <p>Não é uma funcionalidade do Connexa. Existe para mostrar, de ponta a ponta,
 * o caminho que toda fatia percorre: endpoint, teste de aceitação com o nome do
 * cenário e tela no front-end.
 */
@RestController
@RequestMapping("/api/situacao")
class SituacaoController {

    static final String DISPONIVEL = "disponivel";
    static final String INDISPONIVEL = "indisponivel";

    private final JdbcClient banco;
    private final String versao;

    SituacaoController(JdbcClient banco, @Value("${connexa.versao}") String versao) {
        this.banco = banco;
        this.versao = versao;
    }

    @GetMapping
    ResponseEntity<Situacao> consultar() {
        boolean bancoDisponivel = bancoResponde();
        Situacao situacao = new Situacao(DISPONIVEL, bancoDisponivel ? DISPONIVEL : INDISPONIVEL, versao);
        HttpStatus status = bancoDisponivel ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(situacao);
    }

    private boolean bancoResponde() {
        try {
            return banco.sql("select 1").query(Integer.class).single() == 1;
        } catch (RuntimeException falha) {
            return false;
        }
    }

    public record Situacao(String aplicacao, String banco, String versao) {
    }
}
