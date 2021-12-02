package br.com.chamasindico.repository.relatorio;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EstatisticaCorrespondencia {

    private LocalDate name;
    private Long value;


    public EstatisticaCorrespondencia(LocalDateTime name, Long value) {
        this.name = name.toLocalDate();
        this.value = value;
    }
}
