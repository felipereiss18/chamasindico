package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadePK implements Serializable {

    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
            @JoinColumn(name = "id_condominio"),
            @JoinColumn(name = "id_bloco")
    })
    private Bloco bloco;
}
