package br.com.chamasindico.dto.relatorio;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EstatisticaOcorrenciaTipoDTO {
    private String name;
    private Long value;
}
