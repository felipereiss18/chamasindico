package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorrespondenciaPesqReqDTO {

    private Long unidade;
    private String bloco;
    private LocalDate data;
    private LocalDateTime entrega;
    private String remetente;
}
