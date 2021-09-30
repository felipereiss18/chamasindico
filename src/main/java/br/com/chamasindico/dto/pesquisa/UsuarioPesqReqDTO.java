package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.PerfilDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPesqReqDTO {

    private String usuario;
    private Boolean situacao;
    private PerfilDTO perfil;
}
