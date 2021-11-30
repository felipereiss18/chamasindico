package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.VisitanteRepository;
import br.com.chamasindico.repository.model.QVisitante;
import br.com.chamasindico.repository.model.Visitante;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitanteService extends ServiceAbstract<Visitante, VisitanteRepository>{

    @Autowired
    public VisitanteService(VisitanteRepository repository) {
        this.repository = repository;
    }

    public Page<Visitante> pesquisar(Visitante visitante, Pageable pageable) {
        QVisitante qVisitante = QVisitante.visitante;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qVisitante.condominio.id.eq(visitante.getCondominio().getId()));

        if(visitante.getId() != null && !visitante.getId().isEmpty()) {
            booleanBuilder.and(qVisitante.id.containsIgnoreCase(visitante.getId()));
        }

        if(visitante.getNome() != null && !visitante.getNome().isEmpty()) {
            booleanBuilder.and(qVisitante.nome.containsIgnoreCase(visitante.getNome()));
        }

        return this.repository.findAll(booleanBuilder, pageable);
    }

    public List<Visitante> containsPorDocumento(String documento) {
        return this.repository.findAllByIdContains(documento);
    }
}
