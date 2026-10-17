package com.gustavofigueiredo.fitmanager.repository;

import com.gustavofigueiredo.fitmanager.entity.Plano;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlanoRepository extends JpaRepository<Plano, Long>, JpaSpecificationExecutor<Plano> {
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
