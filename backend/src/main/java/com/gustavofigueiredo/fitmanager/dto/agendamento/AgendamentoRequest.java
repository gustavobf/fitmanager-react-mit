package com.gustavofigueiredo.fitmanager.dto.agendamento;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AgendamentoRequest(@NotNull @Positive Long alunoId, @NotNull @Positive Long instrutorId,
                                 @NotNull LocalDateTime dataHora,
                                 @Min(15) @Max(240) int duracaoEmMinutos,
                                 @Size(max = 500) String observacoes) {
}
