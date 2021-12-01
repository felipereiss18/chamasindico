package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_dia_funcionamento", schema = "chama_sindico")
public class DiaFuncionamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DiaFuncionamentoPK id;

}
