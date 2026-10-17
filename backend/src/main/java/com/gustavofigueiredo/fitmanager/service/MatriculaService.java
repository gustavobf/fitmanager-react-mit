package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaRequest;
import com.gustavofigueiredo.fitmanager.dto.matricula.MatriculaResponse;
import com.gustavofigueiredo.fitmanager.dto.matricula.StatusMatriculaRequest;
import com.gustavofigueiredo.fitmanager.entity.Aluno;
import com.gustavofigueiredo.fitmanager.entity.Matricula;
import com.gustavofigueiredo.fitmanager.entity.Plano;
import com.gustavofigueiredo.fitmanager.entity.StatusMatricula;
import com.gustavofigueiredo.fitmanager.exception.ApiException;
import com.gustavofigueiredo.fitmanager.repository.AlunoRepository;
import com.gustavofigueiredo.fitmanager.repository.MatriculaRepository;
import com.gustavofigueiredo.fitmanager.repository.PlanoRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MatriculaService {
    private final MatriculaRepository repository;
    private final AlunoRepository alunos;
    private final PlanoRepository planos;

    public MatriculaService(MatriculaRepository repository, AlunoRepository alunos, PlanoRepository planos) {
        this.repository = repository;
        this.alunos = alunos;
        this.planos = planos;
    }

    @Transactional(readOnly = true)
    public PageResponse<MatriculaResponse> listar(int pagina, int tamanho, Long alunoId, Long planoId,
                                                  StatusMatricula status) {
        Specification<Matricula> spec = (root, query, cb) -> cb.conjunction();
        if (alunoId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("alunoId"), alunoId));
        if (planoId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("planoId"), planoId));
        if (status != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        return PageResponse.from(repository.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("id").ascending()))
                .map(MatriculaResponse::from));
    }

    public MatriculaResponse criar(MatriculaRequest request) {
        Aluno aluno = alunos.findById(request.alunoId())
                .orElseThrow(() -> ApiException.naoEncontrado("Aluno"));
        Plano plano = planos.findById(request.planoId())
                .orElseThrow(() -> ApiException.naoEncontrado("Plano"));
        if (!aluno.isAtivo()) throw ApiException.conflito("ALUNO_INATIVO", "O aluno está inativo.");
        if (!plano.isAtivo()) throw ApiException.conflito("PLANO_INATIVO", "O plano não está disponível para novas matrículas.");
        var fim = request.dataInicio().plusDays(plano.getDuracaoEmDias() - 1L);
        Matricula matricula = new Matricula(aluno.getId(), plano.getId(), request.dataInicio(), fim, StatusMatricula.ATIVA);
        return MatriculaResponse.from(repository.save(matricula));
    }

    @Transactional(readOnly = true)
    public MatriculaResponse buscar(Long id) { return MatriculaResponse.from(obter(id)); }

    public MatriculaResponse atualizarStatus(Long id, StatusMatriculaRequest request) {
        Matricula matricula = obter(id);
        matricula.atualizarStatus(request.status());
        return MatriculaResponse.from(repository.save(matricula));
    }

    private Matricula obter(Long id) {
        return repository.findById(id).orElseThrow(() -> ApiException.naoEncontrado("Matrícula"));
    }
}
