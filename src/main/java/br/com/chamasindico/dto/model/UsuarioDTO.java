package br.com.chamasindico.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private PerfilDTO perfil;
    private String nome;
    private String senha;
    private boolean situacao;
}
