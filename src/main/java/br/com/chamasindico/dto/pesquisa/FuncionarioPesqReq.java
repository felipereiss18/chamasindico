package br.com.chamasindico.dto.pesquisa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioPesqReq {

    private String nome;
    private String usuario;
}
