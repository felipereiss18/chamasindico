package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_bloco", schema = "chama_sindico")
public class Bloco implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private BlocoPK id;

}
