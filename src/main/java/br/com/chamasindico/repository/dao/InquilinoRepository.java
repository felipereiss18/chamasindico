package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.Inquilino;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquilinoRepository extends JpaRepository<Inquilino, Long>, QuerydslPredicateExecutor<Inquilino> {

    List<Inquilino> findAllByAluguel_CondominioAndUsuario_Situacao(Condominio condominio, boolean situacao);

    Optional<Inquilino> findByUsuario_IdAndUsuario_Situacao(Long idUsuario, boolean situacao);

    List<Inquilino> findAllByAluguel_UnidadeAndUsuario_Situacao(Unidade unidade, boolean situacao);
}
