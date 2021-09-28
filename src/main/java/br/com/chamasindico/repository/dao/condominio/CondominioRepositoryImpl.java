package br.com.chamasindico.repository.dao.condominio;

import br.com.chamasindico.repository.model.Condominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

public class CondominioRepositoryImpl implements CondominioRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    public Page<Condominio> pesquisar(Condominio condominio, Pageable pageable) {
        return null;
    }
}
