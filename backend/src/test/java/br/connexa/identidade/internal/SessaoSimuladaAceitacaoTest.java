package br.connexa.identidade.internal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.connexa.TesteDeAceitacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Exemplo do modelo — cenários em {@code docs/exemplo/sessao-simulada.feature}.
 *
 * <p>Repare: um método por cenário, com o nome do cenário no {@code @DisplayName};
 * cada teste verifica o resultado observável descrito no cenário, incluindo o
 * {@code codigo} do erro — e não apenas o status HTTP.
 */
@TesteDeAceitacao
@DisplayName("Funcionalidade: sessão do estudante simulado")
class SessaoSimuladaAceitacaoTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Cenário: sem indicação, a sessão é do primeiro estudante simulado")
    void sem_indicacao_a_sessao_e_do_primeiro_estudante() throws Exception {
        mvc.perform(get("/api/identidade/sessao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chave").value("alice"))
                .andExpect(jsonPath("$.nome").value("Alice Prado"))
                .andExpect(jsonPath("$.simulado").value(true));
    }

    @Test
    @DisplayName("Cenário: estudante simulado escolhido pelo cabeçalho")
    void estudante_escolhido_pelo_cabecalho() throws Exception {
        mvc.perform(get("/api/identidade/sessao").header(UsuarioSimulado.CABECALHO, "bento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chave").value("bento"))
                .andExpect(jsonPath("$.nome").value("Bento Ramos"));
    }

    @Test
    @DisplayName("Cenário: estudante simulado inexistente")
    void estudante_simulado_inexistente() throws Exception {
        mvc.perform(get("/api/identidade/sessao").header(UsuarioSimulado.CABECALHO, "zeca"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value(UsuarioSimulado.USUARIO_SIMULADO_INEXISTENTE));
    }

    @Test
    @DisplayName("Cenário: lista dos estudantes simulados disponíveis")
    void lista_dos_estudantes_simulados() throws Exception {
        mvc.perform(get("/api/identidade/usuarios-simulados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].chave").value("alice"));
    }
}
