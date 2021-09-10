package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoRepository extends JpaRepository<Estado, String> {
}
