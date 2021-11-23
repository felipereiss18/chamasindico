package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.dto.model.CorrespondenciaDTO;
import br.com.chamasindico.dto.model.FuncionarioDTO;
import br.com.chamasindico.dto.model.UnidadeDTO;
import br.com.chamasindico.dto.pesquisa.CorrespondenciaPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.CorrespondenciaPesqRespDTO;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleFuncionario;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleInquilino;
import br.com.chamasindico.security.annotation.RoleProprietario;
import br.com.chamasindico.service.CorrespondenciaService;
import br.com.chamasindico.service.InquilinoService;
import br.com.chamasindico.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("correspondencia")
public class CorrespondenciaRest {

    @Autowired
    private CorrespondenciaService service;

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private InquilinoService inquilinoService;

    @RoleGlobal
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(
            @RequestBody CorrespondenciaPesqReqDTO dto,
            @PageableDefault Pageable pageable
    ) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {
            Inquilino inquilino = inquilinoService.buscarPorUsuarioAtivo(principal.getId());

            dto.setBloco(inquilino.getAluguel().getUnidade().getId().getBloco().getId().getId());
            dto.setUnidade(inquilino.getAluguel().getUnidade().getId().getId());
        } else if(principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole())) {
            Proprietario proprietario = proprietarioService.buscarPorId(principal.getId());

            dto.setBloco(proprietario.getUnidade().getId().getBloco().getId().getId());
            dto.setUnidade(proprietario.getUnidade().getId().getId());
        }

        Correspondencia correspondencia = Correspondencia.builder()
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .bloco(Bloco.builder().id(
                        BlocoPK.builder()
                                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                .id(dto.getBloco())
                                .build()
                ).build())
                .unidade(Unidade.builder().id(
                        UnidadePK.builder()
                                .id(dto.getUnidade())
                                .bloco(Bloco.builder()
                                        .id(BlocoPK.builder()
                                                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                                .id(dto.getBloco())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .data(dto.getData())
                .entrega(dto.getEntrega())
                .remetente(dto.getRemetente())
                .build();

        Page<CorrespondenciaPesqRespDTO> pages = service.pesquisar(correspondencia, pageable).map(
                corresp -> CorrespondenciaPesqRespDTO.builder()
                        .id(corresp.getId())
                        .unidade(corresp.getUnidade().getId().getId())
                        .bloco(corresp.getBloco().getId().getId())
                        .funcionarioCriacao(corresp.getFuncionarioCriacao().getNome())
                        .funcionarioEntrega(corresp.getFuncionarioEntrega() != null ? corresp.getFuncionarioEntrega().getNome() : null)
                        .data(corresp.getData())
                        .entrega(corresp.getEntrega())
                        .remetente(corresp.getRemetente())
                        .build()
        );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleFuncionario
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody CorrespondenciaDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Correspondencia correspondencia = Correspondencia.builder()
                .unidade(Unidade.builder()
                        .id(UnidadePK.builder()
                                .id(dto.getUnidade().getId())
                                .bloco(Bloco.builder()
                                        .id(BlocoPK.builder()
                                                .id(dto.getBloco())
                                                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .data(dto.getData())
                .remetente(dto.getRemetente())
                .genero(dto.getGenero().charAt(0))
                .funcionarioCriacao(Funcionario.builder().id(principal.getId()).build())
                .build();

        service.salvar(correspondencia);

        return ResponseEntity.ok(new ResponseDTO());
    }


    @RoleFuncionario
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable("id") Long id, @RequestBody CorrespondenciaDTO dto) {
       UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       Correspondencia correspondencia = service.buscarPorId(id);

       if (correspondencia.getEntrega() != null) {
           return ResponseEntity.badRequest().body(new ResponseDTO("Correspondência já entregue!"));
       }

       correspondencia.setUnidade(
               Unidade.builder()
                       .id(UnidadePK.builder()
                               .id(dto.getUnidade().getId())
                               .bloco(Bloco.builder()
                                       .id(BlocoPK.builder()
                                               .id(dto.getBloco())
                                               .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                               .build())
                                       .build())
                               .build())
                       .build());
       correspondencia.setData(dto.getData());
       correspondencia.setRemetente(dto.getRemetente());
       correspondencia.setGenero(dto.getGenero().charAt(0));


       service.salvar(correspondencia);

       return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleFuncionario
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable("id") Long id) {

        if (service.buscarPorId(id).getEntrega() == null ) {
            service.excluir(id);
        } else {
            return ResponseEntity.badRequest().body(new ResponseDTO("Correspondência já entregue."));
        }

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleFuncionario
    @PatchMapping("{id}/entregar")
    public ResponseEntity<ResponseDTO> entregar(@PathVariable("id") Long id) {
       UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Correspondencia correspondencia = service.buscarPorId(id);

        correspondencia.setEntrega(LocalDateTime.now());
        correspondencia.setFuncionarioEntrega(Funcionario.builder().id(principal.getId()).build());

        service.salvar(correspondencia);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("id") Long id) {

        Correspondencia correspondencia = service.buscarPorId(id);

        CorrespondenciaDTO dto = CorrespondenciaDTO.builder()
                .unidade(UnidadeDTO.builder()
                        .id(correspondencia.getUnidade().getId().getId())
                        .build())
                .bloco(correspondencia.getBloco().getId().getId())
                .condominio(CondominioDTO.builder()
                        .id(correspondencia.getCondominio().getId())
                        .nome(correspondencia.getCondominio().getNome())
                        .build())
                .data(correspondencia.getData())
                .entrega(correspondencia.getEntrega())
                .genero(correspondencia.getGenero().toString())
                .remetente(correspondencia.getRemetente())
                .funcionarioCriacao(FuncionarioDTO.builder()
                        .nome(correspondencia.getFuncionarioCriacao().getNome())
                        .build())
                .funcionarioEntrega(correspondencia.getFuncionarioEntrega() != null ?
                        FuncionarioDTO.builder()
                        .nome(correspondencia.getFuncionarioEntrega().getNome())
                        .build() : null)
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleInquilino
    @RoleProprietario
    @GetMapping("ativas")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarAtivas() {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CorrespondenciaPesqRespDTO> lista = new ArrayList<>();

        if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {

            Inquilino inquilino = inquilinoService.buscarPorUsuarioAtivo(principal.getId());

            lista = service.buscarAtivaPorUnidade(inquilino.getAluguel().getUnidade()).stream()
                .map(corresp -> CorrespondenciaPesqRespDTO.builder()
                        .id(corresp.getId())
                        .unidade(corresp.getUnidade().getId().getId())
                        .bloco(corresp.getBloco().getId().getId())
                        .funcionarioCriacao(corresp.getFuncionarioCriacao().getNome())
                        .data(corresp.getData())
                        .remetente(corresp.getRemetente())
                        .build()).collect(Collectors.toList());

        } else if(principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole()) ||
                principal.getPerfil().getRole().equals(Roles.SINDICO.getRole())) {

            Proprietario proprietario = proprietarioService.buscarPorId(principal.getId());

            List<Inquilino> inquilinos = inquilinoService.buscarPorUnidadeUsuarioAtivo(proprietario.getUnidade());

            if (inquilinos == null || inquilinos.isEmpty()) {
                lista = service.buscarAtivaPorUnidade(proprietario.getUnidade()).stream()
                        .map(corresp -> CorrespondenciaPesqRespDTO.builder()
                                .id(corresp.getId())
                                .unidade(corresp.getUnidade().getId().getId())
                                .bloco(corresp.getBloco().getId().getId())
                                .funcionarioCriacao(corresp.getFuncionarioCriacao().getNome())
                                .data(corresp.getData())
                                .remetente(corresp.getRemetente())
                                .build()).collect(Collectors.toList());
            }
        }

        return ResponseEntity.ok(new ResponseDTO(lista));
    }
}
