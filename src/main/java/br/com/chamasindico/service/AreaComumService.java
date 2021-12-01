package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.AreaComumRepository;
import br.com.chamasindico.repository.model.AreaComum;
import br.com.chamasindico.repository.model.QAreaComum;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaComumService extends ServiceAbstract<AreaComum, AreaComumRepository> {

    @Autowired
    public AreaComumService(AreaComumRepository repository){
        this.repository = repository;
    }

    public Page<AreaComum> pesquisar(AreaComum areaComum, Pageable page) {

        QAreaComum qAreaComum = QAreaComum.areaComum;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qAreaComum.condominio.id.eq(areaComum.getCondominio().getId()));

        if (areaComum.getNome() != null && !areaComum.getNome().isEmpty()) {
            booleanBuilder.and(qAreaComum.nome.containsIgnoreCase(areaComum.getNome()));
        }
        if (areaComum.getLocacao() != null) {
            booleanBuilder.and(qAreaComum.locacao.eq(areaComum.getLocacao()));
        }
        if (areaComum.getTipoReserva() != null) {
           booleanBuilder.and(qAreaComum.tipoReserva.eq(areaComum.getTipoReserva()));
        }
        if (areaComum.getTipoConfirmacao() != null) {
            booleanBuilder.and(qAreaComum.tipoConfirmacao.eq(areaComum.getTipoConfirmacao()));
        }
        if (areaComum.getSituacao() != null) {
            booleanBuilder.and(qAreaComum.situacao.eq(areaComum.getSituacao()));
        }

        return repository.findAll(booleanBuilder, page);
    }

    public void alterarSituacao(Long id, boolean situacao) {
        AreaComum areaComum = this.buscarPorId(id);

        areaComum.setSituacao(situacao);

        super.salvar(areaComum);
    }

    public List<AreaComum> buscarPorLocacao(Long condominio) {
        return repository.findAllByCondominio_IdAndLocacaoIsTrueAndSituacaoIsTrue(condominio);
    }
}
