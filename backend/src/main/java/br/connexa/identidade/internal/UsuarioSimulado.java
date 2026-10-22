package br.connexa.identidade.internal;

import java.util.List;
import java.util.Optional;

import br.connexa.comum.ErroDeNegocio;
import br.connexa.identidade.UsuarioAtual;
import br.connexa.identidade.UsuarioAutenticado;
import br.connexa.identidade.internal.ConfiguracaoDaSimulacao.EstudanteSimulado;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Implementação temporária de {@link UsuarioAtual} para a Sprint 1.
 *
 * <p>O estudante é escolhido pelo cabeçalho {@value #CABECALHO}, cujo valor é a
 * {@code chave} de um estudante configurado. Sem o cabeçalho, vale o primeiro
 * estudante da lista. Isso permite testar cenários com mais de um estudante
 * (por exemplo, um cria o grupo e outro entra nele).
 *
 * <p>Será substituída pela autenticação real em uma sprint futura.
 */
@Component
class UsuarioSimulado implements UsuarioAtual {

    static final String CABECALHO = "X-Connexa-Usuario-Simulado";
    static final String USUARIO_SIMULADO_INEXISTENTE = "USUARIO_SIMULADO_INEXISTENTE";

    private final List<EstudanteSimulado> estudantes;

    UsuarioSimulado(ConfiguracaoDaSimulacao configuracao) {
        this.estudantes = configuracao.usuarios();
    }

    @Override
    public UsuarioAutenticado obter() {
        EstudanteSimulado estudante = estudanteDaRequisicao();
        return new UsuarioAutenticado(estudante.id(), estudante.nome(), estudante.email());
    }

    /**
     * @return o estudante simulado da requisição atual, com a sua chave
     */
    EstudanteSimulado estudanteDaRequisicao() {
        return chaveInformada()
                .map(this::buscarPelaChave)
                .orElse(estudantes.getFirst());
    }

    List<EstudanteSimulado> todos() {
        return estudantes;
    }

    private EstudanteSimulado buscarPelaChave(String chave) {
        return estudantes.stream()
                .filter(estudante -> estudante.chave().equals(chave))
                .findFirst()
                .orElseThrow(() -> new ErroDeNegocio(
                        HttpStatus.BAD_REQUEST,
                        USUARIO_SIMULADO_INEXISTENTE,
                        "Não existe estudante simulado com a chave '" + chave + "'."));
    }

    private static Optional<String> chaveInformada() {
        RequestAttributes atributos = RequestContextHolder.getRequestAttributes();
        if (atributos instanceof ServletRequestAttributes requisicao) {
            String valor = requisicao.getRequest().getHeader(CABECALHO);
            if (valor != null && !valor.isBlank()) {
                return Optional.of(valor.trim());
            }
        }
        return Optional.empty();
    }
}
