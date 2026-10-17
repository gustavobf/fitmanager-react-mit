package com.gustavofigueiredo.fitmanager.dto.aluno;

import com.gustavofigueiredo.fitmanager.entity.Aluno;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AlunoResponse(Long id, String nome, String email, String telefone, LocalDate dataNascimento,
                            boolean ativo, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
    public static AlunoResponse from(Aluno aluno) {
        return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getTelefone(),
                aluno.getDataNascimento(), aluno.isAtivo(), aluno.getCriadoEm(), aluno.getAtualizadoEm());
    }
}
