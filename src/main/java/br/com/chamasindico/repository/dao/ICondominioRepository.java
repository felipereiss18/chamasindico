package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Condominio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICondominioRepository extends JpaRepository<Condominio, Long> {
}
