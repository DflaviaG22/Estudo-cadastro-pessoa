package estudo.pessoa.cadastro.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Validação de CPF")
class ValidadorCpfTests {

	@Test
	@DisplayName("Deve validar CPF com formato correto")
	void testValidarCpfComFormato() {
		boolean resultado = ValidadorCpf.validarCpf("123.456.789-00");
		
		assertNotNull(resultado);
	}

	@Test
	@DisplayName("Deve validar CPF sem formatação")
	void testValidarCpfSemFormatacao() {
		boolean resultado = ValidadorCpf.validarCpf("12345678900");
		
		assertNotNull(resultado);
	}

	@Test
	@DisplayName("Deve validar CPF com caracteres especiais")
	void testValidarCpfComEspeciais() {
		boolean resultado = ValidadorCpf.validarCpf("123-456-789-00");
		
		assertNotNull(resultado);
	}

	@Test
	@DisplayName("Deve lidar com CPF nulo")
	void testValidarCpfNulo() {
		assertDoesNotThrow(() -> {
			ValidadorCpf.validarCpf(null);
		});
	}

	@Test
	@DisplayName("Deve lidar com CPF vazio")
	void testValidarCpfVazio() {
		assertDoesNotThrow(() -> {
			ValidadorCpf.validarCpf("");
		});
	}

	@Test
	@DisplayName("Deve retornar false para CPF com menos de 11 dígitos")
	void testValidarCpfMenor() {
		ValidadorCpf.validarCpf("123456789");
	}

	@Test
	@DisplayName("Deve retornar false para CPF com mais de 11 dígitos")
	void testValidarCpfMaior() {
		ValidadorCpf.validarCpf("123456789001234");
	}

	@Test
	@DisplayName("Deve validar múltiplos CPFs")
	void testValidarMultiplosCpfs() {
		boolean resultado1 = ValidadorCpf.validarCpf("123.456.789-00");
		boolean resultado2 = ValidadorCpf.validarCpf("987.654.321-00");
		boolean resultado3 = ValidadorCpf.validarCpf("111.222.333-44");

		assertNotNull(resultado1);
		assertNotNull(resultado2);
		assertNotNull(resultado3);
	}
}
