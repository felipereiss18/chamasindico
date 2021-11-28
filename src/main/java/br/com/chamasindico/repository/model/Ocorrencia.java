package br.com.chamasindico.repository.model;

import com.querydsl.core.annotations.QueryInit;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_ocorrencia", schema = "chama_sindico")
public class Ocorrencia extends EntityAbstract<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_situacao", nullable = false)
    private SituacaoOcorrencia situacao;

    @Column(name = "id_unidade_criacao")
    private Long unidade;

    @Column(name = "id_bloco_criacao")
    private String bloco;

    @Column(name = "id_condominio_criacao")
    private Long condominio;

    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "id_unidade_destinatario", referencedColumnName = "id", nullable = true),
            @JoinColumn(name = "id_bloco_destinatario", referencedColumnName = "id_bloco", nullable = true),
            @JoinColumn(name = "id_condominio_destinatario", referencedColumnName = "id_condominio", nullable = true)
    })
    @QueryInit({"id.*", "id.bloco.id.*"})
    private Unidade unidadeDestinatario;

    @ManyToOne
    @JoinColumn(name = "id_proprietario")
    private Proprietario proprietario;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    private Funcionario funcionario;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Column(name = "tipo", nullable = false)
    private Integer tipo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "resposta")
    private String resposta;

    @Column(name = "dt_analise")
    private LocalDateTime dataAnalise;

    @Column(name = "dt_conclusao")
    private LocalDateTime dataConclusao;

}
