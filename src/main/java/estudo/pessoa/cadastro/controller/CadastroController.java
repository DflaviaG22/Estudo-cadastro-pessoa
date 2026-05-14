package estudo.pessoa.cadastro.controller;

import estudo.pessoa.cadastro.controller.docs.CadastroControllerDocs;
import estudo.pessoa.cadastro.dto.CadastroPessoaResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.service.CadastroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cadastro")
public class CadastroController implements CadastroControllerDocs {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @Override
    @PostMapping
    public CadastroPessoaResponse cadastrar(@RequestBody CadastroPessoa request) {
        return CadastroPessoaResponse.from(cadastroService.cadastrar(request));
    }

    @Override
    @GetMapping
    public List<CadastroPessoaResponse> listarTodos() {
        return cadastroService.listarTodos()
                .stream()
                .map(CadastroPessoaResponse::from)
                .toList();
    }

    @Override
    @PutMapping("/{cpf}")
    public ResponseEntity<CadastroPessoaResponse> atualizarPorCpf(
            @PathVariable String cpf,
            @RequestBody CadastroPessoa request
    ) {
        return cadastroService.atualizarPorCpf(cpf, request)
                .map(CadastroPessoaResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarPorCpf(@PathVariable String cpf) {
        boolean deletado = cadastroService.deletarPorCpf(cpf);

        if (deletado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
