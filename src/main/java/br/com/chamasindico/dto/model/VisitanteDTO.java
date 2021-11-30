package br.com.chamasindico.dto.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VisitanteDTO {

    private String documento;
    private CondominioDTO condominio;
    private String nome;
    private String telefone;
    private List<VisitaDTO> visitas;
}
