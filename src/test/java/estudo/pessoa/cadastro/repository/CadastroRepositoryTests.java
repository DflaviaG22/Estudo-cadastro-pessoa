package estudo.pessoa.cadastro.repository;

import estudo.pessoa.cadastro.entity.CadastroPessoa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testes do Repository de Cadastro")
class CadastroRepositoryTests {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CadastroPessoa cadastroValido;

    @BeforeEach
    void setUp() {
        cadastroValido = new CadastroPessoa();
        cadastroValido.setNomeCompleto("Joao Silva");
        cadastroValido.setEmail("joao@test.com");
        cadastroValido.setCpf("123.456.789-00");
        cadastroValido.setTelefone("(11) 98765-4321");
        cadastroValido.setCep("01310-100");
        cadastroValido.setLogradouro("Avenida Paulista");
        cadastroValido.setComplemento("");
        cadastroValido.setBairro("Cerqueira Cesar");
        cadastroValido.setUf("SP");
        cadastroValido.setEstado("Sao Paulo");
        cadastroValido.setDdd("11");

        cadastroRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve encontrar cadastro por CPF numerico")
    void testFindByCpfNumerico() {
        cadastroRepository.save(cadastroValido);
        entityManager.flush();

        Optional<CadastroPessoa> resultado = cadastroRepository.findByCpfNumerico("12345678900");

        assertTrue(resultado.isPresent());
        assertEquals("123.456.789-00", resultado.get().getCpf());
    }

    @Test
    @DisplayName("Deve retornar vazio para CPF numerico inexistente")
    void testFindByCpfNumericoInexistente() {
        Optional<CadastroPessoa> resultado = cadastroRepository.findByCpfNumerico("99999999999");

        assertFalse(resultado.isPresent());
    }
}
