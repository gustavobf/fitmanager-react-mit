package com.gustavofigueiredo.fitmanager.dto.common;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> conteudo, int pagina, int tamanho, long totalElementos, int totalPaginas) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
