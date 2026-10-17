package com.gustavofigueiredo.fitmanager.dto.matricula;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record MatriculaRequest(@NotNull @Positive Long alunoId, @NotNull @Positive Long planoId,
                               @NotNull LocalDate dataInicio) {
}
