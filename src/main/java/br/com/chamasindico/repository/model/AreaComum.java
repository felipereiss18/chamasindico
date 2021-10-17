package br.com.chamasindico.repository.model;

import lombok.*;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "tb_area_comum", schema = "chama_sindico")
public class AreaComum extends EntityAbstract<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumnsOrFormulas(value = {
            @JoinColumnOrFormula(formula = @JoinFormula(value = "id_condominio", referencedColumnName = "id_condominio")),
            @JoinColumnOrFormula(column = @JoinColumn(name = "id_bloco", referencedColumnName = "id"))
    })
    private Bloco bloco;

    @ManyToOne
    @JoinColumn(name = "id_condominio", referencedColumnName = "id")
    private Condominio condominio;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "locacao", nullable = false)
    private Boolean locacao;

    @Column(name = "hr_inicial")
    private LocalTime inicial;

    @Column(name = "hr_final")
    private LocalTime fim;

    @Column(name = "tp_reserva")
    private Integer tipoReserva;

    @Column(name = "tp_confirmacao")
    private Integer tipoConfirmacao;

    @Column(name = "limpeza")
    private String limpeza;

    @Column(name = "anotacao")
    private String anotacao;

    @Column(name = "situacao")
    private Boolean situacao;

    @OneToMany(mappedBy = "id.areaComum",
                cascade = {CascadeType.ALL, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<DiaFuncionamento> diasFuncionamento;

}
