package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoRequest;
import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoResponse;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.openapi.AlunoApi;
import com.gustavofigueiredo.fitmanager.service.AlunoService;

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
@RequestMapping("/api/alunos")
public class AlunoController implements AlunoApi {
    private final AlunoService service;
    public AlunoController(AlunoService service) { this.service = service; }

    @GetMapping
    public PageResponse<AlunoResponse> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean ativo) {
        return service.listar(pagina, tamanho, nome, email, ativo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponse criar(@RequestBody AlunoRequest request) { return service.criar(request); }
    @GetMapping("/{id}")
    public AlunoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}")
    public AlunoResponse atualizar(@PathVariable Long id, @RequestBody AlunoRequest request) {
        return service.atualizar(id, request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
