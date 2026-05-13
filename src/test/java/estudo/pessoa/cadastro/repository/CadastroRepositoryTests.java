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

import static org.junit.jupiter.api.Assertions.*;

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
		cadastroValido.setNomeCompleto("João Silva");
		cadastroValido.setEmail("joao@test.com");
		cadastroValido.setCpf("123.456.789-00");
		cadastroValido.setTelefone("(11) 98765-4321");
		cadastroValido.setCep("01310-100");
		cadastroValido.setLogradouro("Avenida Paulista");
		cadastroValido.setComplemento("");
		cadastroValido.setBairro("Cerqueira César");
		cadastroValido.setUf("SP");
		cadastroValido.setEstado("São Paulo");
		cadastroValido.setDdd("11");

		cadastroRepository.deleteAll();
	}

	@Test
	@DisplayName("Deve encontrar cadastro por CPF")
	void testFindByCpf() {
		cadastroRepository.save(cadastroValido);
		entityManager.flush();

		Optional<CadastroPessoa> resultado = cadastroRepository.findByCpf("123.456.789-00");

		assertTrue(resultado.isPresent());
		assertEquals("João Silva", resultado.get().getNomeCompleto());
	}

	@Test
	@DisplayName("Deve retornar vazio para CPF não existente")
	void testFindByCpfNaoExistente() {
		Optional<CadastroPessoa> resultado = cadastroRepository.findByCpf("999.999.999-99");

		assertFalse(resultado.isPresent());
	}

	@Test
	@DisplayName("Deve encontrar cadastro por CPF numérico")
	void testFindByCpfNumerico() {
		cadastroRepository.save(cadastroValido);
		entityManager.flush();

		Optional<CadastroPessoa> resultado = cadastroRepository.findByCpfNumerico("12345678900");

		assertTrue(resultado.isPresent());
		assertEquals("123.456.789-00", resultado.get().getCpf());
	}

	@Test
	@DisplayName("Deve encontrar por CPF numérico sem formatação")
	void testFindByCpfNumericoSemFormatacao() {
		cadastroRepository.save(cadastroValido);
		entityManager.flush();

		Optional<CadastroPessoa> resultado = cadastroRepository.findByCpfNumerico("12345678900");

		assertTrue(resultado.isPresent());
	}

	@Test
	@DisplayName("Deve salvar e recuperar cadastro")
	void testSaveAndRetrieve() {
		CadastroPessoa salvo = cadastroRepository.save(cadastroValido);
		entityManager.flush();
		entityManager.clear();

		Optional<CadastroPessoa> recuperado = cadastroRepository.findById(salvo.getId());

		assertTrue(recuperado.isPresent());
		assertEquals("João Silva", recuperado.get().getNomeCompleto());
	}

	@Test
	@DisplayName("Deve deletar cadastro")
	void testDelete() {
		CadastroPessoa salvo = cadastroRepository.save(cadastroValido);
		entityManager.flush();

		cadastroRepository.delete(salvo);
		entityManager.flush();

		Optional<CadastroPessoa> resultado = cadastroRepository.findById(salvo.getId());

		assertFalse(resultado.isPresent());
	}
}
