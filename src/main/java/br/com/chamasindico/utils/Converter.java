package br.com.chamasindico.utils;

import br.com.chamasindico.dto.model.BlocoDTO;
import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.dto.model.EstadoDTO;
import br.com.chamasindico.dto.model.UnidadeDTO;
import br.com.chamasindico.repository.model.*;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Converter {

    public static Condominio dtoToCondominio(CondominioDTO dto) {
        Condominio condominio = ConverterUtil.converterToDTO(dto, Condominio.class, "estado", "blocos");

        condominio.setEstado(new Estado(dto.getEstado().getId(), null, null));
        condominio.setBlocos(dto.getBlocos().stream().map(blocoDTO -> new Bloco(
                new BlocoPK(blocoDTO.getId(), condominio),
                blocoDTO.getUnidades().stream()
                        .map(uni -> dtoToUnidade(uni, blocoDTO, dto)).collect(Collectors.toSet())
        )).collect(Collectors.toSet()));

        return condominio;
    }

    public static CondominioDTO condominioToDto(Condominio condominio){
        CondominioDTO dto = ConverterUtil.converterToDTO(condominio, CondominioDTO.class);
        dto.setEstado(ConverterUtil.converterToDTO(condominio.getEstado(), EstadoDTO.class));
        dto.setBlocos(condominio.getBlocos().stream().map(bloco -> BlocoDTO.builder()
                .id(bloco.getId().getId())
                .unidades(bloco.getUnidades().stream().map(uni -> UnidadeDTO.builder()
                        .id(uni.getId().getId())
                        .banheiros(uni.getBanheiros())
                        .quartos(uni.getQuartos())
                        .metragem(uni.getMetragem())
                        .build()
                    ).collect(Collectors.toList()))
                .build()
        ).collect(Collectors.toList()));

        dto.getBlocos().forEach(b -> {
            b.setUnidades(
                    b.getUnidades().stream().sorted(
                            Comparator.comparing(UnidadeDTO::getId)).collect(Collectors.toList()
                    )
            );
        });

        return dto;
    }

    public static Unidade dtoToUnidade(UnidadeDTO dto, BlocoDTO blocoDTO, CondominioDTO condominioDTO){
        return Unidade
                .builder()
                .id(UnidadePK.builder()
                        .id(dto.getId()).bloco(new Bloco(new BlocoPK(blocoDTO.getId(), new Condominio(condominioDTO.getId())), null)).build()
                )
                .banheiros(dto.getBanheiros())
                .metragem(dto.getMetragem())
                .quartos(dto.getQuartos())
                .build();
    }
}
