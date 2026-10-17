package com.gustavofigueiredo.fitmanager.dto.instrutor;

import com.gustavofigueiredo.fitmanager.entity.Instrutor;

import java.time.OffsetDateTime;

public record InstrutorResponse(Long id, String nome, String email, String telefone, String especialidade,
                                boolean ativo, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
    public static InstrutorResponse from(Instrutor instrutor) {
        return new InstrutorResponse(instrutor.getId(), instrutor.getNome(), instrutor.getEmail(),
                instrutor.getTelefone(), instrutor.getEspecialidade(), instrutor.isAtivo(),
                instrutor.getCriadoEm(), instrutor.getAtualizadoEm());
    }
}
