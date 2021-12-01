package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AgendaDTO {
    private Long id;
    private Long unidade;
    private String bloco;
    private LocalDate data;
    private AreaComumDTO areaComum;
    private LocalTime inicio;
    private LocalTime termino;
    private boolean confirmacao;
    private String observacao;
}
