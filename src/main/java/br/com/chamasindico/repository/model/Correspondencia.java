package br.com.chamasindico.repository.model;

import com.querydsl.core.annotations.QueryInit;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_correspondencia", schema = "chama_sindico")
public class Correspondencia extends EntityAbstract<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

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

    @ManyToOne
    @JoinColumns(value =  {
            @JoinColumn(name = "id_condominio", insertable = false, updatable = false),
            @JoinColumn(name = "id_bloco", insertable = false, updatable = false)
        })
    @QueryInit({"id.condominio"})
    private Bloco bloco;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_criacao")
    private Funcionario funcionarioCriacao;

    @ManyToOne
    @JoinColumn(name = "id_funcionario_entrega")
    private Funcionario funcionarioEntrega;

    @Column(name = "remetente")
    private String remetente;

    @Column(name = "genero")
    private Character genero;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "dt_entrega")
    private LocalDateTime entrega;
}
