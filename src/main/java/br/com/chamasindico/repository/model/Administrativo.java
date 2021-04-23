package br.com.chamasindico.repository.model;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_administrativo")
public class Administrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "dt_mandato_inicio", nullable = false)
    private LocalDate mandatoInicio;

    @Column(name = "dt_mandato_fim", nullable = false)
    private LocalDate mandatoFim;

    @ManyToOne
    @JoinColumn(name = "id_sindico", nullable = false)
    private Proprietario sindico;

    @ManyToOne
    @JoinColumn(name = "id_subsindico", nullable = false)
    private Proprietario subsindico;

}
