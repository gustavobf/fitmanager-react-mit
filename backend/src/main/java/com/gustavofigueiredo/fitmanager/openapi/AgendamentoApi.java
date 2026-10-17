package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoResponse;
import com.gustavofigueiredo.fitmanager.dto.agendamento.StatusAgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;
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
import java.time.LocalDateTime;

@Tag(name = "Agendamentos", description = "Agendamento e acompanhamento de aulas e sessões.")
public interface AgendamentoApi {
    @Operation(summary = "Listar agendamentos", description = """
            Lista agendamentos com filtros opcionais por aluno, instrutor, situação e intervalo
            de data/hora. Informe o horário local de Brasília/São Paulo em ISO 8601, sem sufixo
            de fuso, por exemplo `2026-10-20T18:00:00`. Quando informados, `inicio` e `fim`
            delimitam a data/hora local do início do agendamento.
            """)
    @ApiResponse(responseCode = "200", description = "Página de agendamentos retornada.",
            content = @Content( examples = @ExampleObject(
                    value = """
                    {"conteudo":[{"id":20,"alunoId":1,"instrutorId":3,"dataHora":"2026-10-20T18:00:00-03:00","duracaoEmMinutos":60,"observacoes":"Treino funcional","status":"AGENDADO","criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}],"pagina":0,"tamanho":20,"totalElementos":1,"totalPaginas":1}
                    """)))
    PageResponse<AgendamentoResponse> listar(
            @Parameter(description = "Página desejada, começando em zero.", example = "0") @Min(0) int pagina,
            @Parameter(description = "Quantidade por página (1 a 100).", example = "20") @Min(1) @Max(100) int tamanho,
            @Parameter(description = "Identificador do aluno.", example = "1") @Positive Long alunoId,
            @Parameter(description = "Identificador do instrutor.", example = "3") @Positive Long instrutorId,
            @Parameter(description = "AGENDADO, CONCLUIDO, CANCELADO ou NAO_COMPARECEU.", example = "AGENDADO")
            StatusAgendamento status,
            @Parameter(description = "Início do período em horário local (America/Sao_Paulo).",
                    example = "2026-10-20T00:00:00") LocalDateTime inicio,
            @Parameter(description = "Fim do período em horário local (America/Sao_Paulo).",
                    example = "2026-10-21T00:00:00") LocalDateTime fim);

    @Operation(summary = "Criar agendamento", description = """
            Cria agendamento para aluno e instrutor ativos. A duração deve estar entre 15 e 240 minutos.
            Informe `dataHora` no horário local de Brasília/São Paulo, sem enviar offset; por
            exemplo, `2026-10-20T18:00:00`. O servidor interpreta esse valor no fuso
            `America/Sao_Paulo`. A operação retorna conflito se o instrutor já tiver outro
            agendamento não cancelado sobreposto no horário solicitado.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(name = "Sessão funcional", value = """
                                    {"alunoId":1,"instrutorId":3,"dataHora":"2026-10-20T18:00:00","duracaoEmMinutos":60,"observacoes":"Treino funcional"}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agendamento criado.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":20,"alunoId":1,"instrutorId":3,"dataHora":"2026-10-20T18:00:00-03:00","duracaoEmMinutos":60,"observacoes":"Treino funcional","status":"AGENDADO","criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aluno ou instrutor não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Participante inativo ou conflito de horário.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"CONFLITO_DE_HORARIO","mensagem":"O instrutor já possui um agendamento nesse intervalo.","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
    AgendamentoResponse criar(@Valid AgendamentoRequest request);

    @Operation(summary = "Buscar agendamento por ID", description = "Retorna todos os dados do agendamento.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado.",
                    content = @Content()),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content)
    })
    AgendamentoResponse buscar(
            @Parameter(description = "Identificador numérico do agendamento.", example = "20") @Min(1) Long id);

    @Operation(summary = "Atualizar agendamento", description = """
            Substitui aluno, instrutor, data/hora, duração e observações. Os participantes devem
            estar ativos e o horário não pode se sobrepor a outro agendamento não cancelado do instrutor.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"alunoId":1,"instrutorId":3,"dataHora":"2026-10-20T19:00:00","duracaoEmMinutos":45,"observacoes":"Treino atualizado"}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agendamento ou participante não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Participante inativo ou conflito de horário.", content = @Content)
    })
    AgendamentoResponse atualizar(
            @Parameter(description = "Identificador numérico do agendamento.", example = "20") @Min(1) Long id,
            @Valid AgendamentoRequest request);

    @Operation(summary = "Alterar situação do agendamento", description = """
            Atualiza somente a situação. Valores aceitos: `AGENDADO`, `CONCLUIDO`, `CANCELADO`
            e `NAO_COMPARECEU`.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"status":"CONCLUIDO"}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Situação atualizada.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Situação inválida.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content)
    })
    AgendamentoResponse atualizarStatus(
            @Parameter(description = "Identificador numérico do agendamento.", example = "20") @Min(1) Long id,
            @Valid StatusAgendamentoRequest request);

    @Operation(summary = "Excluir agendamento", description = "Remove permanentemente o agendamento informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agendamento excluído."),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado.", content = @Content)
    })
    void excluir(@Parameter(description = "Identificador numérico do agendamento.", example = "20") @Min(1) Long id);
}
