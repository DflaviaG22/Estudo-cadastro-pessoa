package estudo.pessoa.cadastro.dto;

import estudo.pessoa.cadastro.entity.CadastroPessoa;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({
    "nomeCompleto",
    "cpf",
    "email",
    "telefone",
    "endereco"
})
public class CadastroPessoaResponse {

    private String cpf;
    private String nomeCompleto;
    private String telefone;
    private String email;
    private List<EnderecoResponse> endereco;

    public static CadastroPessoaResponse from(CadastroPessoa cadastroPessoa) {
        CadastroPessoaResponse response = new CadastroPessoaResponse();
        response.setCpf(cadastroPessoa.getCpf());
        response.setNomeCompleto(cadastroPessoa.getNomeCompleto());
        response.setTelefone(cadastroPessoa.getTelefone());
        response.setEmail(cadastroPessoa.getEmail());
        response.setEndereco(List.of(EnderecoResponse.from(cadastroPessoa)));
        return response;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<EnderecoResponse> getEndereco() {
        return endereco;
    }

    public void setEndereco(List<EnderecoResponse> endereco) {
        this.endereco = endereco;
    }
}
