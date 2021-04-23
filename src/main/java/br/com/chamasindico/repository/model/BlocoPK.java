package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class BlocoPK implements Serializable {

    private String id;

    @ManyToOne
    @JoinColumn(name = "id_condominio")
    private Condominio condominio;
}
