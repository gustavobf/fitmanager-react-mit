package com.gustavofigueiredo.fitmanager.dto.agendamento;

import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;

import jakarta.validation.constraints.NotNull;

public record StatusAgendamentoRequest(@NotNull StatusAgendamento status) {
}
