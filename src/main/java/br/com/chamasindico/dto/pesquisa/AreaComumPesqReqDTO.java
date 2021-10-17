package br.com.chamasindico.dto.pesquisa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AreaComumPesqReqDTO {

    private String nome;
    private Boolean locacao;
    private Integer tipoReserva;
    private Integer tipoConfirmacao;
    private Boolean situacao;
}
