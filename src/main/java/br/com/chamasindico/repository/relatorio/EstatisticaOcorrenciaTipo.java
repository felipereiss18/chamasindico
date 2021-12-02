package br.com.chamasindico.repository.relatorio;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EstatisticaOcorrenciaTipo {
    private Integer tipo;
    private Long quantidade;
}
