package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Visita;
import br.com.chamasindico.repository.model.VisitaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, VisitaPK>, QuerydslPredicateExecutor<Visita> {
}
