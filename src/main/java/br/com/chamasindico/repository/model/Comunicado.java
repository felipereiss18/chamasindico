package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_comunicado", schema = "chama_sindico")
public class Comunicado extends EntityAbstract<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_condominio")
    private Condominio condominio;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "data")
    private LocalDate data;

    @Column(name = "vencimento")
    private LocalDate vencimento;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "corpo")
    private String corpo;
}
