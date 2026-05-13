package estudo.pessoa.cadastro.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entity CadastroPessoa")
class CadastroPessoaTests {

	private CadastroPessoa cadastroPessoa;

	@BeforeEach
	void setUp() {
		cadastroPessoa = new CadastroPessoa();
	}

	@Test
	@DisplayName("Deve criar instância vazia")
	void testCriarInstanciaVazia() {
		assertNotNull(cadastroPessoa);
	}

	@Test
	@DisplayName("Deve setar e obter ID")
	void testSetEObterId() {
		cadastroPessoa.setId(1);
		
		assertEquals(1, cadastroPessoa.getId());
	}

	@Test
	@DisplayName("Deve setar e obter nome completo")
	void testSetEObterNomeCompleto() {
		cadastroPessoa.setNomeCompleto("João Silva");
		
		assertEquals("João Silva", cadastroPessoa.getNomeCompleto());
	}

	@Test
	@DisplayName("Deve setar e obter CPF")
	void testSetEObterCpf() {
		cadastroPessoa.setCpf("123.456.789-00");
		
		assertEquals("123.456.789-00", cadastroPessoa.getCpf());
	}

	@Test
	@DisplayName("Deve setar e obter email")
	void testSetEObterEmail() {
		cadastroPessoa.setEmail("joao@test.com");
		
		assertEquals("joao@test.com", cadastroPessoa.getEmail());
	}

	@Test
	@DisplayName("Deve setar e obter telefone")
	void testSetEObterTelefone() {
		cadastroPessoa.setTelefone("(11) 98765-4321");
		
		assertEquals("(11) 98765-4321", cadastroPessoa.getTelefone());
	}

	@Test
	@DisplayName("Deve setar e obter CEP")
	void testSetEObterCep() {
		cadastroPessoa.setCep("01310-100");
		
		assertEquals("01310-100", cadastroPessoa.getCep());
	}

	@Test
	@DisplayName("Deve setar e obter logradouro")
	void testSetEObterLogradouro() {
		cadastroPessoa.setLogradouro("Avenida Paulista");
		
		assertEquals("Avenida Paulista", cadastroPessoa.getLogradouro());
	}

	@Test
	@DisplayName("Deve setar e obter complemento")
	void testSetEObterComplemento() {
		cadastroPessoa.setComplemento("Apto 100");
		
		assertEquals("Apto 100", cadastroPessoa.getComplemento());
	}

	@Test
	@DisplayName("Deve setar e obter bairro")
	void testSetEObterBairro() {
		cadastroPessoa.setBairro("Cerqueira César");
		
		assertEquals("Cerqueira César", cadastroPessoa.getBairro());
	}

	@Test
	@DisplayName("Deve setar e obter estado")
	void testSetEObterEstado() {
		cadastroPessoa.setEstado("São Paulo");
		
		assertEquals("São Paulo", cadastroPessoa.getEstado());
	}

	@Test
	@DisplayName("Deve setar e obter UF")
	void testSetEObterUf() {
		cadastroPessoa.setUf("SP");
		
		assertEquals("SP", cadastroPessoa.getUf());
	}

	@Test
	@DisplayName("Deve setar e obter DDD")
	void testSetEObterDdd() {
		cadastroPessoa.setDdd("11");
		
		assertEquals("11", cadastroPessoa.getDdd());
	}

	@Test
	@DisplayName("Deve setar e obter todos os atributos")
	void testSetEObterTodosAtributos() {
		cadastroPessoa.setId(1);
		cadastroPessoa.setNomeCompleto("João Silva");
		cadastroPessoa.setCpf("123.456.789-00");
		cadastroPessoa.setEmail("joao@test.com");
		cadastroPessoa.setTelefone("(11) 98765-4321");
		cadastroPessoa.setCep("01310-100");
		cadastroPessoa.setLogradouro("Avenida Paulista");
		cadastroPessoa.setComplemento("Apto 100");
		cadastroPessoa.setBairro("Cerqueira César");
		cadastroPessoa.setEstado("São Paulo");
		cadastroPessoa.setUf("SP");
		cadastroPessoa.setDdd("11");

		assertEquals(1, cadastroPessoa.getId());
		assertEquals("João Silva", cadastroPessoa.getNomeCompleto());
		assertEquals("123.456.789-00", cadastroPessoa.getCpf());
		assertEquals("joao@test.com", cadastroPessoa.getEmail());
		assertEquals("(11) 98765-4321", cadastroPessoa.getTelefone());
		assertEquals("01310-100", cadastroPessoa.getCep());
		assertEquals("Avenida Paulista", cadastroPessoa.getLogradouro());
		assertEquals("Apto 100", cadastroPessoa.getComplemento());
		assertEquals("Cerqueira César", cadastroPessoa.getBairro());
		assertEquals("São Paulo", cadastroPessoa.getEstado());
		assertEquals("SP", cadastroPessoa.getUf());
		assertEquals("11", cadastroPessoa.getDdd());
	}

	@Test
	@DisplayName("Deve permitir valores null")
	void testPermitirValoresNull() {
		cadastroPessoa.setNomeCompleto(null);
		cadastroPessoa.setEmail(null);
		cadastroPessoa.setCpf(null);

		assertNull(cadastroPessoa.getNomeCompleto());
		assertNull(cadastroPessoa.getEmail());
		assertNull(cadastroPessoa.getCpf());
	}

	@Test
	@DisplayName("Deve atualizar atributo existente")
	void testAtualizarAtributo() {
		cadastroPessoa.setEmail("joao@test.com");
		assertEquals("joao@test.com", cadastroPessoa.getEmail());

		cadastroPessoa.setEmail("joao.novo@test.com");
		assertEquals("joao.novo@test.com", cadastroPessoa.getEmail());
	}
}
