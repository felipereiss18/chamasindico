package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComunicadoPesqRespDTO {

    private Long id;
    private String titulo;
    private LocalDate data;
    private LocalDate vencimento;

}
