package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaComumDTO {

    private CondominioDTO condominio;
    private BlocoDTO bloco;
    private String nome;
    private boolean locacao;
    private LocalTime inicial;
    private LocalTime fim;
    private Integer tipoReserva;
    private Integer tipoConfirmacao;
    private String limpeza;
    private String anotacao;
    private boolean situacao;
    private List<Integer> diasFuncionamento;
}
