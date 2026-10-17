package com.gustavofigueiredo.fitmanager.repository;

import com.gustavofigueiredo.fitmanager.entity.Aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlunoRepository extends JpaRepository<Aluno, Long>, JpaSpecificationExecutor<Aluno> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    long countByAtivoTrue();
}
