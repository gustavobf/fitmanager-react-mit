package com.gustavofigueiredo.fitmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "matriculas")
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long alunoId;
    @Column(nullable = false)
    private Long planoId;
    @Column(nullable = false)
    private LocalDate dataInicio;
    @Column(nullable = false)
    private LocalDate dataFim;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMatricula status;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    protected Matricula() {}
    public Matricula(Long alunoId, Long planoId, LocalDate dataInicio, LocalDate dataFim, StatusMatricula status) {
        this.alunoId = alunoId;
        this.planoId = planoId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
    }
    @PrePersist
    void aoCriar() { criadoEm = OffsetDateTime.now(); }
    public void atualizarStatus(StatusMatricula status) { this.status = status; }
    public Long getId() { return id; }
    public Long getAlunoId() { return alunoId; }
    public Long getPlanoId() { return planoId; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public StatusMatricula getStatus() { return status; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
}
