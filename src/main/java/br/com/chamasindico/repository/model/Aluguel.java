package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_aluguel", schema = "chama_sindico")
public class Aluguel extends EntityAbstract<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_unidade", referencedColumnName = "id"),
            @JoinColumn(name = "id_bloco", referencedColumnName = "id_bloco"),
            @JoinColumn(name = "id_condominio", referencedColumnName = "id_condominio")
        }
    )
    private Unidade unidade;

    @Column(name = "dt_inicio", nullable = false)
    private LocalDate inicio;

    @Column(name = "dt_fim", nullable = false)
    private LocalDate fim;

    @OneToMany(mappedBy = "aluguel", orphanRemoval = true,
            cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<Inquilino> inquilinos;
}
