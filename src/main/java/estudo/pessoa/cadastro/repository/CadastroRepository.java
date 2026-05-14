package estudo.pessoa.cadastro.repository;

import estudo.pessoa.cadastro.entity.CadastroPessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CadastroRepository extends JpaRepository<CadastroPessoa, String> {

    Optional<CadastroPessoa> findByCpf(String cpf);

    @Query("select cadastro from CadastroPessoa cadastro where replace(replace(cadastro.cpf, '.', ''), '-', '') = :cpf")
    Optional<CadastroPessoa> findByCpfNumerico(@Param("cpf") String cpf);

    @Query("select count(cadastro) from CadastroPessoa cadastro where replace(replace(cadastro.cpf, '.', ''), '-', '') = :cpf")
    long countByCpfNumerico(@Param("cpf") String cpf);
}
