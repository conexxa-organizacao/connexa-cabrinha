package br.connexa;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Verificação automática das fronteiras entre módulos.
 *
 * <p>Falha quando um módulo usa um tipo interno de outro módulo (qualquer subpacote,
 * como {@code grupos.internal}) ou quando há dependência circular entre módulos.
 *
 * <p><b>Nunca desative, ignore ou altere este teste para o build passar.</b>
 * Se ele falhar, a mudança precisa ser refeita respeitando as fronteiras.
 */
@DisplayName("Arquitetura: monolito modular")
class ArquiteturaModularTest {

    static final ApplicationModules MODULOS = ApplicationModules.of(ConnexaApplication.class);

    @Test
    @DisplayName("os módulos respeitam as fronteiras e não têm dependências circulares")
    void modulos_respeitam_as_fronteiras() {
        MODULOS.verify();
    }

    @Test
    @DisplayName("gera os diagramas dos módulos em target/spring-modulith-docs")
    void gera_diagramas_dos_modulos() {
        new Documenter(MODULOS)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}
