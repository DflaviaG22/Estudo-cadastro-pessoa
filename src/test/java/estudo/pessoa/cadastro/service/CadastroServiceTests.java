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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Testes do Service de Cadastro")
class CadastroServiceTests {

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

        cadastroValido = criarCadastro("Joao Silva", "joao@test.com", "123.456.789-00", "01310-100");
        when(viaCepClient.consultarCep("01310100")).thenReturn(criarEndereco(
                "01310-100",
                "Avenida Paulista",
                "Cerqueira Cesar",
                "SP",
                "Sao Paulo",
                "11",
                false
        ));
    }

    @Test
    @DisplayName("Deve cadastrar pessoa preenchendo endereco pelo CEP")
    void testCadastrarPessoaComSucesso() {
        CadastroPessoa resultado = cadastroService.cadastrar(cadastroValido);

        assertEquals("123.456.789-00", resultado.getCpf());
        assertEquals("Joao Silva", resultado.getNomeCompleto());
        assertEquals("Avenida Paulista", resultado.getLogradouro());
        assertEquals("SP", resultado.getUf());
    }

    @Test
    @DisplayName("Deve atualizar cadastro existente pelo CPF")
    void testAtualizarCadastroExistente() {
        cadastroService.cadastrar(cadastroValido);

        CadastroPessoa atualizacao = criarCadastro("Joao Silva Updated", "joao.updated@test.com", "123.456.789-00", "01310-100");
        Optional<CadastroPessoa> resultado = cadastroService.atualizarPorCpf("12345678900", atualizacao);

        assertTrue(resultado.isPresent());
        assertEquals("Joao Silva Updated", resultado.get().getNomeCompleto());
        assertEquals("joao.updated@test.com", resultado.get().getEmail());
    }

    @Test
    @DisplayName("Deve retornar vazio ao atualizar CPF inexistente")
    void testAtualizarCpfInexistente() {
        Optional<CadastroPessoa> resultado = cadastroService.atualizarPorCpf("999.999.999-99", cadastroValido);

        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve deletar cadastro existente pelo CPF")
    void testDeletarCadastroExistente() {
        cadastroService.cadastrar(cadastroValido);

        boolean resultado = cadastroService.deletarPorCpf("123.456.789-00");

        assertTrue(resultado);
        assertEquals(0, cadastroRepository.findAll().size());
    }

    @Test
    @DisplayName("Deve recusar CEP invalido ou inexistente")
    void testCadastrarComCepInvalidoOuInexistente() {
        cadastroValido.setCep("123");
        assertThrows(ResponseStatusException.class, () -> cadastroService.cadastrar(cadastroValido));

        CadastroPessoa cadastroComCepInexistente = criarCadastro("Joao Silva", "joao@test.com", "123.456.789-00", "99999-999");
        when(viaCepClient.consultarCep("99999999")).thenReturn(criarEndereco(null, null, null, null, null, null, true));

        assertThrows(ResponseStatusException.class, () -> cadastroService.cadastrar(cadastroComCepInexistente));
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

    private EnderecoViaCepResponse criarEndereco(
            String cep,
            String logradouro,
            String bairro,
            String uf,
            String estado,
            String ddd,
            boolean erro
    ) {
        EnderecoViaCepResponse endereco = new EnderecoViaCepResponse();
        endereco.setCep(cep);
        endereco.setLogradouro(logradouro);
        endereco.setComplemento("");
        endereco.setBairro(bairro);
        endereco.setUf(uf);
        endereco.setEstado(estado);
        endereco.setDdd(ddd);
        endereco.setErro(erro);
        return endereco;
    }
}
