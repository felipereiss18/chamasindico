package br.com.chamasindico.repository.dao.condominio;

import br.com.chamasindico.repository.model.Condominio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CondominioRepositoryCustom {

    Page<Condominio> pesquisar(Condominio condominio, Pageable pageable);
}
