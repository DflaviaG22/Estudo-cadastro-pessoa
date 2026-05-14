package estudo.pessoa.cadastro.controller.docs;

import estudo.pessoa.cadastro.dto.CadastroPessoaResponse;
import estudo.pessoa.cadastro.dto.ErrorResponse;
import estudo.pessoa.cadastro.entity.CadastroPessoa;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Cadastro de Pessoas", description = "API para gerenciamento de cadastros de pessoas com integracao ViaCEP")
public interface CadastroControllerDocs {

    @Operation(summary = "Criar novo cadastro", description = "Cria um novo cadastro de pessoa com validacao de CPF e CEP")
    @ApiResponse(responseCode = "200", description = "Cadastro criado com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados invalidos ou obrigatorios ausentes", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "CPF ja cadastrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    CadastroPessoaResponse cadastrar(CadastroPessoa request);

    @Operation(summary = "Listar todos os cadastros", description = "Retorna uma lista com todos os cadastros de pessoas")
    @ApiResponse(responseCode = "200", description = "Lista de cadastros retornada com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoaResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro ao listar cadastros", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    List<CadastroPessoaResponse> listarTodos();

    @Operation(summary = "Atualizar cadastro por CPF", description = "Atualiza os dados de um cadastro existente usando o CPF como identificador")
    @ApiResponse(responseCode = "200", description = "Cadastro atualizado com sucesso", content = @Content(schema = @Schema(implementation = CadastroPessoaResponse.class)))
    @ApiResponse(responseCode = "400", description = "Dados invalidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cadastro nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro ao atualizar cadastro", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<CadastroPessoaResponse> atualizarPorCpf(String cpf, CadastroPessoa request);

    @Operation(summary = "Deletar cadastro por CPF", description = "Remove um cadastro de pessoa usando o CPF como identificador")
    @ApiResponse(responseCode = "204", description = "Cadastro deletado com sucesso")
    @ApiResponse(responseCode = "400", description = "CPF invalido", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Cadastro nao encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Erro ao deletar cadastro", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    ResponseEntity<Void> deletarPorCpf(String cpf);
}
