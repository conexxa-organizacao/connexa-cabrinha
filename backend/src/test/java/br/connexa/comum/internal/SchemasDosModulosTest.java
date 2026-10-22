package br.connexa.comum.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import br.connexa.TesteDeAceitacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;

/**
 * Garante que o banco tem um schema para cada módulo (regra: cada módulo é dono dos seus dados).
 */
@TesteDeAceitacao
@DisplayName("Arquitetura: um schema por módulo")
class SchemasDosModulosTest {

    static final List<String> MODULOS = List.of(
            "identidade", "perfil", "grupos", "encontros", "acompanhamento", "painel", "materiais");

    @Autowired
    JdbcClient banco;

    @Test
    @DisplayName("cada módulo tem o seu schema no banco")
    void cada_modulo_tem_o_seu_schema() {
        List<String> schemas = banco
                .sql("select schema_name from information_schema.schemata")
                .query(String.class)
                .list();

        assertThat(schemas).containsAll(MODULOS);
    }
}
