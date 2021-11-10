package br.com.chamasindico.repository.model;

import com.querydsl.core.annotations.QueryInit;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_unidade", schema = "chama_sindico")
public class Unidade extends EntityAbstract<UnidadePK> implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @QueryInit({"bloco.id", "bloco.id.condominio"})
    private UnidadePK id;

    @Column(name = "metragem")
    private Integer metragem;

    @Column(name = "qtd_quarto")
    private Integer quartos;

    @Column(name = "qtd_banheiro")
    private Integer banheiros;

    @Column(name = "garagem")
    private String garagem;

    @Column(name = "placa")
    private String placa;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "ano_fabricacao")
    private Integer anoFabricacao;

    @Column(name = "ano_modelo")
    private Integer anoModelo;

    public Unidade(UnidadePK id) {
        this.id = id;
    }
}
