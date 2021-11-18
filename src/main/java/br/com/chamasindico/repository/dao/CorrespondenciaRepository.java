package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Correspondencia;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorrespondenciaRepository extends JpaRepository<Correspondencia, Long>, QuerydslPredicateExecutor<Correspondencia> {

    List<Correspondencia> findAllByUnidadeAndEntregaIsNull(Unidade unidade);
}
