package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Bloco;
import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.model.UnidadePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUnidadeRepository extends JpaRepository<Unidade, UnidadePK>, QuerydslPredicateExecutor<Unidade> {

    List<Unidade> findAllById_Bloco(Bloco bloco);
}
