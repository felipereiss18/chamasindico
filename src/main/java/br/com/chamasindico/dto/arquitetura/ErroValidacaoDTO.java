package br.com.chamasindico.dto.arquitetura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroValidacaoDTO {

    private String campo;

    private String descricaoErro;
}
