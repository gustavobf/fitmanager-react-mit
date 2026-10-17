package com.gustavofigueiredo.fitmanager.dto.matricula;

import com.gustavofigueiredo.fitmanager.entity.Matricula;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record MatriculaResponse(Long id, Long alunoId, Long planoId, LocalDate dataInicio, LocalDate dataFim,
                                StatusMatricula status, OffsetDateTime criadoEm) {
    public static MatriculaResponse from(Matricula matricula) {
        return new MatriculaResponse(matricula.getId(), matricula.getAlunoId(), matricula.getPlanoId(),
                matricula.getDataInicio(), matricula.getDataFim(), matricula.getStatus(), matricula.getCriadoEm());
    }
}
