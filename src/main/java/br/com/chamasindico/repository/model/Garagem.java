package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_garagem", schema = "chama_sindico")
public class Garagem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_unidade", referencedColumnName = "id"),
            @JoinColumn(name = "id_bloco", referencedColumnName = "id_bloco"),
            @JoinColumn(name = "id_condominio", referencedColumnName = "id_condominio")
    })
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "id_condominio", insertable = false, updatable = false)
    private Condominio condominio;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "numero", nullable = false)
    private String numero;

}
