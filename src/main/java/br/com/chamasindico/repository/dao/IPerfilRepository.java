package br.com.chamasindico.repository.dao;

import br.com.chamasindico.repository.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPerfilRepository extends JpaRepository<Perfil, Long> {
}
