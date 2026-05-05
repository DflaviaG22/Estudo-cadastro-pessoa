package estudo.pessoa.cadastro.controller;


import estudo.pessoa.cadastro.entity.DadosCadastroCliente;
import estudo.pessoa.cadastro.service.CadastroService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public DadosCadastroCliente cadastrar(@RequestBody DadosCadastroCliente request) {
        return cadastroService.cadastrar(request);
    }
}
