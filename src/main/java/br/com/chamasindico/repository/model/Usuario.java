package br.com.chamasindico.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

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

    @OneToOne(mappedBy = "usuario")
    private Proprietario proprietario;

    @OneToOne(mappedBy = "usuario")
    private Inquilino inquilino;

    public Usuario(Perfil perfil, String nome, String senha, Boolean situacao, Proprietario proprietario) {
        this.perfil = perfil;
        this.nome = nome;
        this.senha = senha;
        this.situacao = situacao;
        this.proprietario = proprietario;
    }

    public Usuario(Long id, Perfil perfil, String nome, String senha, Boolean situacao, Proprietario proprietario) {
        this.id = id;
        this.perfil = perfil;
        this.nome = nome;
        this.senha = senha;
        this.situacao = situacao;
        this.proprietario = proprietario;
    }

    public Usuario(Long id, Perfil perfil, String nome, String senha, Boolean situacao, Inquilino inquilino) {
        this.id = id;
        this.perfil = perfil;
        this.nome = nome;
        this.senha = senha;
        this.situacao = situacao;
        this.inquilino = inquilino;
    }
}
