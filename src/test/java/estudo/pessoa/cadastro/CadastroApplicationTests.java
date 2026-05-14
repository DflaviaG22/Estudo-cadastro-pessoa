package estudo.pessoa.cadastro;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Testes da API de Cadastro de Pessoas")
class CadastroApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CadastroService cadastroService;

    @Autowired
    private CadastroRepository cadastroRepository;

    @MockitoBean
    private ViaCepClient viaCepClient;

    private CadastroPessoa cadastroValido;

    @BeforeEach
    void setUp() {
        cadastroRepository.deleteAll();

        cadastroValido = criarCadastro("Joao da Silva", "joao@example.com", "123.456.789-00", "01310-100");
        when(viaCepClient.consultarCep("01310100")).thenReturn(criarEndereco(
                "01310-100",
                "Avenida Paulista",
                "Cerqueira Cesar",
                "SP",
                "Sao Paulo",
                "11"
        ));
    }

    @Test
    @DisplayName("Deve criar cadastro com endereco em lista")
    void testCriarCadastroComSucesso() throws Exception {
        mockMvc.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Joao da Silva"))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.endereco", hasSize(1)))
                .andExpect(jsonPath("$.endereco[0].logradouro").value("Avenida Paulista"));
    }

    @Test
    @DisplayName("Deve retornar erro para CEP invalido")
    void testCriarCadastroComCepInvalido() throws Exception {
        cadastroValido.setCep("123");

        mockMvc.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroValido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar erro quando CEP nao existe")
    void testCriarCadastroComCepNaoExistente() throws Exception {
        EnderecoViaCepResponse enderecoInvalido = new EnderecoViaCepResponse();
        enderecoInvalido.setErro(true);
        when(viaCepClient.consultarCep("99999999")).thenReturn(enderecoInvalido);

        cadastroValido.setCep("99999-999");

        mockMvc.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroValido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar conflito ao cadastrar CPF ja existente")
    void testCriarCadastroComCpfJaCadastrado() throws Exception {
        cadastroService.cadastrar(cadastroValido);

        mockMvc.perform(post("/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroValido)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.mensagem").value("Cpf já cadastrado"));
    }

    @Test
    @DisplayName("Deve listar cadastros")
    void testListarCadastros() throws Exception {
        CadastroPessoa cadastro2 = criarCadastro("Maria Silva", "maria@example.com", "987.654.321-00", "01310-100");
        cadastroService.cadastrar(cadastroValido);
        cadastroService.cadastrar(cadastro2);

        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Deve atualizar cadastro e endereco pelo CPF")
    void testAtualizarCadastroComNovoCep() throws Exception {
        cadastroService.cadastrar(cadastroValido);
        when(viaCepClient.consultarCep("20040020")).thenReturn(criarEndereco(
                "20040020",
                "Avenida Rio Branco",
                "Centro",
                "RJ",
                "Rio de Janeiro",
                "21"
        ));

        cadastroValido.setNomeCompleto("Joao Silva Updated");
        cadastroValido.setCep("20040-020");

        mockMvc.perform(put("/cadastro/12345678900")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cadastroValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCompleto").value("Joao Silva Updated"))
                .andExpect(jsonPath("$.endereco[0].logradouro").value("Avenida Rio Branco"))
                .andExpect(jsonPath("$.endereco[0].uf").value("RJ"));
    }

    @Test
    @DisplayName("Deve deletar cadastro pelo CPF")
    void testDeletarCadastroComSucesso() throws Exception {
        cadastroService.cadastrar(cadastroValido);

        mockMvc.perform(delete("/cadastro/123.456.789-00"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private CadastroPessoa criarCadastro(String nome, String email, String cpf, String cep) {
        CadastroPessoa cadastro = new CadastroPessoa();
        cadastro.setNomeCompleto(nome);
        cadastro.setEmail(email);
        cadastro.setCpf(cpf);
        cadastro.setTelefone("(11) 98765-4321");
        cadastro.setCep(cep);
        return cadastro;
    }

    private EnderecoViaCepResponse criarEndereco(String cep, String logradouro, String bairro, String uf, String estado, String ddd) {
        EnderecoViaCepResponse endereco = new EnderecoViaCepResponse();
        endereco.setCep(cep);
        endereco.setLogradouro(logradouro);
        endereco.setComplemento("");
        endereco.setBairro(bairro);
        endereco.setUf(uf);
        endereco.setEstado(estado);
        endereco.setDdd(ddd);
        endereco.setErro(false);
        return endereco;
    }
}
