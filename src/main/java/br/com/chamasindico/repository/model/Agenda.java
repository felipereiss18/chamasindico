package br.com.chamasindico.repository.model;

import com.querydsl.core.annotations.QueryInit;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_agenda", schema = "chama_sindico")
public class Agenda extends EntityAbstract<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_area_comum", nullable = false)
    private AreaComum areaComum;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_unidade", referencedColumnName = "id"),
            @JoinColumn(name = "id_bloco", referencedColumnName = "id_bloco"),
            @JoinColumn(name = "id_condominio", referencedColumnName = "id_condominio")
    })
    @QueryInit({"id.*", "id.bloco.id.*"})
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "id_condominio", insertable = false, updatable = false)
    private Condominio condominio;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hr_inicio", nullable = false)
    private LocalTime inicio;

    @Column(name = "hr_termino", nullable = false)
    private LocalTime termino;

    @Column(name = "confirmacao")
    private boolean confirmacao;

    @Column(name = "observacao")
    private String observacao;

}
