package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaRequest;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.StatusMatriculaRequest;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;
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
import jakarta.validation.constraints.Positive;

@Tag(name = "Matrículas", description = "Vínculos dos alunos com planos comerciais.")
public interface MatriculaApi {
    @Operation(summary = "Listar matrículas", description = """
            Retorna matrículas paginadas. Permite filtrar por aluno, plano e situação.
            A paginação começa em zero; tamanho padrão 20 e máximo 100.
            """)
    @ApiResponse(responseCode = "200", description = "Página de matrículas retornada.",
            content = @Content( examples = @ExampleObject(
                    value = """
                    {"conteudo":[{"id":10,"alunoId":1,"planoId":2,"dataInicio":"2026-10-17","dataFim":"2026-11-15","status":"ATIVA","criadoEm":"2026-10-17T10:00:00-03:00"}],"pagina":0,"tamanho":20,"totalElementos":1,"totalPaginas":1}
                    """)))
    PageResponse<MatriculaResponse> listar(
            @Parameter(description = "Página desejada, começando em zero.", example = "0") @Min(0) int pagina,
            @Parameter(description = "Quantidade por página (1 a 100).", example = "20") @Min(1) @Max(100) int tamanho,
            @Parameter(description = "Identificador do aluno.", example = "1") @Positive Long alunoId,
            @Parameter(description = "Identificador do plano.", example = "2") @Positive Long planoId,
            @Parameter(description = "Situação: ATIVA, ENCERRADA, CANCELADA ou VENCIDA.", example = "ATIVA")
            StatusMatricula status);

    @Operation(summary = "Criar matrícula", description = """
            Cria matrícula para um aluno ativo em um plano ativo. A data final é calculada com
            base na duração do plano; considera-se o dia de início como o primeiro dia de vigência.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(name = "Nova matrícula", value = """
                                    {"alunoId":1,"planoId":2,"dataInicio":"2026-10-17"}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Matrícula criada.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":10,"alunoId":1,"planoId":2,"dataInicio":"2026-10-17","dataFim":"2026-11-15","status":"ATIVA","criadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aluno ou plano não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Aluno inativo ou plano inativo.", content = @Content)
    })
    MatriculaResponse criar(@Valid MatriculaRequest request);

    @Operation(summary = "Buscar matrícula por ID", description = "Retorna uma matrícula específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matrícula encontrada.",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Matrícula não encontrada.", content = @Content)
    })
    MatriculaResponse buscar(@Parameter(description = "Identificador numérico da matrícula.", example = "10") @Min(1) Long id);

    @Operation(summary = "Atualizar situação da matrícula", description = """
            Atualiza somente a situação da matrícula. Valores aceitos: `ATIVA`, `ENCERRADA`,
            `CANCELADA` e `VENCIDA`.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"status":"CANCELADA"}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Situação atualizada.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Situação inválida.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Matrícula não encontrada.", content = @Content)
    })
    MatriculaResponse atualizarStatus(
            @Parameter(description = "Identificador numérico da matrícula.", example = "10") @Min(1) Long id,
            @Valid StatusMatriculaRequest request);
}
