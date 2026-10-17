package com.gustavofigueiredo.fitmanager.repository;

import com.gustavofigueiredo.fitmanager.entity.Matricula;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>, JpaSpecificationExecutor<Matricula> {
    boolean existsByAlunoId(Long alunoId);
    boolean existsByPlanoId(Long planoId);
    long countByStatus(StatusMatricula status);
    long countByStatusAndDataFimBetween(StatusMatricula status, java.time.LocalDate inicio, java.time.LocalDate fim);
}
