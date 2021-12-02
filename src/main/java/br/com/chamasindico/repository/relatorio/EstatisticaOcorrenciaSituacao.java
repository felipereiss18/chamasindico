package br.com.chamasindico.repository.relatorio;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EstatisticaOcorrenciaSituacao {

    private String name;
    private Long value;
}
