package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Agenda;
import br.com.chamasindico.repository.model.AreaComum;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long>, QuerydslPredicateExecutor<Agenda> {

    List<Agenda> findAllByDataAndCondominio(LocalDate data, Condominio condominio);
    Optional<Agenda> findByDataAndAreaComum(LocalDate data, AreaComum areaComum);
    Optional<Agenda> findByDataAndInicioAndTerminoAndAreaComum(
            LocalDate data, LocalTime inicio, LocalTime termino, AreaComum areaComum);
    List<Agenda> findAllByConfirmacaoIsFalseAndUnidadeAndDataGreaterThanEqualAndAreaComum_TipoConfirmacao
            (Unidade unidade, LocalDate data, Integer tipo);
}
