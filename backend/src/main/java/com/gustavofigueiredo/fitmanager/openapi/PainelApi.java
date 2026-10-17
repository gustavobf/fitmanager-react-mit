package com.gustavofigueiredo.fitmanager.openapi;

import com.gustavofigueiredo.fitmanager.dto.painel.PainelResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Painel", description = "Indicadores agregados para o dashboard administrativo.")
public interface PainelApi {
    @Operation(summary = "Consultar indicadores do painel", description = """
            Calcula os totais de alunos, alunos ativos, instrutores, matrículas ativas,
            matrículas ativas que vencem nos próximos sete dias e agendamentos de hoje.
            O dia corrente e os limites do dia usam o fuso America/Sao_Paulo.
            """)
    @ApiResponse(responseCode = "200", description = "Indicadores calculados.",
            content = @Content(
                    examples = @ExampleObject(name = "Resumo diário", value = """
                            {"totalAlunos":120,"alunosAtivos":108,"totalInstrutores":12,"matriculasAtivas":86,"matriculasVencendo":7,"agendamentosHoje":24}
                            """)))
    PainelResponse consultar();
}
