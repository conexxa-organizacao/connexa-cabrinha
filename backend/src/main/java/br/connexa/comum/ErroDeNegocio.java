package br.connexa.comum;

import org.springframework.http.HttpStatus;

/**
 * Violação de uma regra de negócio que deve chegar ao cliente como resposta de erro.
 *
 * <p>É convertida em um <i>Problem Detail</i> (RFC 9457) com a propriedade
 * {@code codigo}, que os testes de aceitação e o front-end usam para identificar
 * o erro sem depender do texto da mensagem.
 *
 * <pre>{@code
 * throw new ErroDeNegocio(HttpStatus.UNPROCESSABLE_CONTENT,
 *         "GRUPO_LOTADO", "O grupo já atingiu o número máximo de participantes.");
 * }</pre>
 *
 * <p>Convenções: {@code codigo} em MAIÚSCULAS_COM_SUBLINHADO; 404 para recurso
 * inexistente, 409 para conflito de estado, 422 para regra de negócio violada.
 */
public class ErroDeNegocio extends RuntimeException {

    private final HttpStatus status;
    private final String codigo;

    public ErroDeNegocio(HttpStatus status, String codigo, String mensagem) {
        super(mensagem);
        this.status = status;
        this.codigo = codigo;
    }

    public HttpStatus status() {
        return status;
    }

    public String codigo() {
        return codigo;
    }
}
