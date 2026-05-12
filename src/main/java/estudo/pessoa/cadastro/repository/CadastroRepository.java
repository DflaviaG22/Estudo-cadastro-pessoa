package estudo.pessoa.cadastro.repository;

import estudo.pessoa.cadastro.entity.CadastroPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CadastroRepository extends JpaRepository<CadastroPessoa, Integer> {

    Optional<CadastroPessoa> findByCpf(String cpf);
}
