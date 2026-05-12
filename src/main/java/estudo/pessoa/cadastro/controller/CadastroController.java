package estudo.pessoa.cadastro.controller;


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
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping
    public CadastroPessoa cadastrar(@RequestBody CadastroPessoa request) {
        return cadastroService.cadastrar(request);
    }

    @GetMapping
    public List<CadastroPessoa> listarTodos() {
        return cadastroService.listarTodos();
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<CadastroPessoa> consultarPorCpf(@PathVariable String cpf) {
        return cadastroService.consultarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<CadastroPessoa> atualizarPorCpf(
            @PathVariable String cpf,
            @RequestBody CadastroPessoa request
    ) {
        return cadastroService.atualizarPorCpf(cpf, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarPorCpf(@PathVariable String cpf) {
        boolean deletado = cadastroService.deletarPorCpf(cpf);

        if (deletado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}