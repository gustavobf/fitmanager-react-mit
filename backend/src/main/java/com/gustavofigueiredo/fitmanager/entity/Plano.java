package com.gustavofigueiredo.fitmanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "planos")
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String nome;
    @Column(length = 500)
    private String descricao;
    @Column(nullable = false)
    private int duracaoEmDias;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(nullable = false)
    private boolean ativo = true;
    @Column(nullable = false, updatable = false)
    private OffsetDateTime criadoEm;
    @Column(nullable = false)
    private OffsetDateTime atualizadoEm;

    protected Plano() {}

    public Plano(String nome, String descricao, int duracaoEmDias, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.duracaoEmDias = duracaoEmDias;
        this.preco = preco;
        this.ativo = ativo;
    }

    @PrePersist
    void aoCriar() { criadoEm = atualizadoEm = OffsetDateTime.now(); }
    @PreUpdate
    void aoAtualizar() { atualizadoEm = OffsetDateTime.now(); }

    public void atualizar(String nome, String descricao, int duracaoEmDias, BigDecimal preco, boolean ativo) {
        this.nome = nome;
        this.descricao = descricao;
        this.duracaoEmDias = duracaoEmDias;
        this.preco = preco;
        this.ativo = ativo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getDuracaoEmDias() { return duracaoEmDias; }
    public BigDecimal getPreco() { return preco; }
    public boolean isAtivo() { return ativo; }
    public OffsetDateTime getCriadoEm() { return criadoEm; }
    public OffsetDateTime getAtualizadoEm() { return atualizadoEm; }
}
