package br.com.chamasindico.rest;

import br.com.chamasindico.dto.LoginDTO;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("auth")
public class AuthRest {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO dto){
        try {
            UsernamePasswordAuthenticationToken user =
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());

            Authentication authentication = authenticationManager.authenticate(user);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);

            return ResponseEntity.ok(jwt);
        }catch (UsernameNotFoundException e){
            throw new ChamaSindicoException(e.getMessage());
        }catch (BadCredentialsException e){
            throw new ChamaSindicoException("login.invalido");
        }
    }
}
