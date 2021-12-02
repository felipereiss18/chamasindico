package br.com.chamasindico.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EstatisticaOcorrenciaRelReqDTO {
    private LocalDate inicio;
    private LocalDate fim;
}
