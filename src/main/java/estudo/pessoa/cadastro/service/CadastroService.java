package estudo.pessoa.cadastro.service;

import estudo.pessoa.cadastro.model.DadosCadastroCliente;
import estudo.pessoa.cadastro.utils.FormatacaoCampo;

public class CadastroService {

    public static DadosCadastroCliente cadastrar(DadosCadastroCliente request) {
        String cpfFormatado = FormatacaoCampo.formatarCpf(request.getCpf());
        request.setCpf(cpfFormatado);

        String telefoneFormatado = FormatacaoCampo.formatarTelefone(request.getTelefone());
        request.setTelefone(telefoneFormatado);
        return request; //adicionado
    }
}
