package br.com.chamasindico.repository.relatorio;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EstatisticaCorrespondenciaTipo {

    private String name;
    private List<EstatisticaCorrespondencia> series;
}
