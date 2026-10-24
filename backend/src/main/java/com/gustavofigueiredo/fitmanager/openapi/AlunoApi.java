package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoRequest;
import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoResponse;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
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

@Tag(name = "Alunos", description = "Cadastro, consulta e manutenção dos alunos da academia.")
public interface AlunoApi {
    @Operation(summary = "Listar alunos", description = """
            Retorna alunos de forma paginada. Os filtros textuais são parciais e não diferenciam
            maiúsculas de minúsculas. A paginação começa em zero; o tamanho padrão é 20 e o máximo é 100.
            """)
    @ApiResponse(responseCode = "200", description = "Página de alunos retornada.",
            content = @Content( examples = @ExampleObject(
                    name = "Página de alunos", value = """
                    {"conteudo":[{"id":1,"nome":"Ana Souza","email":"ana.souza@example.com","telefone":"+55 21 99999-0000","dataNascimento":"2000-05-14","ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}],"pagina":0,"tamanho":20,"totalElementos":1,"totalPaginas":1}
                    """)))
    @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos.", content = @Content)
    PageResponse<AlunoResponse> listar(
            @Parameter(description = "Página desejada, começando em zero.", example = "0") @Min(0) int pagina,
            @Parameter(description = "Quantidade de registros por página (1 a 100).", example = "20") @Min(1) @Max(100) int tamanho,
            @Parameter(description = "Filtra por parte do nome, sem diferenciar maiúsculas.", example = "ana") String nome,
            @Parameter(description = "Filtra por parte do e-mail.", example = "example.com") String email,
            @Parameter(description = "Filtra pela situação ativa ou inativa.", example = "true") Boolean ativo);

    @Operation(summary = "Cadastrar aluno", description = """
            Cadastra aluno. Nome e e-mail são obrigatórios; o e-mail deve ser válido e único.
            Se `ativo` for omitido, o aluno será cadastrado como ativo.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Dados do aluno. Data de nascimento deve usar o formato ISO `yyyy-MM-dd`.",
                    content = @Content(
                            examples = @ExampleObject(name = "Novo aluno", value = """
                                    {"nome":"Ana Souza","email":"ana.souza@example.com","telefone":"+55 21 99999-0000","dataNascimento":"2000-05-14","ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":1,"nome":"Ana Souza","email":"ana.souza@example.com","telefone":"+55 21 99999-0000","dataNascimento":"2000-05-14","ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"REQUISICAO_INVALIDA","mensagem":"A requisição contém dados inválidos.","detalhes":["email: deve ser um endereço de e-mail bem formado"],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """))),
            @ApiResponse(responseCode = "409", description = "Já existe aluno com o e-mail informado.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"EMAIL_DUPLICADO","mensagem":"Já existe um aluno com esse e-mail.","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
    AlunoResponse criar(@Valid AlunoRequest request);

    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados completos do aluno identificado pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno encontrado.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"id":1,"nome":"Ana Souza","email":"ana.souza@example.com","telefone":"+55 21 99999-0000","dataNascimento":"2000-05-14","ativo":true,"criadoEm":"2026-10-17T10:00:00-03:00","atualizadoEm":"2026-10-17T10:00:00-03:00"}
                                    """))),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"RECURSO_NAO_ENCONTRADO","mensagem":"Aluno não encontrado(a).","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
    AlunoResponse buscar(@Parameter(description = "Identificador numérico do aluno.", example = "1") @Min(1) Long id);

    @Operation(summary = "Atualizar aluno", description = """
            Substitui todos os campos editáveis do aluno. Nome e e-mail são obrigatórios.
            O e-mail deve permanecer único entre os alunos.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Representação completa dos campos editáveis do aluno.",
                    content = @Content(
                            examples = @ExampleObject(value = """
                                    {"nome":"Ana Souza Silva","email":"ana.souza@example.com","telefone":"+55 21 99999-1111","dataNascimento":"2000-05-14","ativo":true}
                                    """))))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aluno atualizado.",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já pertence a outro aluno.", content = @Content)
    })
    AlunoResponse atualizar(@Parameter(description = "Identificador numérico do aluno.", example = "1") @Min(1) Long id,
                            @Valid AlunoRequest request);

    @Operation(summary = "Excluir aluno", description = """
            Exclui fisicamente o aluno. Por padrão, a exclusão é recusada se existir qualquer matrícula
            ou agendamento relacionado. Envie `apagarHistorico=true` somente após confirmar que deseja
            excluir permanentemente esses registros junto com o aluno.
            """)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aluno excluído."),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Aluno possui histórico relacionado.", content = @Content(
                    examples = @ExampleObject(value = """
                            {"codigo":"ALUNO_COM_HISTORICO","mensagem":"O aluno possui histórico relacionado e não pode ser excluído.","detalhes":[],"dataHora":"2026-10-17T10:00:00-03:00"}
                            """)))
    })
        void excluir(@Parameter(description = "Identificador numérico do aluno.", example = "1") @Min(1) Long id,
                                 @Parameter(description = "Confirma a exclusão permanente das matrículas e agendamentos relacionados.",
                         example = "true", required = false) boolean apagarHistorico);
}
