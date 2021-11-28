package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.SituacaoOcorrenciaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OcorrenciaPesqReqDTO {

    private LocalDate data;
    private Integer tipo;
    private SituacaoOcorrenciaDTO situacao;
    private String descricao;
}
