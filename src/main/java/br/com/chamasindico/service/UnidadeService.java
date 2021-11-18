package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.IUnidadeRepository;
import br.com.chamasindico.repository.model.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeService extends ServiceAbstract<Unidade, IUnidadeRepository>{

    @Autowired
    public UnidadeService(IUnidadeRepository repository) {
        this.repository = repository;
    }

    public Page<Unidade> pesquisar(Unidade unidade, Pageable page) {

        QUnidade qU = QUnidade.unidade;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (unidade.getId().getBloco() != null &&
            unidade.getId().getBloco().getId().getId() != null &&
            !unidade.getId().getBloco().getId().getId().isEmpty()) {
            booleanBuilder.and(qU.id.bloco.id.id.eq(unidade.getId().getBloco().getId().getId()));
        }

        if (unidade.getId().getBloco().getId().getCondominio() != null &&
                unidade.getId().getBloco().getId().getCondominio().getId() != null) {
            booleanBuilder.and(qU.id.bloco.id.condominio.id.eq(unidade.getId().getBloco().getId().getCondominio().getId()));
        }

        if (unidade.getId().getId() != null) {
            booleanBuilder.and(qU.id.id.eq(unidade.getId().getId()));
        }

        return repository.findAll(booleanBuilder, page);
    }

    public List<Unidade> buscarPorCondominioBloco(Long idCondominio, String bloco) {
        List<Unidade> lista = repository.findAllById_Bloco(Bloco.builder()
                .id(BlocoPK.builder()
                        .id(bloco)
                        .condominio(Condominio.builder().id(idCondominio).build())
                        .build())
                .build());

        if (lista == null || lista.isEmpty()) {
            throw new ChamaSindicoException("Não foi encontrada nenhuma unidade.");
        }

        return lista;
    }
}
