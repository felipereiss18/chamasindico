package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OcorrenciaDTO {

    private Long id;
    private SituacaoOcorrenciaDTO situacao;
    private Long unidadeCriacao;
    private String blocoCriacao;
    private Long unidadeDestinatario;
    private String blocoDestinatario;
    private String criador;
    private LocalDateTime data;
    private Integer tipo;
    private String descricao;
    private String resposta;
    private LocalDateTime analise;
    private LocalDateTime conclusao;
}
