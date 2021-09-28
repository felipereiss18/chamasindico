package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlocoPK implements Serializable {

    private String id;

    @ManyToOne
    @JoinColumn(name = "id_condominio")
    private Condominio condominio;
}
