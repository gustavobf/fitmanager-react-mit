package com.gustavofigueiredo.fitmanager.dto.common;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiError(String codigo, String mensagem, List<String> detalhes, OffsetDateTime dataHora) {
}
