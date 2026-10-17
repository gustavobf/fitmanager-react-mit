package com.gustavofigueiredo.fitmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long alunoId;
    @Column(nullable = false)
    private Long instrutorId;
    @Column(nullable = false)
    private OffsetDateTime dataHora;
    @Column(nullable = false)
    private int duracaoEmMinutos;
    @Column(length = 500)
    private String observacoes;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private StatusAgendamento status;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime criadoEm;
    @Column(nullable = false)
    private OffsetDateTime atualizadoEm;

    protected Agendamento() {}
    public Agendamento(Long alunoId, Long instrutorId, OffsetDateTime dataHora, int duracaoEmMinutos,
                       String observacoes, StatusAgendamento status) {
        this.alunoId = alunoId;
        this.instrutorId = instrutorId;
        this.dataHora = dataHora;
        this.duracaoEmMinutos = duracaoEmMinutos;
        this.observacoes = observacoes;
        this.status = status;
    }
    @PrePersist
    void aoCriar() { criadoEm = atualizadoEm = OffsetDateTime.now(); }
    @PreUpdate
    void aoAtualizar() { atualizadoEm = OffsetDateTime.now(); }
    public void atualizar(Long alunoId, Long instrutorId, OffsetDateTime dataHora, int duracaoEmMinutos,
                          String observacoes) {
        this.alunoId = alunoId;
        this.instrutorId = instrutorId;
        this.dataHora = dataHora;
        this.duracaoEmMinutos = duracaoEmMinutos;
        this.observacoes = observacoes;
    }
    public void atualizarStatus(StatusAgendamento status) { this.status = status; }
    public Long getId() { return id; }
    public Long getAlunoId() { return alunoId; }
    public Long getInstrutorId() { return instrutorId; }
    public OffsetDateTime getDataHora() { return dataHora; }
    public int getDuracaoEmMinutos() { return duracaoEmMinutos; }
    public String getObservacoes() { return observacoes; }
    public StatusAgendamento getStatus() { return status; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public OffsetDateTime getAtualizadoEm() { return atualizadoEm; }
}
