package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.model.UnidadePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IUnidadeRepository extends JpaRepository<Unidade, UnidadePK>, QuerydslPredicateExecutor<Unidade> {
}
