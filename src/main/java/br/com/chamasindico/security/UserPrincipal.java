package br.com.chamasindico.security;

import br.com.chamasindico.repository.model.Perfil;
import br.com.chamasindico.repository.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -181326870758836880L;

	private Long id;

	private String username;

	@JsonIgnore
	private String password;

	private Perfil perfil;

	private Long condominio;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id){
		this.id = id;
	}

	public static UserPrincipal create(Usuario usuario) {

		List<GrantedAuthority> authorities = Stream.of(new SimpleGrantedAuthority(usuario.getPerfil().getRole()))
				.collect(Collectors.toList());

		Long condominio = null;
		if (usuario.getInquilino() != null) {
			condominio = usuario.getInquilino()
					.getAluguel()
					.getUnidade().getId()
					.getBloco().getId()
					.getCondominio().getId();

		}else if (usuario.getProprietario() != null) {
			condominio = usuario.getProprietario().getUnidade().getId().getBloco().getId().getCondominio().getId();
		}else if (usuario.getFuncionario() != null) {
			condominio = usuario.getFuncionario().getCondominio().getId();
		}

		return new UserPrincipal(
				usuario.getId(), usuario.getNome(), usuario.getSenha(), usuario.getPerfil(), condominio, authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
