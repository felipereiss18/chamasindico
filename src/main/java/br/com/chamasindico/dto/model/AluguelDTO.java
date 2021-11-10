package br.com.chamasindico.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AluguelDTO {

    Long id;
    LocalDate dataInicio;
    LocalDate dataFim;
    InquilinoDTO inquilino;
}
