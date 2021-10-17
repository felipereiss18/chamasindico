package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
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
