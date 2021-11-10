package br.com.chamasindico.dto.pesquisa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadePesqReqDTO {

    private Long idCondominio;
    private String idBloco;
    private Long unidade;

}
