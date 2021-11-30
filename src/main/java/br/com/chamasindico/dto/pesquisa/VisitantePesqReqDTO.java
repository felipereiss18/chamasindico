package br.com.chamasindico.dto.pesquisa;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VisitantePesqReqDTO {

    private String nome;
    private String documento;

}
