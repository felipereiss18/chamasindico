package br.com.chamasindico.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProprietarioDTO {

    private UsuarioDTO usuario;
    private UnidadeDTO unidade;
    private String bloco;
    private CondominioDTO condominio;
    private String nome;
    private String cpf;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nascimento;
    private String email;
    private String telefone;
    private Boolean morador;
    private Boolean sindico;
    private Integer cep;
    private EstadoDTO estado;
    private String cidade;
    private String endereco;
    private String bairro;
    private String numero;
    private String complemento;
}
