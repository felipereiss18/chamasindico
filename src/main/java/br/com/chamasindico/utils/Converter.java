package br.com.chamasindico.utils;

import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.repository.model.Condominio;

public class Converter {

    public static Condominio dtoToCondominio(CondominioDTO dto) {
        Condominio condominio = ConverterUtil.converterToDTO(dto, Condominio.class, "estado", "blocos");



        return condominio;
    }

}
