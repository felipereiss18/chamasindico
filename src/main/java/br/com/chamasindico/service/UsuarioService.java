package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.usuario.UsuarioRepository;
import br.com.chamasindico.repository.model.Perfil;
import br.com.chamasindico.repository.model.QUsuario;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.security.UserPrincipal;
import com.querydsl.core.BooleanBuilder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class UsuarioService
        extends ServiceAbstract<Usuario, UsuarioRepository>
        implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        return UserPrincipal.create(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = repository.findByNome(login)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado com o Login: " + login)
                );

        if(!usuario.getSituacao()) {
            throw new ChamaSindicoException("Usuário " + login + " não se encontra ativo.");
        }
        return UserPrincipal.create(usuario);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        try {

            usuario.setPerfil(perfilService.buscarPorRole("ADMIN"));

            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            return super.salvar(usuario);
        }catch (Exception e){
            throw new ChamaSindicoException("erro.persistir");
        }
    }

    @Override
    public Usuario editar(Usuario usuario) {
        Usuario usuOld = this.buscarPorId(usuario.getId());

        usuOld.setNome(usuario.getNome());

        return super.salvar(usuOld);
    }

    public void trocarSenha(Long id, String atual, String nova) {

        Usuario usuario = super.buscarPorId(id);

        if(usuario.getSenha().equals(passwordEncoder.encode(atual))) {
            usuario.setSenha(passwordEncoder.encode(nova));

            super.salvar(usuario);
        }else {
            throw new ChamaSindicoException("erro.senhaatual");
        }
    }

    public Page<Usuario> pesquisar(String usuario, Boolean situacao, Perfil perfil, Pageable page) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QUsuario qUsuario = QUsuario.usuario;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (usuario != null && !usuario.isEmpty()) {
            booleanBuilder.and(qUsuario.nome.containsIgnoreCase(usuario));
        }
        if (perfil != null && perfil.getId() != null) {
            booleanBuilder.and(qUsuario.perfil.id.eq(perfil.getId()));
        }
        if (situacao != null) {
            booleanBuilder.and(qUsuario.situacao.eq(situacao));
        }

        booleanBuilder.and(qUsuario.id.ne(principal.getId()));

        return repository.findAll(booleanBuilder, page);
    }

    public void alterarSituacao(Long id, boolean situacao) {
        Usuario usuario = this.buscarPorId(id);

        usuario.setSituacao(situacao);

        super.salvar(usuario);
    }
}
