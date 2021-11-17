package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Comunicado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComunicadoRepository extends JpaRepository<Comunicado, Long>, QuerydslPredicateExecutor<Comunicado> {


    List<Comunicado> findAllByCondominio_IdAndVencimentoGreaterThanEqual(Long condominio, LocalDate Vencimento);
    Optional<Comunicado> findByIdAndCondominio_Id(Long id, Long idCondominio);
}
