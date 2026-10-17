package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.painel.PainelResponse;
import com.gustavofigueiredo.fitmanager.openapi.PainelApi;
import com.gustavofigueiredo.fitmanager.service.PainelService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/painel")
public class PainelController implements PainelApi {
    private final PainelService service;
    public PainelController(PainelService service) { this.service = service; }

    @GetMapping
    public PainelResponse consultar() { return service.consultar(); }
}
