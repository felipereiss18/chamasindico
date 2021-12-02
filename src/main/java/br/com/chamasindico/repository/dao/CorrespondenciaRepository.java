package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Correspondencia;
import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.relatorio.EstatisticaCorrespondencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CorrespondenciaRepository extends JpaRepository<Correspondencia, Long>, QuerydslPredicateExecutor<Correspondencia> {

    List<Correspondencia> findAllByUnidadeAndEntregaIsNull(Unidade unidade);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaCorrespondencia(c.data, count(c)) " +
            "FROM Correspondencia c WHERE c.condominio.id =:condominio GROUP BY c.data")
    List<EstatisticaCorrespondencia> findEstatisticaData(Long condominio);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaCorrespondencia(c.data, count(c)) " +
            "FROM Correspondencia c WHERE c.condominio.id =:condominio and c.data between :inicio AND :fim " +
            "GROUP BY c.data")
    List<EstatisticaCorrespondencia> findEstatisticaData(Long condominio, LocalDate inicio, LocalDate fim);
}
