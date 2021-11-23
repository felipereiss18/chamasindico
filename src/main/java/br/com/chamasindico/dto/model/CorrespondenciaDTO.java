package br.com.chamasindico.dto.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CorrespondenciaDTO {

    private UnidadeDTO unidade;
    private String bloco;
    private CondominioDTO condominio;
    private String remetente;
    private LocalDate data;
    private String genero;
    private FuncionarioDTO funcionarioCriacao;
    private FuncionarioDTO funcionarioEntrega;
    private LocalDateTime entrega;
}
