package com.gustavofigueiredo.fitmanager.dto.agendamento;

import com.gustavofigueiredo.fitmanager.entity.Agendamento;
import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;

import java.time.OffsetDateTime;

public record AgendamentoResponse(Long id, Long alunoId, Long instrutorId, OffsetDateTime dataHora,
                                  int duracaoEmMinutos, String observacoes, StatusAgendamento status,
                                  OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
    public static AgendamentoResponse from(Agendamento agendamento) {
        return new AgendamentoResponse(agendamento.getId(), agendamento.getAlunoId(), agendamento.getInstrutorId(),
                agendamento.getDataHora(), agendamento.getDuracaoEmMinutos(), agendamento.getObservacoes(),
                agendamento.getStatus(), agendamento.getCriadoEm(), agendamento.getAtualizadoEm());
    }
}
