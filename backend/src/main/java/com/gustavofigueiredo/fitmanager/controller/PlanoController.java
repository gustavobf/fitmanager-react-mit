package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoRequest;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoResponse;
import com.gustavofigueiredo.fitmanager.openapi.PlanoApi;
import com.gustavofigueiredo.fitmanager.service.PlanoService;

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
@RequestMapping("/api/planos")
public class PlanoController implements PlanoApi {
    private final PlanoService service;
    public PlanoController(PlanoService service) { this.service = service; }

    @GetMapping
    public PageResponse<PlanoResponse> listar(@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) Boolean ativo, @RequestParam(required = false) String nome) {
        return service.listar(pagina, tamanho, ativo, nome);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoResponse criar(@RequestBody PlanoRequest request) { return service.criar(request); }
    @GetMapping("/{id}")
    public PlanoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}")
    public PlanoResponse atualizar(@PathVariable Long id, @RequestBody PlanoRequest request) {
        return service.atualizar(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
