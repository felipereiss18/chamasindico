package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuncionarioPesqResp {

    private Long id;
    private String nome;
    private String usuario;
    private String email;
    private String telefone;
    private LocalDate nascimento;
    private boolean situacao;

}
