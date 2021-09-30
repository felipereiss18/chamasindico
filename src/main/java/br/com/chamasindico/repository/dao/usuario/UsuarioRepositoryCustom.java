package br.com.chamasindico.repository.dao.usuario;

import br.com.chamasindico.repository.model.Perfil;
import br.com.chamasindico.repository.model.Usuario;
import org.springframework.data.domain.Page;

public interface UsuarioRepositoryCustom {
    Page<Usuario> pesquisar(String usuario, String cpf, Perfil perfil);
}
