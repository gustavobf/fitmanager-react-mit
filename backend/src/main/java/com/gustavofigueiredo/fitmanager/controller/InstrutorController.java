package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorRequest;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorResponse;
import com.gustavofigueiredo.fitmanager.openapi.InstrutorApi;
import com.gustavofigueiredo.fitmanager.service.InstrutorService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/instrutores")
public class InstrutorController implements InstrutorApi {
    private final InstrutorService service;
    public InstrutorController(InstrutorService service) { this.service = service; }

    @GetMapping
    public PageResponse<InstrutorResponse> listar(@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) String nome, @RequestParam(required = false) Boolean ativo) {
        return service.listar(pagina, tamanho, nome, ativo);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstrutorResponse criar(@RequestBody InstrutorRequest request) { return service.criar(request); }
    @GetMapping("/{id}")
    public InstrutorResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}")
    public InstrutorResponse atualizar(@PathVariable Long id, @RequestBody InstrutorRequest request) {
        return service.atualizar(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
