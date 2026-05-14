package estudo.pessoa.cadastro.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Testes de validacao de CPF")
class ValidadorCpfTests {

    @Test
    @DisplayName("Deve validar CPF correto com e sem mascara")
    void testValidarCpfValido() {
        assertTrue(ValidadorCpf.validarCpf("529.982.247-25"));
        assertTrue(ValidadorCpf.validarCpf("52998224725"));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com digitos verificadores invalidos")
    void testValidarCpfInvalido() {
        assertFalse(ValidadorCpf.validarCpf("123.456.789-00"));
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo ou vazio")
    void testValidarCpfNuloOuVazio() {
        assertFalse(ValidadorCpf.validarCpf(null));
        assertFalse(ValidadorCpf.validarCpf(""));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com tamanho invalido ou digitos repetidos")
    void testValidarCpfComTamanhoInvalidoOuRepetido() {
        assertFalse(ValidadorCpf.validarCpf("123456789"));
        assertFalse(ValidadorCpf.validarCpf("123456789001234"));
        assertFalse(ValidadorCpf.validarCpf("111.111.111-11"));
    }
}
