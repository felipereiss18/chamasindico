package br.com.chamasindico.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CondominioDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private Integer cep;
    private String endereco;
    private String bairro;
    private String complemento;
    private String cidade;
    private EstadoDTO estado;
    private List<String> blocos;
}
