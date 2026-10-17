package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoResponse;
import com.gustavofigueiredo.fitmanager.dto.agendamento.StatusAgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;
import com.gustavofigueiredo.fitmanager.openapi.AgendamentoApi;
import com.gustavofigueiredo.fitmanager.service.AgendamentoService;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController implements AgendamentoApi {
    private final AgendamentoService service;
    public AgendamentoController(AgendamentoService service) { this.service = service; }

    @GetMapping
    public PageResponse<AgendamentoResponse> listar(@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long instrutorId,
            @RequestParam(required = false) StatusAgendamento status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return service.listar(pagina, tamanho, alunoId, instrutorId, status, inicio, fim);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse criar(@RequestBody AgendamentoRequest request) { return service.criar(request); }
    @GetMapping("/{id}")
    public AgendamentoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}")
    public AgendamentoResponse atualizar(@PathVariable Long id,
                                          @RequestBody AgendamentoRequest request) {
        return service.atualizar(id, request);
    }
    @PatchMapping("/{id}")
    public AgendamentoResponse atualizarStatus(@PathVariable Long id,
                                                @RequestBody StatusAgendamentoRequest request) {
        return service.atualizarStatus(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
