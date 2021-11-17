package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ComunicadoDTO {

    private Long id;
    private LocalDate data;
    private LocalDate vencimento;
    private String titulo;
    private String corpo;

}
