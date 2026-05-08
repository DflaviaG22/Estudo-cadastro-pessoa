package estudo.pessoa.cadastro.repository;

import estudo.pessoa.cadastro.entity.DadosCadastroCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroRepository extends JpaRepository<DadosCadastroCliente, Integer> {
}
