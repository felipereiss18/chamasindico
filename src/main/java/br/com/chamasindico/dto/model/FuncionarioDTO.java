package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuncionarioDTO {

    private UsuarioDTO usuario;
    private CondominioDTO condominio;
    private String nome;
    private String cpf;
    private LocalDate nascimento;
    private String email;
    private String telefone;

}
