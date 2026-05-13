package estudo.pessoa.cadastro.controller;


import estudo.pessoa.cadastro.entity.CadastroPessoa;
import estudo.pessoa.cadastro.service.CadastroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cadastro de Pessoas", description = "API para gerenciamento de cadastros de pessoas com integração ViaCEP")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping
    @Operation(summary = "Criar novo cadastro", description = "Cria um novo cadastro de pessoa com validação de CPF e CEP")
    @ApiResponse(responseCode = "200", description = "Cadastro criado com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoa.class)))
    public CadastroPessoa cadastrar(@RequestBody CadastroPessoa request) {
        return cadastroService.cadastrar(request);
    }

    @GetMapping
    @Operation(summary = "Listar todos os cadastros", description = "Retorna uma lista com todos os cadastros de pessoas")
    @ApiResponse(responseCode = "200", description = "Lista de cadastros retornada com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoa.class)))
    public List<CadastroPessoa> listarTodos() {
        return cadastroService.listarTodos();
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar cadastro por CPF", description = "Atualiza os dados de um cadastro existente usando o CPF como identificador")
    @ApiResponse(responseCode = "200", description = "Cadastro atualizado com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoa.class)))
    @ApiResponse(responseCode = "404", description = "Cadastro não encontrado")
    public ResponseEntity<CadastroPessoa> atualizarPorCpf(
            @PathVariable String cpf,
            @RequestBody CadastroPessoa request
    ) {
        return cadastroService.atualizarPorCpf(cpf, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{cpf}")
    @Operation(summary = "Deletar cadastro por CPF", description = "Remove um cadastro de pessoa usando o CPF como identificador")
    @ApiResponse(responseCode = "204", description = "Cadastro deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cadastro não encontrado")
    public ResponseEntity<Void> deletarPorCpf(@PathVariable String cpf) {
        boolean deletado = cadastroService.deletarPorCpf(cpf);

        if (deletado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}