package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, String>, QuerydslPredicateExecutor<Visitante> {

    List<Visitante> findAllByIdContains(String id);
}
