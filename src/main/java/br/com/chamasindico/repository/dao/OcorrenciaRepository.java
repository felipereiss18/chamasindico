package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaSituacao;
import br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long>, QuerydslPredicateExecutor<Ocorrencia> {

    List<Ocorrencia> findAllByUnidadeAndBlocoAndProprietarioAndSituacaoIn
            (Long unidade, String bloco, Proprietario proprietario, List<SituacaoOcorrencia> situacoes);
    List<Ocorrencia> findAllByUnidadeAndBlocoAndInquilinoAndSituacaoIn
            (Long unidade, String bloco, Inquilino inquilino, List<SituacaoOcorrencia> situacoes);
    List<Ocorrencia> findAllByFuncionarioAndSituacaoIn(Funcionario funcionario, List<SituacaoOcorrencia> situacoes);

    List<Ocorrencia> findAllBySituacaoIn(List<SituacaoOcorrencia> situacoes);
    List<Ocorrencia> findAllByUnidadeDestinatarioAndSituacaoIn(Unidade unidade, List<SituacaoOcorrencia> situacoes);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaTipo(o.tipo, COUNT(o)) " +
            "FROM Ocorrencia o where o.condominio =:condominio GROUP BY o.tipo")
    List<EstatisticaOcorrenciaTipo> findEstatisticaPorTipo(Long condominio);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaTipo(o.tipo, COUNT(o)) " +
            "FROM Ocorrencia o WHERE o.condominio =:condominio and o.data between :inicio AND :fim GROUP BY o.tipo")
    List<EstatisticaOcorrenciaTipo> findEstatisticaPorTipo(
            Long condominio, @Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaSituacao(so.descricao, COUNT(o)) " +
            "FROM Ocorrencia o inner join o.situacao so where o.condominio =:condominio GROUP BY so.descricao")
    List<EstatisticaOcorrenciaSituacao> findEstatisticaPorSituacao(Long condominio);

    @Query("SELECT new br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaSituacao(so.descricao, COUNT(o)) " +
            "FROM Ocorrencia o inner join o.situacao so  WHERE o.condominio =:condominio and o.data between :inicio AND :fim GROUP BY so.descricao")
    List<EstatisticaOcorrenciaSituacao> findEstatisticaPorSituacao(Long condominio, LocalDateTime inicio, LocalDateTime fim);

}
