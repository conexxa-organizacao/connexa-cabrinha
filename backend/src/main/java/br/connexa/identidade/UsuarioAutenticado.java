package br.connexa.identidade;

import java.util.UUID;

/**
 * Estudante identificado na requisição atual.
 *
 * <p>Outros módulos devem guardar apenas o {@code id} — nunca copiar nome ou e-mail
 * para as próprias tabelas.
 *
 * @param id    identificador estável do estudante
 * @param nome  nome para exibição
 * @param email e-mail institucional
 */
public record UsuarioAutenticado(UUID id, String nome, String email) {
}
