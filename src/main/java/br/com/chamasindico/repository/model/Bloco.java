package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_bloco", schema = "chama_sindico")
public class Bloco implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private BlocoPK id;

    @OneToMany(mappedBy = "id.bloco", cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<Unidade> unidades;

}
