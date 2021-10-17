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
public class BlocoDTO {

    private String id;
    private List<UnidadeDTO> unidades;

    public BlocoDTO(String id) {
        this.id = id;
    }
}
