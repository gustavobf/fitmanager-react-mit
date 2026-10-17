package com.gustavofigueiredo.fitmanager.controller;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaRequest;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.StatusMatriculaRequest;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;
import com.gustavofigueiredo.fitmanager.openapi.MatriculaApi;
import com.gustavofigueiredo.fitmanager.service.MatriculaService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController implements MatriculaApi {
    private final MatriculaService service;
    public MatriculaController(MatriculaService service) { this.service = service; }

    @GetMapping
    public PageResponse<MatriculaResponse> listar(@RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanho,
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long planoId,
            @RequestParam(required = false) StatusMatricula status) {
        return service.listar(pagina, tamanho, alunoId, planoId, status);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatriculaResponse criar(@RequestBody MatriculaRequest request) { return service.criar(request); }
    @GetMapping("/{id}")
    public MatriculaResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @PatchMapping("/{id}")
    public MatriculaResponse atualizarStatus(@PathVariable Long id,
                                              @RequestBody StatusMatriculaRequest request) {
        return service.atualizarStatus(id, request);
    }
}
