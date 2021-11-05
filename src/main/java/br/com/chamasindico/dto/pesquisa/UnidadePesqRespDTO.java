package br.com.chamasindico.dto.pesquisa;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnidadePesqRespDTO {

    private Long idCondominio;
    private String condominio;
    private String bloco;
    private Long unidade;
    private Integer metragem;
    private Integer banheiros;
    private Integer quartos;
}
