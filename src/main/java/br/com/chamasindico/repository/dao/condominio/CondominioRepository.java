package br.com.chamasindico.repository.dao.condominio;

import br.com.chamasindico.repository.model.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CondominioRepository extends JpaRepository<Condominio, Long>, CondominioRepositoryCustom, QuerydslPredicateExecutor<Condominio> {

}
