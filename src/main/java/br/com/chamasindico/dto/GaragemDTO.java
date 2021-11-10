package br.com.chamasindico.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GaragemDTO {

    private String numero;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anoFabricacao;
    private Integer anoModelo;

}
