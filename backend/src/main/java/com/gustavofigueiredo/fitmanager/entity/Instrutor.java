package com.gustavofigueiredo.fitmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "instrutores")
public class Instrutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, unique = true, length = 160)
    private String email;
    @Column(length = 25)
    private String telefone;
    @Column(nullable = false, length = 100)
    private String especialidade;
    @Column(nullable = false)
    private boolean ativo = true;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime criadoEm;
    @Column(nullable = false)
    private OffsetDateTime atualizadoEm;

    protected Instrutor() {}
    public Instrutor(String nome, String email, String telefone, String especialidade, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }
    @PrePersist
    void aoCriar() { criadoEm = atualizadoEm = OffsetDateTime.now(); }
    @PreUpdate
    void aoAtualizar() { atualizadoEm = OffsetDateTime.now(); }
    public void atualizar(String nome, String email, String telefone, String especialidade, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.ativo = ativo;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEspecialidade() { return especialidade; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public OffsetDateTime getAtualizadoEm() { return atualizadoEm; }
}
