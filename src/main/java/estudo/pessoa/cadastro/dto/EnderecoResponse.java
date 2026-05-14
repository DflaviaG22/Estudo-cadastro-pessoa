package estudo.pessoa.cadastro.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import estudo.pessoa.cadastro.entity.CadastroPessoa;

@JsonPropertyOrder({
    "cep",
    "logradouro",
    "bairro",
    "complemento",
    "estado",
    "endereco",
    "uf",
    "ddd"
})
public class EnderecoResponse {

    private String cep;
    private String logradouro;
    private String complemento;
    private String uf;
    private String estado;
    private String bairro;
    private String ddd;

    public static EnderecoResponse from(CadastroPessoa cadastroPessoa) {
        EnderecoResponse response = new EnderecoResponse();
        response.setCep(cadastroPessoa.getCep());
        response.setLogradouro(cadastroPessoa.getLogradouro());
        response.setComplemento(cadastroPessoa.getComplemento());
        response.setUf(cadastroPessoa.getUf());
        response.setEstado(cadastroPessoa.getEstado());
        response.setBairro(cadastroPessoa.getBairro());
        response.setDdd(cadastroPessoa.getDdd());
        return response;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
