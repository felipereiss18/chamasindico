package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.SituacaoOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SituacaoOcorrenciaRepository extends JpaRepository<SituacaoOcorrencia, Long> {
}
