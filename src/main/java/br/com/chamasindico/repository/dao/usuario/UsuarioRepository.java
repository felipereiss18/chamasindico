package br.com.chamasindico.repository.dao.usuario;

import br.com.chamasindico.repository.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioRepositoryCustom, QuerydslPredicateExecutor<Usuario> {

    Optional<Usuario> findByNome(String nome);
}
