package br.com.chamasindico.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO implements Serializable {

    private Long id;

    @Email(message = "erro.emailinvalido")
    @NotBlank(message = "erro.naoinformado:Login")
    private String login;

    @NotBlank(message = "erro.naoinformado:Senha")
    private String senha;

    private String role;

    private String perfil;
}