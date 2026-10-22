package br.connexa.identidade.internal;

import java.util.List;
import java.util.UUID;

import br.connexa.identidade.internal.ConfiguracaoDaSimulacao.EstudanteSimulado;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de apoio à simulação: qual estudante está "logado" e quais podem ser escolhidos.
 * O front-end usa estes endpoints no seletor de estudante simulado.
 */
@RestController
@RequestMapping("/api/identidade")
class SessaoSimuladaController {

    private final UsuarioSimulado usuarioSimulado;

    SessaoSimuladaController(UsuarioSimulado usuarioSimulado) {
        this.usuarioSimulado = usuarioSimulado;
    }

    @GetMapping("/sessao")
    Sessao sessao() {
        return Sessao.de(usuarioSimulado.estudanteDaRequisicao());
    }

    @GetMapping("/usuarios-simulados")
    List<Sessao> usuariosSimulados() {
        return usuarioSimulado.todos().stream().map(Sessao::de).toList();
    }

    public record Sessao(String chave, UUID id, String nome, String email, boolean simulado) {

        static Sessao de(EstudanteSimulado estudante) {
            return new Sessao(estudante.chave(), estudante.id(), estudante.nome(), estudante.email(), true);
        }
    }
}
