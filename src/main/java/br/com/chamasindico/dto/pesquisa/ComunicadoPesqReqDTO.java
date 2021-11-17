package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComunicadoPesqReqDTO {

    private String titulo;
    private LocalDate data;
    private LocalDate vencimento;

}
