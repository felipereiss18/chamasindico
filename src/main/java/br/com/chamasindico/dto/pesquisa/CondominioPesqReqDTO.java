package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.EstadoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CondominioPesqReqDTO {

    private String nome;
    private EstadoDTO estado;
    private String cidade;
    private Boolean situacao;
}
