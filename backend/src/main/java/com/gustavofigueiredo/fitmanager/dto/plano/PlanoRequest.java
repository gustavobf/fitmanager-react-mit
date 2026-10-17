package com.gustavofigueiredo.fitmanager.dto.plano;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record PlanoRequest(
        @NotBlank @Size(min = 2, max = 80) String nome,
        @Size(max = 500) String descricao,
        @Min(1) @Max(730) int duracaoEmDias,
        @NotNull @DecimalMin("0.0") BigDecimal preco,
        Boolean ativo) {
}
