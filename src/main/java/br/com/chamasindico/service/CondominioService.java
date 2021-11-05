package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.IBlocoRepository;
import br.com.chamasindico.repository.dao.condominio.CondominioRepository;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.QCondominio;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CondominioService extends ServiceAbstract<Condominio, CondominioRepository> {

    @Autowired
    private IBlocoRepository blocoRepository;

    @Autowired
    public CondominioService(CondominioRepository repository){
        this.repository = repository;
    }

    @Override
    public Condominio salvar(Condominio condominio) {
        condominio.setSituacao(true);
        Condominio cond = super.salvar(condominio);

        condominio.getBlocos().forEach(b -> b.getId().setCondominio(cond));

        condominio.getBlocos().forEach(b -> {
                b.getUnidades().forEach(u -> {
                    u.getId().setBloco(b);
                });
            }
        );

       blocoRepository.saveAll(condominio.getBlocos());

        return cond;
    }

    @Override
    public Condominio editar(Condominio condominio) {
        Condominio condOld = this.buscarPorId(condominio.getId());

        blocoRepository.deleteAll(condOld.getBlocos());

        condominio.setSituacao(condOld.getSituacao());

        condominio.getBlocos().forEach(b -> b.getId().setCondominio(condominio));

        condominio.getBlocos().forEach(b -> {
                    b.getUnidades().forEach(u -> {
                        u.getId().setBloco(b);
                    });
                }
        );

        blocoRepository.saveAll(condominio.getBlocos());

        return repository.save(condominio);
    }

    public Page<Condominio> pesquisar(Condominio condominio, Pageable page) {
        QCondominio qc = QCondominio.condominio;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (condominio.getNome() != null && !condominio.getNome().equals("")) {
            booleanBuilder.and(qc.nome.contains(condominio.getNome()));
        }

        if (condominio.getCidade() != null && !condominio.getCidade().equals("")) {
            booleanBuilder.and(qc.cidade.contains(condominio.getCidade()));
        }

        if (condominio.getEstado() != null && condominio.getEstado().getId() != null && !condominio.getEstado().getId().equals("")) {
            booleanBuilder.and(qc.estado.id.eq(condominio.getEstado().getId()));
        }

        if(condominio.getSituacao() != null) {
            booleanBuilder.and(qc.situacao.eq(condominio.getSituacao()));
        }

        return repository.findAll(booleanBuilder, page);
    }

    public void alterarSituacao(Long id, boolean situacao) {
        Condominio condominio = this.buscarPorId(id);

        condominio.setSituacao(situacao);

        super.salvar(condominio);
    }

    @Override
    public List<Condominio> listar() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }
}
