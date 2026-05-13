package estudo.pessoa.cadastro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.service.CadastroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes do Controller de Cadastro")
class CadastroControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
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
	@DisplayName("POST /cadastro - Deve criar novo cadastro")
	void testPostCadastro() throws Exception {
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.nomeCompleto").value("João Silva"));
	}

	@Test
	@DisplayName("POST /cadastro - Deve retornar os dados completos com endereço")
	void testPostCadastroComEndereco() throws Exception {
		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.logradouro").value("Avenida Paulista"))
				.andExpect(jsonPath("$.bairro").value("Cerqueira César"))
				.andExpect(jsonPath("$.uf").value("SP"));
	}

	@Test
	@DisplayName("GET /cadastro - Deve listar todos os cadastros")
	void testGetCadastros() throws Exception {
		cadastroService.cadastrar(cadastroValido);

		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	@DisplayName("GET /cadastro - Deve retornar lista vazia")
	void testGetCadastrosVazio() throws Exception {
		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DisplayName("PUT /cadastro/{cpf} - Deve atualizar cadastro")
	void testPutCadastro() throws Exception {
		cadastroService.cadastrar(cadastroValido);

		CadastroPessoa atualizacao = new CadastroPessoa();
		atualizacao.setNomeCompleto("João Silva Updated");
		atualizacao.setEmail("joao.updated@test.com");
		atualizacao.setCpf("123.456.789-00");
		atualizacao.setTelefone("(11) 99876-5432");
		atualizacao.setCep("01310-100");

		mockMvc.perform(put("/cadastro/123.456.789-00")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(atualizacao)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nomeCompleto").value("João Silva Updated"));
	}

	@Test
	@DisplayName("PUT /cadastro/{cpf} - Deve retornar 404 para CPF inexistente")
	void testPutCadastroNaoEncontrado() throws Exception {
		mockMvc.perform(put("/cadastro/999.999.999-99")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("DELETE /cadastro/{cpf} - Deve deletar cadastro")
	void testDeleteCadastro() throws Exception {
		cadastroService.cadastrar(cadastroValido);

		mockMvc.perform(delete("/cadastro/123.456.789-00"))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/cadastro"))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	@DisplayName("DELETE /cadastro/{cpf} - Deve retornar 404 para CPF inexistente")
	void testDeleteCadastroNaoEncontrado() throws Exception {
		mockMvc.perform(delete("/cadastro/999.999.999-99"))
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("POST /cadastro - Deve retornar 400 para CEP inválido")
	void testPostCadastroComCepInvalido() throws Exception {
		cadastroValido.setCep("123");

		mockMvc.perform(post("/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cadastroValido)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("PUT /cadastro/{cpf} - Deve atualizar com CPF sem formatação")
	void testPutCadastroComCpfSemFormatacao() throws Exception {
		cadastroService.cadastrar(cadastroValido);

		CadastroPessoa atualizacao = new CadastroPessoa();
		atualizacao.setNomeCompleto("Updated");
		atualizacao.setEmail("updated@test.com");
		atualizacao.setCpf("12345678900");
		atualizacao.setTelefone("(11) 99876-5432");
		atualizacao.setCep("01310-100");

		mockMvc.perform(put("/cadastro/12345678900")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(atualizacao)))
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("GET /cadastro - Deve retornar multiple cadastros")
	void testGetMultiplosCadastros() throws Exception {
		for (int i = 0; i < 5; i++) {
			CadastroPessoa cadastro = new CadastroPessoa();
			cadastro.setNomeCompleto("Pessoa " + i);
			cadastro.setEmail("pessoa" + i + "@test.com");
			cadastro.setCpf("123.456.789-0" + i);
			cadastro.setTelefone("(11) 9876" + i + "-432" + i);
			cadastro.setCep("01310-100");
			cadastroService.cadastrar(cadastro);
		}

		mockMvc.perform(get("/cadastro"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(5)));
	}
}
