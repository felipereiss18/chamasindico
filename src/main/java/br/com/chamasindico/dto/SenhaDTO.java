package br.com.chamasindico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SenhaDTO {

    private String atual;
    private String nova;

}
