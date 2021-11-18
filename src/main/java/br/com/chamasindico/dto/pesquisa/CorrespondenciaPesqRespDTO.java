package br.com.chamasindico.dto.pesquisa;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorrespondenciaPesqRespDTO {

    private Long id;
    private Long unidade;
    private String bloco;
    private String funcionarioCriacao;
    private String funcionarioEntrega;
    private LocalDate data;
    private LocalDateTime entrega;
    private String remetente;
}
