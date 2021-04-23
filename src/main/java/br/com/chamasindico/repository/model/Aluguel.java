package br.com.chamasindico.repository.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_aluguel", schema = "chama_sindico")
public class Aluguel implements Serializable {

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

}
