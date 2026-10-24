package com.gustavofigueiredo.fitmanager.dto.auth;

import java.time.Instant;

public record LoginResponse(String token, String nome, Instant expiraEm) {
}