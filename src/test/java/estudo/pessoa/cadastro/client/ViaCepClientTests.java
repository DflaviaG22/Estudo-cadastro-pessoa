package estudo.pessoa.cadastro.client;

import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do Cliente ViaCEP")
class ViaCepClientTests {

	@Autowired
	private ViaCepClient viaCepClient;

	@Test
	@DisplayName("Deve consultar CEP válido")
	void testConsultarCepValido() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("01310100");

		assertNotNull(resultado);
		assertEquals("01310-100", resultado.getCep());
	}

	@Test
	@DisplayName("Deve consultar CEP e retornar logradouro")
	void testConsultarCepRetornaLogradouro() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("01310100");

		assertNotNull(resultado.getLogradouro());
		assertFalse(resultado.getLogradouro().isEmpty());
	}

	@Test
	@DisplayName("Deve consultar CEP e retornar bairro")
	void testConsultarCepRetornaBairro() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("01310100");

		assertNotNull(resultado.getBairro());
		assertFalse(resultado.getBairro().isEmpty());
	}

	@Test
	@DisplayName("Deve consultar CEP e retornar UF")
	void testConsultarCepRetornaUf() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("01310100");

		assertNotNull(resultado.getUf());
		assertEquals("SP", resultado.getUf());
	}

	@Test
	@DisplayName("Deve retornar erro para CEP inválido")
	void testConsultarCepInvalido() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("99999999");

		assertTrue(resultado.getErro());
	}

	@Test
	@DisplayName("Deve consultar CEP e retornar DDD")
	void testConsultarCepRetornaDdd() {
		EnderecoViaCepResponse resultado = viaCepClient.consultarCep("01310100");

		assertNotNull(resultado.getDdd());
		assertFalse(resultado.getDdd().isEmpty());
	}

	@Test
	@DisplayName("Deve consultar CEP diferentes")
	void testConsultarMultiplosCeps() {
		EnderecoViaCepResponse resultado1 = viaCepClient.consultarCep("01310100");
		EnderecoViaCepResponse resultado2 = viaCepClient.consultarCep("20040020");

		assertNotNull(resultado1);
		assertNotNull(resultado2);
		assertNotEquals(resultado1.getCep(), resultado2.getCep());
	}
}
