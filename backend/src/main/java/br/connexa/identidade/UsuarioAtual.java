package br.connexa.identidade;

/**
 * API pública do módulo Identidade: informa qual estudante fez a requisição atual.
 *
 * <p>Use sempre esta interface para saber "quem está usando o sistema".
 * Nunca leia cabeçalhos, tokens ou sessões diretamente em outros módulos.
 *
 * <pre>{@code
 * @RestController
 * class MeusGruposController {
 *     private final UsuarioAtual usuarioAtual;
 *     ...
 *     UUID estudante = usuarioAtual.obter().id();
 * }
 * }</pre>
 */
public interface UsuarioAtual {

    /**
     * @return o estudante da requisição atual
     */
    UsuarioAutenticado obter();
}
