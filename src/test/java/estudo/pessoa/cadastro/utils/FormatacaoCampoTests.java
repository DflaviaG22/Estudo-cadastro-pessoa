package estudo.pessoa.cadastro.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Formatação de Campos")
class FormatacaoCampoTests {

	@Test
	@DisplayName("Deve formatar CPF com números")
	void testFormatarCpfComNumeros() {
		String resultado = FormatacaoCampo.formatarCpf("12345678900");
		
		assertEquals("123.456.789-00", resultado);
	}

	@Test
	@DisplayName("Deve formatar CPF já formatado")
	void testFormatarCpfJaFormatado() {
		String resultado = FormatacaoCampo.formatarCpf("123.456.789-00");
		
		assertEquals("123.456.789-00", resultado);
	}

	@Test
	@DisplayName("Deve formatar CPF com caracteres especiais")
	void testFormatarCpfComCaracteresEspeciais() {
		String resultado = FormatacaoCampo.formatarCpf("123-456-789-00");
		
		assertEquals("123.456.789-00", resultado);
	}

	@Test
	@DisplayName("Deve formatar telefone com 11 dígitos")
	void testFormatarTelefoneOnze() {
		String resultado = FormatacaoCampo.formatarTelefone("11987654321");
		
		assertEquals("(11) 98765-4321", resultado);
	}

	@Test
	@DisplayName("Deve formatar telefone com 10 dígitos")
	void testFormatarTelefoneDez() {
		String resultado = FormatacaoCampo.formatarTelefone("1133334444");
		
		assertEquals("(11) 3333-4444", resultado);
	}

	@Test
	@DisplayName("Deve formatar telefone já formatado")
	void testFormatarTelefoneJaFormatado() {
		String resultado = FormatacaoCampo.formatarTelefone("(11) 98765-4321");
		
		assertEquals("(11) 98765-4321", resultado);
	}

	@Test
	@DisplayName("Deve remover máscara de CPF")
	void testRemoverMascaraCpf() {
		String cpfComMascara = "123.456.789-00";
		String resultado = cpfComMascara.replaceAll("\\D", "");
		
		assertEquals("12345678900", resultado);
	}

	@Test
	@DisplayName("Deve remover máscara de telefone")
	void testRemoverMascaraTelefone() {
		String telefoneComMascara = "(11) 98765-4321";
		String resultado = telefoneComMascara.replaceAll("\\D", "");
		
		assertEquals("11987654321", resultado);
	}

	@Test
	@DisplayName("Deve lidar com CPF nulo")
	void testFormatarCpfNulo() {
		assertDoesNotThrow(() -> {
			FormatacaoCampo.formatarCpf(null);
		});
	}

	@Test
	@DisplayName("Deve lidar com telefone nulo")
	void testFormatarTelefoneNulo() {
		assertDoesNotThrow(() -> {
			FormatacaoCampo.formatarTelefone(null);
		});
	}

	@Test
	@DisplayName("Deve formatar CPF vazio")
	void testFormatarCpfVazio() {
		String resultado = FormatacaoCampo.formatarCpf("");
		
		assertNotNull(resultado);
	}

	@Test
	@DisplayName("Deve formatar telefone vazio")
	void testFormatarTelefoneVazio() {
		String resultado = FormatacaoCampo.formatarTelefone("");
		
		assertNotNull(resultado);
	}
}
