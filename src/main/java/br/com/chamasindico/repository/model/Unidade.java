package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_unidade", schema = "chama_sindico")
public class Unidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UnidadePK id;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_bloco", insertable = false, updatable = false),
            @JoinColumn(name = "id_condominio", insertable = false, updatable = false)
    })
    private Bloco bloco;

    @Column(name = "metragem")
    private Integer metragem;

    @Column(name = "qtd_quarto")
    private Integer qtdQuarto;

    @Column(name = "qtd_banheiro")
    private Integer qtdBanheiro;

}
