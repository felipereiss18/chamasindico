package br.com.chamasindico.repository.dao.usuario;

import br.com.chamasindico.repository.model.Perfil;
import br.com.chamasindico.repository.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;

public class UsuarioRepositoryImpl implements UsuarioRepositoryCustom{

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Usuario> pesquisar(String usuario, String cpf, Perfil perfil) {

        /*QProprietario qProprietario = QProprietario.proprietario;
        QInquilino qInquilino = QInquilino.inquilino;
        QUsuario qUsuario = QUsuario.usuario;
Ø
        JPAQueryBase from = query.from(qUsuario);
        if(cpf != null && !cpf.isEmpty()) {
            from.leftJoin(qProprietario).on(qProprietario.cpf.eq(cpf))
                    .leftJoin(qInquilino).on(qInquilino.cpf.eq(cpf));
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(usuario != null && !usuario.isEmpty()) {
            booleanBuilder.and(qUsuario.nome.containsIgnoreCase(usuario));
        }

        if (perfil != null && perfil.getId() != null) {
            booleanBuilder.and(qUsuario.perfil.id.eq(perfil.getId()));
        }

        from.where(booleanBuilder);*/

        return null;

    }
}
