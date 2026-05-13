package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.client.ViaCepClient;
import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.exception.*;
import estudo.pessoa.cadastro.repository.CadastroRepository;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;
import org.springframework.stereotype.Service;

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
        ValidadorCadastro.validareCpf(request.getCpf());
        ValidadorCadastro.validareCep(request.getCep());

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

    public Optional<CadastroPessoa> atualizarPorCpf(String cpf, CadastroPessoa request) {
        String cpfNumerico = ValidadorCadastro.validareCpf(cpf);
        String cpfFormatado = FormatacaoCampo.formatarCpf(cpfNumerico);

        return cadastroRepository.findByCpfNumerico(cpfNumerico)
                .map(cadastroExistente -> {
                    validarDadosAtualizacao(request);
                    
                    cadastroExistente.setCpf(cpfFormatado);
                    cadastroExistente.setNomeCompleto(request.getNomeCompleto());
                    cadastroExistente.setEmail(request.getEmail());
                    cadastroExistente.setTelefone(FormatacaoCampo.formatarTelefone(request.getTelefone()));
                    preencherEndereco(cadastroExistente, request.getCep());

                    return cadastroRepository.save(cadastroExistente);
                });
    }

    public boolean deletarPorCpf(String cpf) {
        String cpfNumerico = ValidadorCadastro.validareCpf(cpf);

        return cadastroRepository.findByCpfNumerico(cpfNumerico)
                .map(cadastro -> {
                    cadastroRepository.delete(cadastro);
                    return true;
                })
                .orElse(false);
    }

    private void preencherEndereco(CadastroPessoa cadastroPessoa, String cep) {
        String cepNumerico = ValidadorCadastro.validareCep(cep);
        EnderecoViaCepResponse endereco = viaCepClient.consultarCep(cepNumerico);

        if (endereco == null || Boolean.TRUE.equals(endereco.getErro())) {
            throw new CepInvalidoException("CEP informado não existe ou é inválido. Verifique e tente novamente.");
        }

        cadastroPessoa.setCep(endereco.getCep());
        cadastroPessoa.setLogradouro(endereco.getLogradouro());
        cadastroPessoa.setComplemento(endereco.getComplemento());
        cadastroPessoa.setUf(endereco.getUf());
        cadastroPessoa.setEstado(endereco.getEstado());
        cadastroPessoa.setBairro(endereco.getBairro());
        cadastroPessoa.setDdd(endereco.getDdd());
    }





    private void validarDadosAtualizacao(CadastroPessoa request) {
        if (request.getNomeCompleto() != null && request.getNomeCompleto().trim().isEmpty()) {
            throw new ValidacaoException("Nome completo não pode ser vazio");
        }

        if (request.getEmail() != null && !request.getEmail().contains("@")) {
            throw new ValidacaoException("Email inválido. Verifique o formato");
        }

        if (request.getCep() != null && request.getCep().trim().isEmpty()) {
            throw new ValidacaoException("CEP não pode ser vazio");
        }
    }
}
