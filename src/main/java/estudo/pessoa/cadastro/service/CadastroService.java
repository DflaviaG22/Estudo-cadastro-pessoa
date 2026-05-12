package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroService {

    private final CadastroRepository cadastroRepository;
    private final ViaCepClient viaCepClient;

    public CadastroService(CadastroRepository cadastroRepository, ViaCepClient viaCepClient) {
        this.cadastroRepository = cadastroRepository;
        this.viaCepClient = viaCepClient;
    }

    public CadastroPessoa cadastrar(CadastroPessoa request) {

        String cpfFormatado = FormatacaoCampo.formatarCpf(request.getCpf());
        request.setCpf(cpfFormatado);

        String telefoneFormatado = FormatacaoCampo.formatarTelefone(request.getTelefone());
        request.setTelefone(telefoneFormatado);

        preencherEndereco(request, request.getCep());

        return cadastroRepository.save(request);
    }

    public List<CadastroPessoa> listarTodos() {
        return cadastroRepository.findAll();
    }

    public Optional<CadastroPessoa> consultarPorCpf(String cpf) {
        String cpfNumerico = limparCpf(cpf);
        return cadastroRepository.findByCpfNumerico(cpfNumerico);
    }

    public Optional<CadastroPessoa> atualizarPorCpf(String cpf, CadastroPessoa request) {
        String cpfNumerico = limparCpf(cpf);
        String cpfFormatado = FormatacaoCampo.formatarCpf(cpfNumerico);

        return cadastroRepository.findByCpfNumerico(cpfNumerico)
                .map(cadastroExistente -> {
                    cadastroExistente.setCpf(cpfFormatado);
                    cadastroExistente.setNomeCompleto(request.getNomeCompleto());
                    cadastroExistente.setEmail(request.getEmail());
                    cadastroExistente.setTelefone(FormatacaoCampo.formatarTelefone(request.getTelefone()));
                    preencherEndereco(cadastroExistente, request.getCep());

                    return cadastroRepository.save(cadastroExistente);
                });
    }

    public boolean deletarPorCpf(String cpf) {
        String cpfNumerico = limparCpf(cpf);

        return cadastroRepository.findByCpfNumerico(cpfNumerico)
                .map(cadastro -> {
                    cadastroRepository.delete(cadastro);
                    return true;
                })
                .orElse(false);
    }

    private void preencherEndereco(CadastroPessoa cadastroPessoa, String cep) {
        String cepNumerico = limparCep(cep);
        EnderecoViaCepResponse endereco = viaCepClient.consultarCep(cepNumerico);

        if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP invalido");
        }

        cadastroPessoa.setCep(endereco.getCep());
        cadastroPessoa.setLogradouro(endereco.getLogradouro());
        cadastroPessoa.setComplemento(endereco.getComplemento());
        cadastroPessoa.setUf(endereco.getUf());
        cadastroPessoa.setEstado(endereco.getEstado());
        cadastroPessoa.setBairro(endereco.getBairro());
        cadastroPessoa.setDdd(endereco.getDdd());
    }

    private String limparCep(String cep) {
        if (cep == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP e obrigatorio");
        }

        String cepNumerico = cep.replaceAll("\\D", "");

        if (cepNumerico.length() != 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP deve conter 8 digitos");
        }

        return cepNumerico;
    }

    private String limparCpf(String cpf) {
        if (cpf == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF e obrigatorio");
        }

        String cpfNumerico = cpf.replaceAll("\\D", "");

        if (cpfNumerico.length() != 11) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF deve conter 11 digitos");
        }

        return cpfNumerico;
    }
}
