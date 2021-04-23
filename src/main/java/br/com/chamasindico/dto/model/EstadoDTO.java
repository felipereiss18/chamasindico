package br.com.chamasindico.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EstadoDTO {

    private String id;
    private String nome;
    private Integer ibge;
}
