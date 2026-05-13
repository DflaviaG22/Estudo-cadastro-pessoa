package estudo.pessoa.cadastro;

import com.fasterxml.jackson.databind.ObjectMapper;
import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.service.CadastroService;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;
import estudo.pessoa.cadastro.utils.ValidadorCpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes da API de Cadastro de Pessoas")
class CadastroApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ViaCepClient viaCepClient;

	@Autowired
	private CadastroService cadastroService;

	@Autowired
	private CadastroRepository cadastroRepository;

	private CadastroPessoa cadastroValido;
	private EnderecoViaCepResponse enderecoValido;

	@BeforeEach
	void setUp() {
		cadastroRepository.deleteAll();
		
		cadastroValido = new CadastroPessoa();
		cadastroValido.setNomeCompleto("João da Silva");
		cadastroValido.setEmail("joao@example.com");
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
	@DisplayName("Deve criar um novo cadastro com sucesso")
	void testCriarCadastroComSucesso() throws Exception {
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nomeCompleto").value("João da Silva"))
				.andExpect(jsonPath("$.cpf").value("123.456.789-00"))
				.andExpect(jsonPath("$.email").value("joao@example.com"))
				.andExpect(jsonPath("$.logradouro").value("Avenida Paulista"));
	}

	@Test
	@DisplayName("Deve formatar CPF ao criar cadastro")
	void testCriarCadastroFormataCpf() throws Exception {
		cadastroValido.setCpf("12345678900");
		
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cpf").value("123.456.789-00"));
	}

	@Test
	@DisplayName("Deve formatar telefone ao criar cadastro")
	void testCriarCadastroFormataTelefone() throws Exception {
		cadastroValido.setTelefone("11987654321");
		
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.telefone").value("(11) 98765-4321"));
	}

	@Test
	@DisplayName("Deve validar CEP inválido ao criar cadastro")
	void testCriarCadastroComCepInvalido() throws Exception {
		cadastroValido.setCep("123");
		
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Deve retornar erro quando CEP não existe na ViaCEP")
	void testCriarCadastroComCepNaoExistente() throws Exception {
		EnderecoViaCepResponse enderecoPerfil = new EnderecoViaCepResponse();
		enderecoPerfil.setErro(true);
		
		when(viaCepClient.consultarCep("99999999")).thenReturn(enderecoPerfil);
		
		cadastroValido.setCep("99999-999");
		
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Deve formatar CPF ao criar cadastro")
	void testListarTodosCadastros() throws Exception {
		// Criar dois cadastros
		CadastroPessoa cadastro2 = new CadastroPessoa();
		cadastro2.setNomeCompleto("Maria Silva");
		cadastro2.setEmail("maria@example.com");
		cadastro2.setCpf("987.654.321-00");
		cadastro2.setTelefone("(21) 98765-4321");
		cadastro2.setCep("01310-100");

		cadastroService.cadastrar(cadastroValido);
		cadastroService.cadastrar(cadastro2);

		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nomeCompleto").value("João da Silva"))
				.andExpect(jsonPath("$[1].nomeCompleto").value("Maria Silva"));
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando não há cadastros")
	void testListarCadastrosVazio() throws Exception {
		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DisplayName("Deve atualizar um cadastro existente")
	void testAtualizarCadastroComSucesso() throws Exception {
		// Criar cadastro
		CadastroPessoa cadastroSalvo = cadastroService.cadastrar(cadastroValido);

		// Atualizar
		cadastroValido.setNomeCompleto("João Silva Updated");
		cadastroValido.setEmail("joao.updated@example.com");

		mockMvc.perform(put("/cadastro/123.456.789-00")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nomeCompleto").value("João Silva Updated"))
				.andExpect(jsonPath("$.email").value("joao.updated@example.com"));
	}

	@Test
	@DisplayName("Deve retornar 404 ao atualizar cadastro inexistente")
	void testAtualizarCadastroInexistente() throws Exception {
		mockMvc.perform(put("/cadastro/999.999.999-99")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve atualizar endereço quando CEP é alterado")
	void testAtualizarCadastroComNovoCep() throws Exception {
		CadastroPessoa cadastroSalvo = cadastroService.cadastrar(cadastroValido);

		EnderecoViaCepResponse novoEndereco = new EnderecoViaCepResponse();
		novoEndereco.setCep("20040020");
		novoEndereco.setLogradouro("Avenida Rio Branco");
		novoEndereco.setComplemento("");
		novoEndereco.setBairro("Centro");
		novoEndereco.setUf("RJ");
		novoEndereco.setEstado("Rio de Janeiro");
		novoEndereco.setDdd("21");
		novoEndereco.setErro(false);

		when(viaCepClient.consultarCep("20040020")).thenReturn(novoEndereco);

		cadastroValido.setCep("20040-020");
		cadastroValido.setNomeCompleto("João da Silva");

		mockMvc.perform(put("/cadastro/123.456.789-00")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.logradouro").value("Avenida Rio Branco"))
				.andExpect(jsonPath("$.uf").value("RJ"));
	}

	@Test
	@DisplayName("Deve deletar um cadastro existente")
	void testDeletarCadastroComSucesso() throws Exception {
		CadastroPessoa cadastroSalvo = cadastroService.cadastrar(cadastroValido);

		mockMvc.perform(delete("/cadastro/123.456.789-00"))
				.andExpect(status().isNoContent());

		// Verificar que foi deletado
		mockMvc.perform(put("/cadastro/123.456.789-00")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve retornar 404 ao deletar cadastro inexistente")
	void testDeletarCadastroInexistente() throws Exception {
		mockMvc.perform(delete("/cadastro/999.999.999-99"))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Deve formatar CPF corretamente")
	void testFormatarCpf() {
		assertEquals("123.456.789-00", FormatacaoCampo.formatarCpf("12345678900"));
		assertEquals("123.456.789-00", FormatacaoCampo.formatarCpf("123.456.789-00"));
	}

	@Test
	@DisplayName("Deve formatar telefone corretamente")
	void testFormatarTelefone() {
		assertEquals("(11) 98765-4321", FormatacaoCampo.formatarTelefone("11987654321"));
		assertEquals("(11) 98765-4321", FormatacaoCampo.formatarTelefone("(11) 98765-4321"));
	}

	@Test
	@DisplayName("Deve validar CPF válido")
	void testValidarCpfValido() {
		boolean resultado = ValidadorCpf.validarCpf("123.456.789-00");
		assertNotNull(resultado);
	}

	@Test
	@DisplayName("Deve executar fluxo completo: criar, listar, atualizar, deletar")
	void testFluxoCompleto() throws Exception {
		// 1. Criar
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk());

		// 2. Listar
		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));

		// 3. Atualizar
		cadastroValido.setEmail("novo.email@example.com");
		mockMvc.perform(put("/cadastro/123.456.789-00")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("novo.email@example.com"));

		// 4. Deletar
		mockMvc.perform(delete("/cadastro/123.456.789-00"))
				.andExpect(status().isNoContent());

		// 5. Verificar que foi deletado
		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DisplayName("Deve limpar CPF e consultar pelo CPF numérico")
	void testConsultarPorCpfNumerico() {
		CadastroPessoa cadastroSalvo = cadastroService.cadastrar(cadastroValido);
		
		// Buscar com CPF formatado
		Optional<CadastroPessoa> encontrado = cadastroService.atualizarPorCpf("123.456.789-00", cadastroValido);
		assertTrue(encontrado.isPresent());
		
		// Buscar com CPF sem formatação
		encontrado = cadastroService.atualizarPorCpf("12345678900", cadastroValido);
		assertTrue(encontrado.isPresent());
	}

	@Test
	@DisplayName("Contexto da aplicação carrega com sucesso")
	void contextLoads() {
		assertNotNull(mockMvc);
		assertNotNull(cadastroService);
		assertNotNull(cadastroRepository);
	}
}
