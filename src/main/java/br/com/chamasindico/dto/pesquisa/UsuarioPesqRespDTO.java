package br.com.chamasindico.dto.pesquisa;

import br.com.chamasindico.dto.model.PerfilDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioPesqRespDTO {

    private Long id;
    private String usuario;
    private PerfilDTO perfil;
    private boolean situacao;
}
