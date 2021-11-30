package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_visitante", schema = "chama_sindico")
public class Visitante extends EntityAbstract<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_condominio", nullable = false)
    private Condominio condominio;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @OneToMany(mappedBy = "id.visitante",
            cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Visita> visitas;

}
