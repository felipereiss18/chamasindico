package br.com.chamasindico.repository.model;

import com.querydsl.core.annotations.QueryInit;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_visita", schema = "chama_sindico")
public class Visita extends EntityAbstract<VisitaPK> implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private VisitaPK id;

    @Column(name = "area_comum", nullable = false)
    private boolean areaComum;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_unidade", referencedColumnName = "id"),
            @JoinColumn(name = "id_bloco", referencedColumnName = "id_bloco"),
            @JoinColumn(name = "id_condominio", referencedColumnName = "id_condominio")
    })
    @QueryInit({"id.*", "id.bloco.id.*"})
    private Unidade unidade;

}
