package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaFuncionamentoPK implements Serializable {

    @Column(name = "dia", nullable = false)
    private Integer dia;

    @ManyToOne
    @JoinColumn(name = "id_area_comum", nullable = false)
    private AreaComum areaComum;

}
