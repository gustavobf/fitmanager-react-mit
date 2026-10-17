package com.gustavofigueiredo.fitmanager.service;

import com.gustavofigueiredo.fitmanager.dto.common.PageResponse;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoRequest;
import com.gustavofigueiredo.fitmanager.dto.plano.PlanoResponse;
import com.gustavofigueiredo.fitmanager.entity.Plano;
import com.gustavofigueiredo.fitmanager.exception.ApiException;
import com.gustavofigueiredo.fitmanager.repository.MatriculaRepository;
import com.gustavofigueiredo.fitmanager.repository.PlanoRepository;

import java.util.Locale;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlanoService {
    private final PlanoRepository repository;
    private final MatriculaRepository matriculas;
    public PlanoService(PlanoRepository repository, MatriculaRepository matriculas) {
        this.repository = repository;
        this.matriculas = matriculas;
    }

    @Transactional(readOnly = true)
    public PageResponse<PlanoResponse> listar(int pagina, int tamanho, Boolean ativo, String nome) {
        Specification<Plano> spec = (root, query, cb) -> cb.conjunction();
        if (ativo != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        if (nome != null && !nome.isBlank()) {
            String filter = "%" + nome.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), filter));
        }
        return PageResponse.from(repository.findAll(spec, PageRequest.of(pagina, tamanho, Sort.by("id").ascending()))
                .map(PlanoResponse::from));
    }

    public PlanoResponse criar(PlanoRequest request) {
        validarNome(request.nome(), null);
        return PlanoResponse.from(repository.save(new Plano(request.nome().trim(), request.descricao(),
                request.duracaoEmDias(), request.preco(), request.ativo() == null || request.ativo())));
    }

    @Transactional(readOnly = true)
    public PlanoResponse buscar(Long id) { return PlanoResponse.from(obter(id)); }

    public PlanoResponse atualizar(Long id, PlanoRequest request) {
        Plano plano = obter(id);
        validarNome(request.nome(), id);
        plano.atualizar(request.nome().trim(), request.descricao(), request.duracaoEmDias(),
                request.preco(), request.ativo() == null || request.ativo());
        return PlanoResponse.from(repository.save(plano));
    }

    public void excluir(Long id) {
        Plano plano = obter(id);
        if (matriculas.existsByPlanoId(id)) {
            throw ApiException.conflito("PLANO_VINCULADO", "O plano está vinculado a matrículas e não pode ser excluído.");
        }
        repository.delete(plano);
    }

    private Plano obter(Long id) {
        return repository.findById(id).orElseThrow(() -> ApiException.naoEncontrado("Plano"));
    }

    private void validarNome(String nome, Long id) {
        boolean exists = id == null ? repository.existsByNomeIgnoreCase(nome.trim())
                : repository.existsByNomeIgnoreCaseAndIdNot(nome.trim(), id);
        if (exists) {
            throw ApiException.conflito("NOME_DUPLICADO", "Já existe um plano com o mesmo nome.");
        }
    }
}
