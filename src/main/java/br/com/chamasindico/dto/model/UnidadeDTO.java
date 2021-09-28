package br.com.chamasindico.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeDTO {

    private Long id;
    private Integer metragem;
    private Integer quartos;
    private Integer banheiros;
}
