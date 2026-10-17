package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.agendamento.AgendamentoResponse;
import com.gustavofigueiredo.fitmanager.dto.agendamento.StatusAgendamentoRequest;
import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.entity.Agendamento;
import com.gustavofigueiredo.fitmanager.entity.Aluno;
import com.gustavofigueiredo.fitmanager.entity.Instrutor;
import com.gustavofigueiredo.fitmanager.entity.StatusAgendamento;
import com.gustavofigueiredo.fitmanager.exception.ApiException;
import com.gustavofigueiredo.fitmanager.repository.AgendamentoRepository;
import com.gustavofigueiredo.fitmanager.repository.AlunoRepository;
import com.gustavofigueiredo.fitmanager.repository.InstrutorRepository;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgendamentoService {
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Sao_Paulo");
    private final AgendamentoRepository repository;
    private final AlunoRepository alunos;
    private final InstrutorRepository instrutores;

    public AgendamentoService(AgendamentoRepository repository, AlunoRepository alunos,
                              InstrutorRepository instrutores) {
        this.repository = repository;
        this.alunos = alunos;
        this.instrutores = instrutores;
    }

    @Transactional(readOnly = true)
    public PageResponse<AgendamentoResponse> listar(int pagina, int tamanho, Long alunoId, Long instrutorId,
            StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim) {
        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw ApiException.requisicaoInvalida("O fim do intervalo deve ser posterior ao início.");
        }
        Specification<Agendamento> spec = (root, query, cb) -> cb.conjunction();
        if (alunoId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("alunoId"), alunoId));
        if (instrutorId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("instrutorId"), instrutorId));
        if (status != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        OffsetDateTime inicioComFuso = converterParaFusoDeNegocio(inicio);
        OffsetDateTime fimComFuso = converterParaFusoDeNegocio(fim);
        if (inicioComFuso != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataHora"), inicioComFuso));
        }
        if (fimComFuso != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataHora"), fimComFuso));
        }
        return PageResponse.from(repository.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("dataHora").ascending()))
                .map(AgendamentoResponse::from));
    }

    public AgendamentoResponse criar(AgendamentoRequest request) {
        validarParticipantes(request.alunoId(), request.instrutorId());
        OffsetDateTime dataHora = converterParaFusoDeNegocio(request.dataHora());
        validarConflito(request.instrutorId(), dataHora, request.duracaoEmMinutos(), null);
        Agendamento agendamento = new Agendamento(request.alunoId(), request.instrutorId(), dataHora,
                request.duracaoEmMinutos(), request.observacoes(), StatusAgendamento.AGENDADO);
        return AgendamentoResponse.from(repository.save(agendamento));
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscar(Long id) { return AgendamentoResponse.from(obter(id)); }

    public AgendamentoResponse atualizar(Long id, AgendamentoRequest request) {
        Agendamento agendamento = obter(id);
        validarParticipantes(request.alunoId(), request.instrutorId());
        OffsetDateTime dataHora = converterParaFusoDeNegocio(request.dataHora());
        validarConflito(request.instrutorId(), dataHora, request.duracaoEmMinutos(), id);
        agendamento.atualizar(request.alunoId(), request.instrutorId(), dataHora,
                request.duracaoEmMinutos(), request.observacoes());
        return AgendamentoResponse.from(repository.save(agendamento));
    }

    public AgendamentoResponse atualizarStatus(Long id, StatusAgendamentoRequest request) {
        Agendamento agendamento = obter(id);
        agendamento.atualizarStatus(request.status());
        return AgendamentoResponse.from(repository.save(agendamento));
    }

    public void excluir(Long id) { repository.delete(obter(id)); }

    private Agendamento obter(Long id) {
        return repository.findById(id).orElseThrow(() -> ApiException.naoEncontrado("Agendamento"));
    }

    private OffsetDateTime converterParaFusoDeNegocio(LocalDateTime dataHora) {
        return dataHora == null ? null : dataHora.atZone(BUSINESS_ZONE).toOffsetDateTime();
    }

    private void validarParticipantes(Long alunoId, Long instrutorId) {
        Aluno aluno = alunos.findById(alunoId).orElseThrow(() -> ApiException.naoEncontrado("Aluno"));
        Instrutor instrutor = instrutores.findById(instrutorId)
                .orElseThrow(() -> ApiException.naoEncontrado("Instrutor"));
        if (!aluno.isAtivo()) throw ApiException.conflito("ALUNO_INATIVO", "O aluno está inativo.");
        if (!instrutor.isAtivo()) throw ApiException.conflito("INSTRUTOR_INATIVO", "O instrutor está inativo.");
    }

    private void validarConflito(Long instrutorId, OffsetDateTime inicio, int duracao, Long idIgnorado) {
        OffsetDateTime fim = inicio.plusMinutes(duracao);
        boolean conflito = repository.findByInstrutorIdAndStatusNotAndDataHoraLessThan(
                        instrutorId, StatusAgendamento.CANCELADO, fim).stream()
                .anyMatch(existing -> !existing.getId().equals(idIgnorado)
                        && existing.getDataHora().plusMinutes(existing.getDuracaoEmMinutos()).isAfter(inicio));
        if (conflito) {
            throw ApiException.conflito("CONFLITO_DE_HORARIO",
                    "O instrutor já possui um agendamento nesse intervalo.");
        }
    }
}
