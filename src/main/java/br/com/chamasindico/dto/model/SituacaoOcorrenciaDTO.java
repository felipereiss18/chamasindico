package br.com.chamasindico.dto.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SituacaoOcorrenciaDTO {

    private Long id;
    private String descricao;
}
