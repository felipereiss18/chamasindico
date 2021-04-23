package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_area_comum", schema = "chama_sindico")
public class AreaComum implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_bloco"),
            @JoinColumn(name = "id_condominio")
    })
    private Bloco bloco;

    @ManyToOne
    @JoinColumn(name = "id_condominio", insertable = false, updatable = false)
    private Condominio condominio;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "locacao", nullable = false)
    private Integer locacao;

}
