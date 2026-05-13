package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do Service de Cadastro")
class CadastroServiceTests {

	@Autowired
	private CadastroService cadastroService;

	@Autowired
	private CadastroRepository cadastroRepository;

	@MockBean
	private ViaCepClient viaCepClient;

	private CadastroPessoa cadastroValido;
	private EnderecoViaCepResponse enderecoValido;

	@BeforeEach
	void setUp() {
		cadastroRepository.deleteAll();

		cadastroValido = new CadastroPessoa();
		cadastroValido.setNomeCompleto("João Silva");
		cadastroValido.setEmail("joao@test.com");
		cadastroValido.setCpf("123.456.789-00");
		cadastroValido.setTelefone("(11) 98765-4321");
		cadastroValido.setCep("01310-100");

		enderecoValido = new EnderecoViaCepResponse();
		enderecoValido.setCep("01310-100");
		enderecoValido.setLogradouro("Avenida Paulista");
		enderecoValido.setComplemento("");
		enderecoValido.setBairro("Cerqueira César");
		enderecoValido.setUf("SP");
		enderecoValido.setEstado("São Paulo");
		enderecoValido.setDdd("11");
		enderecoValido.setErro(false);

		when(viaCepClient.consultarCep("01310100")).thenReturn(enderecoValido);
	}

	@Test
	@DisplayName("Deve cadastrar pessoa com sucesso")
	void testCadastrarPessoaComSucesso() {
		CadastroPessoa resultado = cadastroService.cadastrar(cadastroValido);
		
		assertNotNull(resultado.getId());
		assertEquals("João Silva", resultado.getNomeCompleto());
		assertEquals("123.456.789-00", resultado.getCpf());
		assertEquals("Avenida Paulista", resultado.getLogradouro());
	}

	@Test
	@DisplayName("Deve listar todos os cadastros")
	void testListarTodosOsCadastros() {
		CadastroPessoa pessoa1 = cadastroValido;
		CadastroPessoa pessoa2 = new CadastroPessoa();
		pessoa2.setNomeCompleto("Maria Silva");
		pessoa2.setEmail("maria@test.com");
		pessoa2.setCpf("987.654.321-00");
		pessoa2.setTelefone("(21) 98765-4321");
		pessoa2.setCep("01310-100");

		cadastroService.cadastrar(pessoa1);
		cadastroService.cadastrar(pessoa2);

		List<CadastroPessoa> resultado = cadastroService.listarTodos();

		assertEquals(2, resultado.size());
	}

	@Test
	@DisplayName("Deve atualizar cadastro existente")
	void testAtualizarCadastroExistente() {
		CadastroPessoa salvo = cadastroService.cadastrar(cadastroValido);

		CadastroPessoa atualizacao = new CadastroPessoa();
		atualizacao.setNomeCompleto("João Silva Updated");
		atualizacao.setEmail("joao.updated@test.com");
		atualizacao.setTelefone("(11) 99876-5432");
		atualizacao.setCep("01310-100");

		Optional<CadastroPessoa> resultado = cadastroService.atualizarPorCpf("123.456.789-00", atualizacao);

		assertTrue(resultado.isPresent());
		assertEquals("João Silva Updated", resultado.get().getNomeCompleto());
		assertEquals("joao.updated@test.com", resultado.get().getEmail());
	}

	@Test
	@DisplayName("Deve retornar vazio ao atualizar CPF inexistente")
	void testAtualizarCpfInexistente() {
		Optional<CadastroPessoa> resultado = cadastroService.atualizarPorCpf("999.999.999-99", cadastroValido);
		
		assertFalse(resultado.isPresent());
	}

	@Test
	@DisplayName("Deve deletar cadastro existente")
	void testDeletarCadastroExistente() {
		CadastroPessoa salvo = cadastroService.cadastrar(cadastroValido);

		boolean resultado = cadastroService.deletarPorCpf("123.456.789-00");

		assertTrue(resultado);
		assertEquals(0, cadastroRepository.findAll().size());
	}

	@Test
	@DisplayName("Deve retornar false ao deletar CPF inexistente")
	void testDeletarCpfInexistente() {
		boolean resultado = cadastroService.deletarPorCpf("999.999.999-99");
		
		assertFalse(resultado);
	}

	@Test
	@DisplayName("Deve lançar exceção com CEP inválido")
	void testCadastrarComCepInvalido() {
		cadastroValido.setCep("123");

		assertThrows(ResponseStatusException.class, () -> {
			cadastroService.cadastrar(cadastroValido);
		});
	}

	@Test
	@DisplayName("Deve lançar exceção com CEP não existente")
	void testCadastrarComCepNaoExistente() {
		EnderecoViaCepResponse enderecoInvalido = new EnderecoViaCepResponse();
		enderecoInvalido.setErro(true);
		when(viaCepClient.consultarCep("99999999")).thenReturn(enderecoInvalido);

		cadastroValido.setCep("99999-999");

		assertThrows(ResponseStatusException.class, () -> {
			cadastroService.cadastrar(cadastroValido);
		});
	}

	@Test
	@DisplayName("Deve lançar exceção com CEP obrigatório")
	void testCadastrarComCepNulo() {
		cadastroValido.setCep(null);

		assertThrows(ResponseStatusException.class, () -> {
			cadastroService.cadastrar(cadastroValido);
		});
	}
}
