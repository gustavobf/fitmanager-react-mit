package com.gustavofigueiredo.fitmanager.dto.painel;

public record PainelResponse(long totalAlunos, long alunosAtivos, long totalInstrutores, long matriculasAtivas,
                             long matriculasVencendo, long agendamentosHoje) {
}
