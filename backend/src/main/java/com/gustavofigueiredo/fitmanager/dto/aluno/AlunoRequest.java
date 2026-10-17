package com.gustavofigueiredo.fitmanager.dto.aluno;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AlunoRequest(
        @NotBlank @Size(min = 2, max = 120) String nome,
        @NotBlank @Email @Size(max = 160) String email,
        @Size(max = 25) String telefone,
        LocalDate dataNascimento,
        Boolean ativo) {
}
