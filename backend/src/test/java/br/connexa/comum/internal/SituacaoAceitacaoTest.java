package br.connexa.comum.internal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.connexa.TesteDeAceitacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Exemplo do modelo — cenários em {@code docs/exemplo/situacao-do-sistema.feature}.
 */
@TesteDeAceitacao
@DisplayName("Funcionalidade: situação do sistema")
class SituacaoAceitacaoTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Cenário: aplicação e banco disponíveis")
    void aplicacao_e_banco_disponiveis() throws Exception {
        mvc.perform(get("/api/situacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aplicacao").value("disponivel"))
                .andExpect(jsonPath("$.banco").value("disponivel"))
                .andExpect(jsonPath("$.versao").isNotEmpty());
    }
}
