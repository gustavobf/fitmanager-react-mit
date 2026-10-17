package com.gustavofigueiredo.fitmanager.repository;

import com.gustavofigueiredo.fitmanager.entity.Instrutor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long>, JpaSpecificationExecutor<Instrutor> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
