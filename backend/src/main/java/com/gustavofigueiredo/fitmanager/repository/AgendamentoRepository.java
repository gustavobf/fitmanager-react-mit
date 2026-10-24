package com.gustavofigueiredo.fitmanager.repository;

import com.gustavofigueiredo.fitmanager.entity.Agendamento;
import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    boolean existsByAlunoId(Long alunoId);
    void deleteAllByAlunoId(Long alunoId);
    boolean existsByInstrutorId(Long instrutorId);
    long countByDataHoraGreaterThanEqualAndDataHoraLessThan(OffsetDateTime inicio, OffsetDateTime fim);
    List<Agendamento> findByInstrutorIdAndStatusNotAndDataHoraLessThan(
            Long instrutorId, StatusAgendamento status, OffsetDateTime dataHora);
    long countByStatus(StatusAgendamento status);
}
