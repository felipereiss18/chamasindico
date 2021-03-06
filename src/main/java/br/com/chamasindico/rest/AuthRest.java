package br.com.chamasindico.rest;

import br.com.chamasindico.dto.LoginDTO;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.security.JwtTokenProvider;
import br.com.chamasindico.security.annotation.RoleGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthRest {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @RoleGlobal
    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto){
        try {
            UsernamePasswordAuthenticationToken user =
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());

            Authentication authentication = authenticationManager.authenticate(user);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccessControlExposeHeaders(List.of("Authorization"));
            headers.setAccessControlAllowHeaders(List.of("Authorization", "X-PINGOTHER", "Origin", "X-Requested-With", "Content-Type", "Accept", "X-Custom-header"));

            headers.set("Authorization", jwt);


            return ResponseEntity.ok().headers(headers).build();
        }catch (UsernameNotFoundException e){
            throw new ChamaSindicoException(e.getMessage());
        }catch (BadCredentialsException e){
            throw new ChamaSindicoException("login.invalido");
        }
    }

}
