package estudo.pessoa.cadastro.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testes de formatacao de campos")
class FormatacaoCampoTests {

    @Test
    @DisplayName("Deve formatar CPF removendo caracteres nao numericos")
    void testFormatarCpf() {
        assertEquals("123.456.789-00", FormatacaoCampo.formatarCpf("12345678900"));
        assertEquals("123.456.789-00", FormatacaoCampo.formatarCpf("123.456.789-00"));
        assertEquals("123.456.789-00", FormatacaoCampo.formatarCpf("123-456-789-00"));
    }

    @Test
    @DisplayName("Deve formatar telefone celular e fixo")
    void testFormatarTelefone() {
        assertEquals("(11) 98765-4321", FormatacaoCampo.formatarTelefone("11987654321"));
        assertEquals("(11) 3333-4444", FormatacaoCampo.formatarTelefone("1133334444"));
        assertEquals("(11) 98765-4321", FormatacaoCampo.formatarTelefone("(11) 98765-4321"));
    }

    @Test
    @DisplayName("Deve retornar vazio para CPF nulo")
    void testFormatarCpfNulo() {
        assertEquals("", FormatacaoCampo.formatarCpf(null));
    }

    @Test
    @DisplayName("Deve retornar vazio para telefone nulo")
    void testFormatarTelefoneNulo() {
        assertEquals("", FormatacaoCampo.formatarTelefone(null));
    }
}
