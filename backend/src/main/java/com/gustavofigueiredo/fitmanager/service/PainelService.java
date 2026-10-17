package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.painel.PainelResponse;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;
import com.gustavofigueiredo.fitmanager.repository.AgendamentoRepository;
import com.gustavofigueiredo.fitmanager.repository.AlunoRepository;
import com.gustavofigueiredo.fitmanager.repository.InstrutorRepository;
import com.gustavofigueiredo.fitmanager.repository.MatriculaRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PainelService {
    private static final ZoneId ZONE = ZoneId.of("America/Sao_Paulo");
    private final AlunoRepository alunos;
    private final InstrutorRepository instrutores;
    private final MatriculaRepository matriculas;
    private final AgendamentoRepository agendamentos;

    public PainelService(AlunoRepository alunos, InstrutorRepository instrutores,
                        MatriculaRepository matriculas, AgendamentoRepository agendamentos) {
        this.alunos = alunos;
        this.instrutores = instrutores;
        this.matriculas = matriculas;
        this.agendamentos = agendamentos;
    }

    @Transactional(readOnly = true)
    public PainelResponse consultar() {
        LocalDate hoje = LocalDate.now(ZONE);
        return new PainelResponse(alunos.count(), alunos.countByAtivoTrue(), instrutores.count(),
                matriculas.countByStatus(StatusMatricula.ATIVA),
                matriculas.countByStatusAndDataFimBetween(StatusMatricula.ATIVA, hoje, hoje.plusDays(7)),
                agendamentos.countByDataHoraGreaterThanEqualAndDataHoraLessThan(
                        hoje.atStartOfDay(ZONE).toOffsetDateTime(), hoje.plusDays(1).atStartOfDay(ZONE).toOffsetDateTime()));
    }
}
