package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.SituacaoOcorrenciaDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OcorrenciaPesqRespDTO {

    private Long id;
    private LocalDateTime data;
    private LocalDateTime analise;
    private LocalDateTime conclusao;
    private SituacaoOcorrenciaDTO situacao;
    private String criador;
    private Long unidade;
    private String bloco;
    private Integer tipo;
    private boolean dono;

}
