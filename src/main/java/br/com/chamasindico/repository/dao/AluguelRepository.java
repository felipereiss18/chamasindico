package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Aluguel;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long>, QuerydslPredicateExecutor<Aluguel> {

    List<Aluguel> findAllByUnidade(Unidade unidade);

    Optional<Aluguel> findFirstByUnidadeOrderByFimDesc(Unidade unidade);
}
