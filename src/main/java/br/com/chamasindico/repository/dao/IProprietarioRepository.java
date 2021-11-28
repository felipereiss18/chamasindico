package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Proprietario;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProprietarioRepository extends JpaRepository<Proprietario, Long>, QuerydslPredicateExecutor<Proprietario> {

    Optional<Proprietario> findByUnidadeAndUsuario_Situacao(Unidade unidade, boolean situacao);
    Optional<Proprietario> findBySindicoTrueAndCondominio_Id(Long idCondominio);

    List<Proprietario> findAllByCondominio_Id(Long id);

    Optional<Proprietario> findByIdAndMoradorIsTrue(Long id);
}
