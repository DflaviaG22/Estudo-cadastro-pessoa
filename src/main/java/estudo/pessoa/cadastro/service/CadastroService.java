package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Optional<CadastroPessoa> consultarPorCpf(String cpf) {
        String cpfFormatado = FormatacaoCampo.formatarCpf(cpf);
        return cadastroRepository.findByCpf(cpfFormatado);
    }

    public Optional<CadastroPessoa> atualizarPorCpf(String cpf, CadastroPessoa request) {
        String cpfFormatado = FormatacaoCampo.formatarCpf(cpf);

        return cadastroRepository.findByCpf(cpfFormatado)
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
        String cpfFormatado = FormatacaoCampo.formatarCpf(cpf);

        return cadastroRepository.findByCpf(cpfFormatado)
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
}
