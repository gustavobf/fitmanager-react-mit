package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorRequest;
import com.gustavofigueiredo.fitmanager.dto.instrutor.InstrutorResponse;
import com.gustavofigueiredo.fitmanager.entity.Instrutor;
import com.gustavofigueiredo.fitmanager.exception.ApiException;
import com.gustavofigueiredo.fitmanager.repository.AgendamentoRepository;
import com.gustavofigueiredo.fitmanager.repository.InstrutorRepository;

import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InstrutorService {
    private final InstrutorRepository repository;
    private final AgendamentoRepository agendamentos;

    public InstrutorService(InstrutorRepository repository, AgendamentoRepository agendamentos) {
        this.repository = repository;
        this.agendamentos = agendamentos;
    }

    @Transactional(readOnly = true)
    public PageResponse<InstrutorResponse> listar(int pagina, int tamanho, String nome, Boolean ativo) {
        Specification<Instrutor> spec = (root, query, cb) -> cb.conjunction();
        if (nome != null && !nome.isBlank()) {
            String filter = "%" + nome.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), filter));
        }
        if (ativo != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        return PageResponse.from(repository.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("id").ascending()))
                .map(InstrutorResponse::from));
    }

    public InstrutorResponse criar(InstrutorRequest request) {
        validarEmail(request.email(), null);
        Instrutor instrutor = new Instrutor(request.nome().trim(), request.email().trim(), request.telefone(),
                request.especialidade().trim(), request.ativo() == null || request.ativo());
        return InstrutorResponse.from(repository.save(instrutor));
    }

    @Transactional(readOnly = true)
    public InstrutorResponse buscar(Long id) { return InstrutorResponse.from(obter(id)); }

    public InstrutorResponse atualizar(Long id, InstrutorRequest request) {
        Instrutor instrutor = obter(id);
        validarEmail(request.email(), id);
        instrutor.atualizar(request.nome().trim(), request.email().trim(), request.telefone(),
                request.especialidade().trim(), request.ativo() == null || request.ativo());
        return InstrutorResponse.from(repository.save(instrutor));
    }

    public void excluir(Long id) {
        Instrutor instrutor = obter(id);
        if (agendamentos.existsByInstrutorId(id)) {
            throw ApiException.conflito("INSTRUTOR_COM_AGENDAMENTOS",
                    "O instrutor possui agendamentos relacionados.");
        }
        repository.delete(instrutor);
    }

    private Instrutor obter(Long id) {
        return repository.findById(id).orElseThrow(() -> ApiException.naoEncontrado("Instrutor"));
    }

    private void validarEmail(String email, Long id) {
        boolean exists = id == null ? repository.existsByEmailIgnoreCase(email)
                : repository.existsByEmailIgnoreCaseAndIdNot(email, id);
        if (exists) throw ApiException.conflito("EMAIL_DUPLICADO", "Já existe um instrutor com esse e-mail.");
    }
}
