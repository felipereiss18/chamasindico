package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_condominio", schema = "chama_sindico")
public class Condominio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "cep", nullable = false)
    private Integer cep;

    @Column(name = "endereco", nullable = false)
    private String endereco;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "complemento", nullable = false)
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

}
