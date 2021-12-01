package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.AreaComum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaComumRepository extends JpaRepository<AreaComum, Long>, QuerydslPredicateExecutor<AreaComum> {

    List<AreaComum> findAllByCondominio_IdAndLocacaoIsTrueAndSituacaoIsTrue(Long idCondominio);
}
