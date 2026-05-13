package estudo.pessoa.cadastro.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes do DTO EnderecoViaCepResponse")
class EnderecoViaCepResponseTests {

	private EnderecoViaCepResponse endereco;

	@BeforeEach
	void setUp() {
		endereco = new EnderecoViaCepResponse();
	}

	@Test
	@DisplayName("Deve criar instância vazia")
	void testCriarInstanciaVazia() {
		assertNotNull(endereco);
	}

	@Test
	@DisplayName("Deve setar e obter CEP")
	void testSetEObterCep() {
		endereco.setCep("01310-100");
		
		assertEquals("01310-100", endereco.getCep());
	}

	@Test
	@DisplayName("Deve setar e obter logradouro")
	void testSetEObterLogradouro() {
		endereco.setLogradouro("Avenida Paulista");
		
		assertEquals("Avenida Paulista", endereco.getLogradouro());
	}

	@Test
	@DisplayName("Deve setar e obter complemento")
	void testSetEObterComplemento() {
		endereco.setComplemento("de 971/972 a 1399/1400");
		
		assertEquals("de 971/972 a 1399/1400", endereco.getComplemento());
	}

	@Test
	@DisplayName("Deve setar e obter bairro")
	void testSetEObterBairro() {
		endereco.setBairro("Cerqueira César");
		
		assertEquals("Cerqueira César", endereco.getBairro());
	}

	@Test
	@DisplayName("Deve setar e obter UF")
	void testSetEObterUf() {
		endereco.setUf("SP");
		
		assertEquals("SP", endereco.getUf());
	}

	@Test
	@DisplayName("Deve setar e obter estado")
	void testSetEObterEstado() {
		endereco.setEstado("São Paulo");
		
		assertEquals("São Paulo", endereco.getEstado());
	}

	@Test
	@DisplayName("Deve setar e obter DDD")
	void testSetEObterDdd() {
		endereco.setDdd("11");
		
		assertEquals("11", endereco.getDdd());
	}

	@Test
	@DisplayName("Deve setar e obter erro")
	void testSetEObterErro() {
		endereco.setErro(true);
		
		assertTrue(endereco.getErro());
	}

	@Test
	@DisplayName("Deve inicializar erro como false")
	void testInicializarErroFalse() {
		assertFalse(endereco.getErro());
	}

	@Test
	@DisplayName("Deve setar e obter todos os atributos")
	void testSetEObterTodosAtributos() {
		endereco.setCep("01310-100");
		endereco.setLogradouro("Avenida Paulista");
		endereco.setComplemento("");
		endereco.setBairro("Cerqueira César");
		endereco.setUf("SP");
		endereco.setEstado("São Paulo");
		endereco.setDdd("11");
		endereco.setErro(false);

		assertEquals("01310-100", endereco.getCep());
		assertEquals("Avenida Paulista", endereco.getLogradouro());
		assertEquals("", endereco.getComplemento());
		assertEquals("Cerqueira César", endereco.getBairro());
		assertEquals("SP", endereco.getUf());
		assertEquals("São Paulo", endereco.getEstado());
		assertEquals("11", endereco.getDdd());
		assertFalse(endereco.getErro());
	}

	@Test
	@DisplayName("Deve permitir valores null")
	void testPermitirValoresNull() {
		endereco.setCep(null);
		endereco.setLogradouro(null);
		endereco.setComplemento(null);

		assertNull(endereco.getCep());
		assertNull(endereco.getLogradouro());
		assertNull(endereco.getComplemento());
	}

	@Test
	@DisplayName("Deve atualizar atributo existente")
	void testAtualizarAtributo() {
		endereco.setLogradouro("Avenida Paulista");
		assertEquals("Avenida Paulista", endereco.getLogradouro());

		endereco.setLogradouro("Avenida Presidente");
		assertEquals("Avenida Presidente", endereco.getLogradouro());
	}

	@Test
	@DisplayName("Deve representar CEP inválido com erro true")
	void testCepInvalidoComErro() {
		EnderecoViaCepResponse enderecoInvalido = new EnderecoViaCepResponse();
		enderecoInvalido.setErro(true);

		assertTrue(enderecoInvalido.getErro());
		assertNull(enderecoInvalido.getCep());
	}

	@Test
	@DisplayName("Deve representar CEP válido com erro false")
	void testCepValidoComErroFalse() {
		EnderecoViaCepResponse enderecoValido = new EnderecoViaCepResponse();
		enderecoValido.setCep("01310-100");
		enderecoValido.setErro(false);

		assertFalse(enderecoValido.getErro());
		assertEquals("01310-100", enderecoValido.getCep());
	}
}
