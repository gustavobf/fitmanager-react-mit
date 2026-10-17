package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorRequest;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorResponse;
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

@Tag(name = "Instrutores", description = "Cadastro e consulta dos profissionais da academia.")
public interface InstrutorApi {
    @Operation(summary = "Listar instrutores", description = """
            Retorna instrutores paginados. O filtro por nome procura por trecho e ignora maiúsculas
            e minúsculas. A paginação começa em zero; tamanho padrão 20 e máximo 100.
            """)
    @ApiResponse(responseCode = "200", description = "Página de instrutores retornada.",
            content = @Content( examples = @ExampleObject(
                    value = """
                    {"conteudo":[{"id":3,"nome":"Carlos Lima","email":"carlos@example.com","telefone":"+55 11 99999-1111","especialidade":"Musculação","ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}],"pagina":0,"tamanho":20,"totalElementos":1,"totalPaginas":1}
                    """)))
    PageResponse<InstrutorResponse> listar(
            @Parameter(description = "Página desejada, começando em zero.", example = "0") @Min(0) int pagina,
            @Parameter(description = "Quantidade por página (1 a 100).", example = "20") @Min(1) @Max(100) int tamanho,
            @Parameter(description = "Filtra por parte do nome.", example = "carlos") String nome,
            @Parameter(description = "Filtra instrutores ativos ou inativos.", example = "true") Boolean ativo);

    @Operation(summary = "Cadastrar instrutor", description = """
            Cadastra instrutor. Nome, e-mail e especialidade são obrigatórios.
            E-mail deve ser válido e único. Se `ativo` for omitido, será `true`.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(name = "Novo instrutor", value = """
                                    {"nome":"Carlos Lima","email":"carlos@example.com","telefone":"+55 11 99999-1111","especialidade":"Musculação","ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Instrutor cadastrado.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":3,"nome":"Carlos Lima","email":"carlos@example.com","telefone":"+55 11 99999-1111","especialidade":"Musculação","ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado para outro instrutor.", content = @Content)
    })
    InstrutorResponse criar(@Valid InstrutorRequest request);

    @Operation(summary = "Buscar instrutor por ID", description = "Retorna os dados do instrutor identificado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrutor encontrado.",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Instrutor não encontrado.", content = @Content)
    })
    InstrutorResponse buscar(@Parameter(description = "Identificador numérico do instrutor.", example = "3") @Min(1) Long id);

    @Operation(summary = "Atualizar instrutor", description = """
            Substitui todos os campos editáveis. O e-mail deve ser válido e não pode pertencer
            a outro instrutor.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"nome":"Carlos Lima","email":"carlos@example.com","telefone":"+55 11 99999-2222","especialidade":"Treinamento funcional","ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instrutor atualizado.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Instrutor não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já pertence a outro instrutor.", content = @Content)
    })
    InstrutorResponse atualizar(@Parameter(description = "Identificador numérico do instrutor.", example = "3") @Min(1) Long id,
                                @Valid InstrutorRequest request);

    @Operation(summary = "Excluir instrutor", description = """
            Exclui fisicamente o instrutor quando não há agendamentos relacionados.
            Para instrutores com histórico, desative o cadastro em vez de excluí-lo.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Instrutor excluído."),
            @ApiResponse(responseCode = "404", description = "Instrutor não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Instrutor possui agendamentos relacionados.", content = @Content)
    })
    void excluir(@Parameter(description = "Identificador numérico do instrutor.", example = "3") @Min(1) Long id);
}
