package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VisitaDTO {

    private VisitanteDTO visitante;
    private LocalDateTime data;
    private boolean areaComum;
    private Long unidade;
    private String bloco;

}
