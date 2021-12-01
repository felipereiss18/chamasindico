package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AgendaRespDTO {

    private Long id;
    private Long unidade;
    private String bloco;
    private LocalDate data;
    private String areaComum;
    private LocalTime inicio;
    private LocalTime termino;
    private boolean dono;
    private boolean confirmado;
    private boolean necessariaConfirmacao;
}
