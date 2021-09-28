package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_condominio", schema = "chama_sindico")
public class Condominio extends EntityAbstract<Long> implements Serializable {

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

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "complemento", nullable = false)
    private String complemento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "situacao")
    private Boolean situacao;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @OneToMany(orphanRemoval = true, mappedBy = "id.condominio")
    private Set<Bloco> blocos;

    public Condominio(Long id) {
        this.id = id;
    }
}
