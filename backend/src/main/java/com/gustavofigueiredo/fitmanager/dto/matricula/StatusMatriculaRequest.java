package com.gustavofigueiredo.fitmanager.dto.matricula;

import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;

import jakarta.validation.constraints.NotNull;

public record StatusMatriculaRequest(@NotNull StatusMatricula status) {
}
