package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_conselheiro")
public class Conselheiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_administrativo", nullable = false)
    private Administrativo administrativo;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_proprietario", nullable = false)
    private Proprietario proprietario;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

}
