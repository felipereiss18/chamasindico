package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_usuario", schema = "chama_sindico")
public class Usuario extends EntityAbstract<Long> implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "situacao")
    private Boolean situacao;

    @OneToMany(mappedBy = "usuario")
    private Set<Proprietario> proprietarios;

    @OneToMany(mappedBy = "usuario")
    private Set<Inquilino> inquilinos;

}
