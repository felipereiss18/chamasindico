package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Bloco;
import br.com.chamasindico.repository.model.BlocoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBlocoRepository extends JpaRepository<Bloco, BlocoPK> {
}
