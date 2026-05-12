package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.entity.DadosCadastroCliente;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;
import org.springframework.stereotype.Service;

@Service
public class CadastroService {

    private final CadastroRepository cadastroRepository;

    public CadastroService(CadastroRepository cadastroRepository) {
        this.cadastroRepository = cadastroRepository;
    }

    public DadosCadastroCliente cadastrar(DadosCadastroCliente request) {

        String cpfFormatado = FormatacaoCampo.formatarCpf(request.getCpf());
        request.setCpf(cpfFormatado);

        String telefoneFormatado = FormatacaoCampo.formatarTelefone(request.getTelefone());
        request.setTelefone(telefoneFormatado);

        return cadastroRepository.save(request);
    }
}