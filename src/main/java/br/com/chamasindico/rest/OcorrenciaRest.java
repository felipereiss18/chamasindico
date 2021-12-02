package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.OcorrenciaDTO;
import br.com.chamasindico.dto.model.SituacaoOcorrenciaDTO;
import br.com.chamasindico.dto.pesquisa.OcorrenciaPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.OcorrenciaPesqRespDTO;
import br.com.chamasindico.dto.relatorio.EstatisticaOcorrenciaRelReqDTO;
import br.com.chamasindico.dto.relatorio.EstatisticaOcorrenciaTipoDTO;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaSituacao;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.*;
import br.com.chamasindico.service.InquilinoService;
import br.com.chamasindico.service.OcorrenciaService;
import br.com.chamasindico.service.ProprietarioService;
import br.com.chamasindico.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("ocorrencia")
public class OcorrenciaRest {

    @Autowired
    private OcorrenciaService service;

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private InquilinoService inquilinoService;


    @RoleGlobal
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(
            @RequestBody OcorrenciaPesqReqDTO dto, @PageableDefault Pageable pageable
    ) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Ocorrencia ocorrencia = Ocorrencia.builder()
                .data(dto.getData() != null ? dto.getData().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime() : null)
                .descricao(dto.getDescricao())
                .situacao(dto.getSituacao() != null ? SituacaoOcorrencia.builder().id(dto.getSituacao().getId()).build() : null)
                .build();
        if (principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole())) {
            ocorrencia.setProprietario(Proprietario.builder().id(principal.getId()).build());
        }else if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {
            ocorrencia.setInquilino(Inquilino.builder().id(principal.getId()).build());
        }

        ocorrencia.setCondominio(principal.getCondominio());

        Page<OcorrenciaPesqRespDTO> pages = service.pesquisar(ocorrencia, pageable)
                .map(oco -> OcorrenciaPesqRespDTO.builder()
                        .id(oco.getId())
                        .bloco(oco.getBloco())
                        .unidade(oco.getUnidade())
                        .data(oco.getData())
                        .analise(oco.getDataAnalise())
                        .conclusao(oco.getDataConclusao())
                        .criador(Converter.buscarNomeCriador(oco))
                        .tipo(oco.getTipo())
                        .dono(Converter.verificarCriador(oco, principal))
                        .situacao(SituacaoOcorrenciaDTO.builder()
                                .id(oco.getSituacao().getId())
                                .descricao(oco.getSituacao().getDescricao())
                                .build()
                        )
                        .build()
                );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @GetMapping("{id}")
    @RoleGlobal
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        Ocorrencia ocorrencia = service.buscarPorId(id);

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if ( (!principal.getPerfil().getRole().equals(Roles.SINDICO.getRole())) &&
                (
                (ocorrencia.getInquilino() != null && !ocorrencia.getInquilino().getId().equals(principal.getId())) &&
                (ocorrencia.getInquilino() != null && !ocorrencia.getFuncionario().getId().equals(principal.getId())) &&
                (ocorrencia.getInquilino() != null && !ocorrencia.getProprietario().getId().equals(principal.getId()))
                )
        ) {
            throw new ChamaSindicoException("Ocorrência não pertence ao usuário logado.");
        }

        return ResponseEntity.ok(new ResponseDTO(Converter.ocorrenciaToDTO(ocorrencia)));
    }

    @RoleGlobal
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody OcorrenciaDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Ocorrencia ocorrencia = Ocorrencia.builder()
                .situacao(SituacaoOcorrencia.builder().id(1L).build())
                .data(LocalDateTime.now())
                .tipo(dto.getTipo())
                .descricao(dto.getDescricao())
                .build();

        if (dto.getUnidadeDestinatario() != null) {
            ocorrencia.setUnidadeDestinatario(
                    Unidade.builder()
                    .id(UnidadePK.builder()
                            .id(dto.getUnidadeDestinatario())
                            .bloco(Bloco.builder()
                                    .id(BlocoPK.builder()
                                            .id(dto.getBlocoDestinatario())
                                            .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                            .build())
                                    .build())
                            .build())
                    .build()
            );
        }

        carregarInformacaoUsuario(principal, ocorrencia);

        service.salvar(ocorrencia);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @PutMapping("{id}")
    @RoleGlobal
    public ResponseEntity<ResponseDTO> alterar(@PathVariable("id") Long id, @RequestBody OcorrenciaDTO dto) {
        Ocorrencia ocorrencia = service.buscarPorId(id);

        if(!service.buscarPorId(id).getSituacao().getId().equals(1L)) {
            throw new ChamaSindicoException("Somente é permitido exclusão de ocorrência com situação Aberta");
        }

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        if ( (!principal.getPerfil().getRole().equals(Roles.SINDICO.getRole())) &&
            (
                (ocorrencia.getInquilino() != null && !ocorrencia.getInquilino().getId().equals(principal.getId())) &&
                (ocorrencia.getInquilino() != null && !ocorrencia.getFuncionario().getId().equals(principal.getId())) &&
                (ocorrencia.getInquilino() != null && !ocorrencia.getProprietario().getId().equals(principal.getId()))
            )
        ) {
            throw new ChamaSindicoException("Ocorrência não pertence ao usuário logado.");
        }

        ocorrencia.setDescricao(dto.getDescricao());
        ocorrencia.setTipo(dto.getTipo());
        ocorrencia.setUnidadeDestinatario(Unidade.builder()
                .id(UnidadePK.builder()
                        .id(dto.getUnidadeDestinatario())
                        .bloco(Bloco.builder()
                                .id(BlocoPK.builder()
                                        .id(dto.getBlocoDestinatario())
                                        .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                        .build())
                                .build())
                        .build())
                .build());

        service.editar(ocorrencia);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @PatchMapping("{id}/analisar")
    @RoleSindico
    public ResponseEntity<ResponseDTO> colocarEmAnalise(@PathVariable("id") Long id) {
        Ocorrencia ocorrencia = service.buscarPorId(id);

        ocorrencia.setSituacao(SituacaoOcorrencia.builder().id(2L).build());
        ocorrencia.setDataAnalise(LocalDateTime.now());

        service.editar(ocorrencia);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @PatchMapping("{id}/concluir")
    @RoleSindico
    public ResponseEntity<ResponseDTO> concluir(@PathVariable("id") Long id, @RequestBody String resposta) {
        Ocorrencia ocorrencia = service.buscarPorId(id);

        ocorrencia.setSituacao(SituacaoOcorrencia.builder().id(3L).build());
        ocorrencia.setDataConclusao(LocalDateTime.now());
        ocorrencia.setResposta(resposta);
        service.editar(ocorrencia);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable("id") Long id) {
        if(!service.buscarPorId(id).getSituacao().getId().equals(1L)) {
            throw new ChamaSindicoException("Somente é permitido exclusão de ocorrência com situação Aberta");
        }

        service.excluir(id);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @GetMapping("situacoes")
    public ResponseEntity<ResponseDTO> situacoes() {
        List<SituacaoOcorrenciaDTO> lista = service.buscarSituacoes().stream()
                .map(sit -> SituacaoOcorrenciaDTO.builder().id(sit.getId()).descricao(sit.getDescricao()).build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleGlobal
    @GetMapping("minhas")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarOcorrenciasProprias() {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Ocorrencia ocorrencia = new Ocorrencia();
        List<SituacaoOcorrencia> situacoes =
                Arrays.asList(SituacaoOcorrencia.builder().id(1L).build(), SituacaoOcorrencia.builder().id(2L).build());

        carregarInformacaoUsuario(principal, ocorrencia);

        List<OcorrenciaDTO> lista = service.buscarPorDonoSituacao(ocorrencia, situacoes).stream().map(ocorr ->
                OcorrenciaDTO.builder()
                        .id(ocorr.getId())
                        .situacao(SituacaoOcorrenciaDTO.builder()
                                .id(ocorr.getSituacao().getId())
                                .descricao(ocorr.getSituacao().getDescricao())
                                .build())
                        .data(ocorr.getData())
                        .analise(ocorr.getDataAnalise())
                        .descricao(ocorr.getDescricao())
                        .tipo(ocorr.getTipo())
                    .build()
        ).collect(Collectors.toList());


        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    private void carregarInformacaoUsuario(UserPrincipal principal, Ocorrencia ocorrencia) {
        ocorrencia.setCondominio(principal.getCondominio());

        if (principal.getPerfil().getRole().equals(Roles.SINDICO.getRole()) ||
                principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole())){
            Proprietario proprietario = proprietarioService.buscarComoMorador(principal.getId());
            ocorrencia.setProprietario(proprietario);
            ocorrencia.setUnidade(proprietario.getUnidade().getId().getId());
            ocorrencia.setBloco(proprietario.getUnidade().getId().getBloco().getId().getId());
        } else if (principal.getPerfil().getRole().equals(Roles.FUNCIONARIO.getRole())){
            ocorrencia.setFuncionario(Funcionario.builder().id(principal.getId()).build());
        } else if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {
            Inquilino inquilino = inquilinoService.buscarPorId(principal.getId());
            ocorrencia.setInquilino(inquilino);
            ocorrencia.setUnidade(inquilino.getAluguel().getUnidade().getId().getId());
            ocorrencia.setBloco(inquilino.getAluguel().getUnidade().getId().getBloco().getId().getId());
        }
    }

    @RoleSindico
    @RoleFuncionario
    @GetMapping("aberta-analise")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarOcorrenciaAbertaEmAnalise() {
        List<OcorrenciaDTO> ocorrencias = service.buscarPorSituacoes(
                Arrays.asList(SituacaoOcorrencia.builder().id(1L).build(), SituacaoOcorrencia.builder().id(2L).build())
        ).stream().map(Converter::ocorrenciaToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(ocorrencias));
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @GetMapping("para-mim")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarOcorrenciaParaMim() {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Unidade unidade = verificarUnidade(principal);

        List<OcorrenciaDTO> lista = service.buscarPorDestinatario(unidade,
                Arrays.asList(SituacaoOcorrencia.builder().id(1L).build(), SituacaoOcorrencia.builder().id(2L).build())
        ).stream().map(oco -> OcorrenciaDTO.builder()
                .id(oco.getId())
                .unidadeDestinatario(oco.getUnidadeDestinatario().getId().getId())
                .blocoDestinatario(oco.getUnidadeDestinatario().getId().getBloco().getId().getId())
                .descricao(oco.getDescricao())
                .data(oco.getData())
                .analise(oco.getDataAnalise())
                .tipo(oco.getTipo())
                .situacao(SituacaoOcorrenciaDTO.builder()
                        .id(oco.getSituacao().getId())
                        .descricao(oco.getSituacao().getDescricao())
                        .build())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    private Unidade verificarUnidade(UserPrincipal principal) {
        if (principal.getPerfil().getRole().equals(Roles.SINDICO.getRole()) ||
                principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole())){
            Proprietario proprietario = proprietarioService.buscarComoMorador(principal.getId());
            if (proprietario != null) {
                return proprietario.getUnidade();
            }
        } else if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {
            return inquilinoService.buscarPorId(principal.getId()).getAluguel().getUnidade();
        }

        return null;
    }

    @RoleSindico
    @PostMapping("relatorio/tipo")
    public ResponseEntity<ResponseDTO> gerarRelatorioPorTipo(@RequestBody EstatisticaOcorrenciaRelReqDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<EstatisticaOcorrenciaTipoDTO> lista =
                service.buscarEstatisticaTipo(
                        principal.getCondominio(),
                        dto.getInicio() != null ? dto.getInicio().atStartOfDay() : null,
                        dto.getFim() != null ? dto.getFim().atStartOfDay() : null)
                        .stream().map(oc ->
                            EstatisticaOcorrenciaTipoDTO.builder()
                                    .name(verificarTipo(oc.getTipo()))
                                    .value(oc.getQuantidade())
                                    .build()
                ).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleSindico
    @PostMapping("relatorio/situacao")
    public ResponseEntity<ResponseDTO> gerarRelatorioPorSituacao(@RequestBody EstatisticaOcorrenciaRelReqDTO dto){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<EstatisticaOcorrenciaSituacao> lista =
                service.buscarEstatisticaSituacao(
                        principal.getCondominio(),
                        dto.getInicio() != null ? dto.getInicio().atStartOfDay() : null,
                        dto.getFim() != null ? dto.getFim().atStartOfDay() : null);

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    private String verificarTipo(Integer tipo) {
        switch (tipo) {
            case 1:
                return "Barulho";
            case 2:
                return "Reparo";
            case 3:
                return "Mudança";
            case 4:
                return "Outros";
            default:
                return "";
        }
    }
}
