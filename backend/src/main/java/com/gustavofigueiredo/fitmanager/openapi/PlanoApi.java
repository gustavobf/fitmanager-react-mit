package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoRequest;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Planos", description = "Planos comerciais e condições de acesso à academia.")
public interface PlanoApi {
    @Operation(summary = "Listar planos", description = """
            Lista planos com paginação. O filtro de nome procura por trecho, sem diferenciar
            maiúsculas de minúsculas. Páginas começam em zero; tamanho padrão 20, máximo 100.
            """)
    @ApiResponse(responseCode = "200", description = "Página de planos retornada.",
            content = @Content( examples = @ExampleObject(
                    value = """
                    {"conteudo":[{"id":2,"nome":"Plano Mensal","descricao":"Acesso à academia por 30 dias","duracaoEmDias":30,"preco":129.90,"ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}],"pagina":0,"tamanho":20,"totalElementos":1,"totalPaginas":1}
                    """)))
    PageResponse<PlanoResponse> listar(
            @Parameter(description = "Página desejada, começando em zero.", example = "0") @Min(0) int pagina,
            @Parameter(description = "Quantidade por página (1 a 100).", example = "20") @Min(1) @Max(100) int tamanho,
            @Parameter(description = "Filtra planos ativos ou inativos.", example = "true") Boolean ativo,
            @Parameter(description = "Filtra por trecho do nome.", example = "mensal") String nome);

    @Operation(summary = "Cadastrar plano", description = """
            Cria um plano comercial. Nome, duração e preço são obrigatórios.
            A duração deve estar entre 1 e 730 dias, preço deve ser não negativo e o nome deve ser único.
            Se `ativo` não for informado, será `true`.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Dados comerciais do plano.",
                    content = @Content(
                            examples = @ExampleObject(name = "Plano mensal", value = """
                                    {"nome":"Plano Mensal","descricao":"Acesso à academia por 30 dias","duracaoEmDias":30,"preco":129.90,"ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plano cadastrado.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":2,"nome":"Plano Mensal","descricao":"Acesso à academia por 30 dias","duracaoEmDias":30,"preco":129.90,"ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Já existe plano com o mesmo nome.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"NOME_DUPLICADO","mensagem":"Já existe um plano com o mesmo nome.","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
    PlanoResponse criar(@Valid PlanoRequest request);

    @Operation(summary = "Buscar plano por ID", description = "Retorna os dados de um plano.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plano encontrado.",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado.", content = @Content)
    })
    PlanoResponse buscar(@Parameter(description = "Identificador numérico do plano.", example = "2") @Min(1) Long id);

    @Operation(summary = "Atualizar plano", description = """
            Substitui os campos editáveis do plano. O ID não pode ser alterado.
            A duração deve estar entre 1 e 730 dias e o preço deve ser não negativo.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"nome":"Plano Trimestral","descricao":"Acesso por 90 dias","duracaoEmDias":90,"preco":349.90,"ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plano atualizado.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Já existe outro plano com o nome informado.", content = @Content)
    })
    PlanoResponse atualizar(@Parameter(description = "Identificador numérico do plano.", example = "2") @Min(1) Long id,
                            @Valid PlanoRequest request);

    @Operation(summary = "Excluir plano", description = """
            Exclui fisicamente o plano somente quando ele não está referenciado por matrículas.
            Para planos com histórico, atualize o campo `ativo` para `false`.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plano excluído."),
            @ApiResponse(responseCode = "404", description = "Plano não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Plano vinculado a matrículas.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"PLANO_VINCULADO","mensagem":"O plano está vinculado a matrículas e não pode ser excluído.","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
    void excluir(@Parameter(description = "Identificador numérico do plano.", example = "2") @Min(1) Long id);
}
