package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_dia_funcionamento", schema = "chama_sindico")
public class DiaFuncionamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DiaFuncionamentoPK id;

}
