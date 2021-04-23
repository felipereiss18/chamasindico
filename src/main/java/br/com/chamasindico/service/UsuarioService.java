package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.IUsuarioRepository;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.security.UserPrincipal;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@NoArgsConstructor
public class UsuarioService
        extends ServiceAbstract<Usuario, IUsuarioRepository>
        implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(IUsuarioRepository repository){
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
        return UserPrincipal.create(usuario);
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        try {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

            return super.salvar(usuario);
        }catch (Exception e){
            throw new ChamaSindicoException("erro.persistir");
        }
    }
}
