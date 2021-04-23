package br.com.chamasindico.repository.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UnidadePK implements Serializable {

    private Long id;

    @Column(name = "id_bloco")
    private String idBloco;

    @Column(name = "id_condominio")
    private Long idCondominio;
}
