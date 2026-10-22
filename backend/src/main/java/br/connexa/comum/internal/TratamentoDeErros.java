package br.connexa.comum.internal;

import java.util.LinkedHashMap;
import java.util.Map;

import br.connexa.comum.ErroDeNegocio;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Converte erros em respostas no formato <i>Problem Detail</i> (RFC 9457),
 * sempre com a propriedade {@code codigo}.
 *
 * <ul>
 *   <li>{@link ErroDeNegocio}: status e código definidos por quem lançou o erro.</li>
 *   <li>Falha de validação ({@code @Valid}): 400, código {@code DADOS_INVALIDOS}
 *       e a propriedade {@code campos} com a mensagem de cada campo inválido.</li>
 * </ul>
 */
@RestControllerAdvice
class TratamentoDeErros extends ResponseEntityExceptionHandler {

    static final String DADOS_INVALIDOS = "DADOS_INVALIDOS";

    @ExceptionHandler(ErroDeNegocio.class)
    ResponseEntity<ProblemDetail> tratarErroDeNegocio(ErroDeNegocio erro) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(erro.status(), erro.getMessage());
        problema.setTitle(erro.status().getReasonPhrase());
        problema.setProperty("codigo", erro.codigo());
        return ResponseEntity.status(erro.status()).body(problema);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException erro,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError campo : erro.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(campo.getField(), campo.getDefaultMessage());
        }

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Um ou mais campos estão inválidos.");
        problema.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        problema.setProperty("codigo", DADOS_INVALIDOS);
        problema.setProperty("campos", campos);
        return ResponseEntity.badRequest().body(problema);
    }
}
