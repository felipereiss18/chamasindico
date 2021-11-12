package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, QuerydslPredicateExecutor<Funcionario> {
}
