package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.EstadoDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CondominioPesqRespDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String cidade;
    private EstadoDTO estado;
    private boolean situacao;

}
