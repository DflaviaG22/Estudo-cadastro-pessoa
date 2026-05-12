package estudo.pessoa.cadastro.client;

import estudo.pessoa.cadastro.dto.EnderecoViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br")
public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json/")
    EnderecoViaCepResponse consultarCep(@PathVariable String cep);
}
