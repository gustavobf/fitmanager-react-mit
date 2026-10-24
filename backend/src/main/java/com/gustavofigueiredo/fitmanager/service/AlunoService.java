package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoRequest;
import com.gustavofigueiredo.fitmanager.dto.aluno.AlunoResponse;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.entity.Aluno;
import com.gustavofigueiredo.fitmanager.exception.ApiException;
import com.gustavofigueiredo.fitmanager.repository.AgendamentoRepository;
import com.gustavofigueiredo.fitmanager.repository.AlunoRepository;
import com.gustavofigueiredo.fitmanager.repository.MatriculaRepository;

import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlunoService {
    private final AlunoRepository repository;
    private final MatriculaRepository matriculas;
    private final AgendamentoRepository agendamentos;

    public AlunoService(AlunoRepository repository, MatriculaRepository matriculas,
                        AgendamentoRepository agendamentos) {
        this.repository = repository;
        this.matriculas = matriculas;
        this.agendamentos = agendamentos;
    }

    @Transactional(readOnly = true)
    public PageResponse<AlunoResponse> listar(int pagina, int tamanho, String nome, String email, Boolean ativo) {
        Specification<Aluno> spec = (root, query, cb) -> cb.conjunction();
        if (nome != null && !nome.isBlank()) {
            String filter = "%" + nome.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), filter));
        }
        if (email != null && !email.isBlank()) {
            String filter = "%" + email.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), filter));
        }
        if (ativo != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        return PageResponse.from(repository.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("id").ascending()))
                .map(AlunoResponse::from));
    }

    public AlunoResponse criar(AlunoRequest request) {
        validarEmail(request.email(), null);
        return AlunoResponse.from(repository.save(new Aluno(request.nome().trim(), request.email().trim(),
                request.telefone(), request.dataNascimento(), request.ativo() == null || request.ativo())));
    }

    @Transactional(readOnly = true)
    public AlunoResponse buscar(Long id) {
        return AlunoResponse.from(obter(id));
    }

    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno aluno = obter(id);
        validarEmail(request.email(), id);
        aluno.atualizar(request.nome().trim(), request.email().trim(), request.telefone(),
                request.dataNascimento(), request.ativo() == null || request.ativo());
        return AlunoResponse.from(repository.save(aluno));
    }

    public void excluir(Long id, boolean apagarHistorico) {
        Aluno aluno = obter(id);
        boolean possuiHistorico = matriculas.existsByAlunoId(id) || agendamentos.existsByAlunoId(id);
        if (possuiHistorico && !apagarHistorico) {
            throw ApiException.conflito("ALUNO_COM_HISTORICO",
                    "O aluno possui histórico relacionado e não pode ser excluído.");
        }
        if (apagarHistorico) {
            matriculas.deleteAllByAlunoId(id);
            agendamentos.deleteAllByAlunoId(id);
        }
        repository.delete(aluno);
    }

    private Aluno obter(Long id) {
        return repository.findById(id).orElseThrow(() -> ApiException.naoEncontrado("Aluno"));
    }

    private void validarEmail(String email, Long id) {
        boolean exists = id == null ? repository.existsByEmailIgnoreCase(email)
                : repository.existsByEmailIgnoreCaseAndIdNot(email, id);
        if (exists) throw ApiException.conflito("EMAIL_DUPLICADO", "Já existe um aluno com esse e-mail.");
    }
}
