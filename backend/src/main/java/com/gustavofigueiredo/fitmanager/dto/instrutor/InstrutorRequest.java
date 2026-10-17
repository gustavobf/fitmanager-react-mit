package com.gustavofigueiredo.fitmanager.dto.instrutor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstrutorRequest(
        @NotBlank @Size(min = 2, max = 120) String nome,
        @NotBlank @Email @Size(max = 160) String email,
        @Size(max = 25) String telefone,
        @NotBlank @Size(max = 100) String especialidade,
        Boolean ativo) {
}
