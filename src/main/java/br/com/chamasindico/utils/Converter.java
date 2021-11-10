package br.com.chamasindico.utils;

import br.com.chamasindico.dto.model.*;
import br.com.chamasindico.dto.pesquisa.AreaComumPesqRespDTO;
import br.com.chamasindico.repository.model.*;

import java.util.Comparator;
import java.util.Optional;
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
                        .id(dto.getId()).bloco(
                                new Bloco(
                                        new BlocoPK(
                                                blocoDTO.getId(),
                                                new Condominio(condominioDTO.getId())),
                                        null
                                )
                        )
                        .build()
                )
                .banheiros(dto.getBanheiros())
                .metragem(dto.getMetragem())
                .quartos(dto.getQuartos())
                .build();
    }

    public static AreaComumPesqRespDTO areaComumToPesqDTO(AreaComum areaComum) {

       return AreaComumPesqRespDTO.builder()
                .id(areaComum.getId())
                .bloco(areaComum.getBloco() != null ? areaComum.getBloco().getId().getId(): "")
                .nome(areaComum.getNome())
                .locacao(areaComum.getLocacao())
                .inicial(areaComum.getInicial())
                .fim(areaComum.getFim())
                .tipoReserva(areaComum.getTipoReserva() != null ? getTipoReserva(areaComum.getTipoReserva()) : "")
                .tipoConfirmacao(
                        areaComum.getTipoConfirmacao() != null ? getTipoConfirmacao(areaComum.getTipoConfirmacao()) : ""
                )
                .diasFuncionamento(areaComum.getDiasFuncionamento()
                        .stream()
                        .map(dias -> dias.getId().getDia())
                        .collect(Collectors.toList())
                )
                .situacao(areaComum.getSituacao())
                .build();
    }

    private static String getTipoConfirmacao(Integer tipo) {
        switch (tipo) {
            case 1:
                return "Alerta Por E-mail";
            case 2:
                return "Requer Confirmação";
            case 3:
                return "Não é necessário";
            default:
                return "";
        }
    }

    private static String getTipoReserva(Integer tipo) {
        switch (tipo) {
            case 1:
                return "Por Dia";
            case 2:
                return "Por Hora";
            case 3:
                return "Apenas Informativo";
            default:
                return "";
        }
    }

    public static AreaComum dtoToAreaComum(AreaComumDTO dto) {

        Condominio condominio = Condominio.builder().id(dto.getCondominio().getId()).build();
        BlocoPK blocoPK = null;
        if (dto.getBloco() != null && dto.getBloco().getId() != null && !dto.getBloco().getId().isEmpty()) {
            blocoPK = BlocoPK.builder().id(dto.getBloco().getId()).condominio(condominio).build();
        }

        AreaComum areaComum = AreaComum.builder()
                .condominio(condominio)
                .bloco(blocoPK != null ? Bloco.builder().id(blocoPK).build() : null)
                .nome(dto.getNome())
                .locacao(dto.isLocacao())
                .inicial(dto.getInicial())
                .fim(dto.getFim())
                .tipoReserva(dto.getTipoReserva())
                .tipoConfirmacao(dto.getTipoConfirmacao())
                .limpeza(dto.getLimpeza())
                .anotacao(dto.getAnotacao())
                .situacao(dto.isSituacao())
                .build();
        areaComum.setDiasFuncionamento(dto.getDiasFuncionamento().stream()
                .map(dia -> new DiaFuncionamento(new DiaFuncionamentoPK(dia, areaComum)))
                .collect(Collectors.toSet())
        );

        return areaComum;
    }

    public static AreaComumDTO areaComumToDto(AreaComum areaComum) {
        return AreaComumDTO.builder()
                .condominio(CondominioDTO.builder()
                        .id(areaComum.getCondominio().getId())
                        .nome(areaComum.getCondominio().getNome())
                        .build()
                )
                .bloco(BlocoDTO.builder().id(areaComum.getBloco() != null ? areaComum.getBloco().getId().getId() : null)
                        .build()
                )
                .nome(areaComum.getNome())
                .locacao(areaComum.getLocacao())
                .inicial(areaComum.getInicial())
                .fim(areaComum.getFim())
                .tipoReserva(areaComum.getTipoReserva())
                .tipoConfirmacao(areaComum.getTipoConfirmacao())
                .limpeza(areaComum.getLimpeza())
                .anotacao(areaComum.getAnotacao())
                .diasFuncionamento(
                        areaComum.getDiasFuncionamento().stream()
                                .map(d -> d.getId().getDia())
                                .collect(Collectors.toList())
                )
                .situacao(areaComum.getSituacao())
                .build();
    }

    public static AluguelDTO aluguelToDTO(Aluguel aluguel){

        Optional<Inquilino> optionalInquilino = aluguel.getInquilinos().stream().findFirst();

        InquilinoDTO inquilinoDTO = null;
        if (optionalInquilino.isPresent()) {
           inquilinoDTO = InquilinoDTO.builder()
                   .cpf(optionalInquilino.get().getCpf())
                   .nome(optionalInquilino.get().getNome())
                   .email(optionalInquilino.get().getEmail())
                   .nascimento(optionalInquilino.get().getNascimento())
                   .telefone(optionalInquilino.get().getTelefone())
                   .usuario(UsuarioDTO.builder()
                           .id(optionalInquilino.get().getUsuario().getId())
                           .nome(optionalInquilino.get().getUsuario().getNome())
                           .situacao(optionalInquilino.get().getUsuario().getSituacao())
                           .build()
                   )
                   .build();
        }

        return AluguelDTO.builder()
                .dataFim(aluguel.getFim())
                .dataInicio(aluguel.getInicio())
                .id(aluguel.getId())
                .inquilino(inquilinoDTO)
                .build();
    }
}
