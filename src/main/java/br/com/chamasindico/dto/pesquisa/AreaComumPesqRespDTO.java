package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaComumPesqRespDTO {

    private Long id;
    private String bloco;
    private String nome;
    private boolean locacao;
    private String tipoReserva;
    private String tipoConfirmacao;
    private List<Integer> diasFuncionamento;
    private LocalTime inicial;
    private LocalTime fim;
    private boolean situacao;

}
