package br.com.chamasindico.repository.dao.usuario;

import br.com.chamasindico.repository.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

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


}
