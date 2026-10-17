package com.gustavofigueiredo.fitmanager.dto.plano;

import com.gustavofigueiredo.fitmanager.entity.Plano;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PlanoResponse(Long id, String nome, String descricao, int duracaoEmDias, BigDecimal preco,
                            boolean ativo, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
    public static PlanoResponse from(Plano plano) {
        return new PlanoResponse(plano.getId(), plano.getNome(), plano.getDescricao(),
                plano.getDuracaoEmDias(), plano.getPreco(), plano.isAtivo(),
                plano.getCriadoEm(), plano.getAtualizadoEm());
    }
}
