package com.gustavofigueiredo.fitmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "alunos")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, unique = true, length = 160)
    private String email;
    @Column(length = 25)
    private String telefone;
    private LocalDate dataNascimento;
    @Column(nullable = false)
    private boolean ativo = true;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime criadoEm;
    @Column(nullable = false)
    private OffsetDateTime atualizadoEm;

    protected Aluno() {}

    public Aluno(String nome, String email, String telefone, LocalDate dataNascimento, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
    }

    @PrePersist
    void aoCriar() {
        criadoEm = atualizadoEm = OffsetDateTime.now();
    }

    @PreUpdate
    void aoAtualizar() {
        atualizadoEm = OffsetDateTime.now();
    }

    public void atualizar(String nome, String email, String telefone, LocalDate dataNascimento, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public OffsetDateTime getAtualizadoEm() { return atualizadoEm; }
}
