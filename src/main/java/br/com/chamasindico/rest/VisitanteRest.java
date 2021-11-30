package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.VisitaDTO;
import br.com.chamasindico.dto.model.VisitanteDTO;
import br.com.chamasindico.dto.pesquisa.VisitantePesqReqDTO;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleFuncionario;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.VisitaService;
import br.com.chamasindico.service.VisitanteService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("visitante")
public class VisitanteRest {

    @Autowired
    private VisitanteService service;

    @Autowired
    private VisitaService visitaService;

    @RoleSindico
    @RoleFuncionario
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody VisitanteDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Visitante visitante = Visitante.builder()
                .id(dto.getDocumento())
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .build();

        service.salvar(visitante);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleFuncionario
    @RoleSindico
    @PutMapping("{documento}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable("documento") String id, @RequestBody Visitante dto) {

        Visitante visitante = service.buscarPorId(id);

        visitante.setTelefone(dto.getTelefone());
        visitante.setNome(dto.getNome());

        service.editar(visitante);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(
            @RequestBody VisitantePesqReqDTO dto, @PageableDefault Pageable pageable
    ) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Visitante visitante = Visitante.builder()
                .id(dto.getDocumento())
                .nome(dto.getNome())
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .build();

        Page<VisitanteDTO> pages = service.pesquisar(visitante, pageable)
                .map(Converter::visitanteToDTO);

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleSindico
    @RoleFuncionario
    @GetMapping("{documento}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("documento") String documento) {
        Visitante visitante = service.buscarPorId(documento);
        VisitanteDTO dto = Converter.visitanteToDTO(visitante);
        dto.setVisitas(visitante.getVisitas().stream()
                .map(visita -> VisitaDTO.builder()
                        .areaComum(visita.isAreaComum())
                        .data(visita.getId().getData())
                        .unidade(visita.getUnidade().getId().getId())
                        .bloco(visita.getUnidade().getId().getBloco().getId().getId())
                        .build())
                .collect(Collectors.toList()));

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleSindico
    @RoleFuncionario
    @PostMapping("{documento}/visitar")
    public ResponseEntity<ResponseDTO> realizarVisita(
            @PathVariable("documento") String documento,
            @RequestBody VisitaDTO dto
    ){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Visita visita = Visita.builder()
                .id(VisitaPK.builder()
                        .data(LocalDateTime.now())
                        .visitante(Visitante.builder()
                                .id(documento)
                                .build())
                        .build())
                .areaComum(dto.isAreaComum())
                .unidade(Unidade.builder()
                        .id(UnidadePK.builder()
                                .id(dto.getUnidade())
                                .bloco(Bloco.builder()
                                        .id(BlocoPK.builder()
                                                .id(dto.getBloco())
                                                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        visitaService.salvar(visita);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleSindico
    @RoleFuncionario
    @DeleteMapping("{documento}/visita/{data}")
    public ResponseEntity<ResponseDTO> excluirVisita(
            @PathVariable("documento") String documento,
            @PathVariable("data") String data
    ) {

        LocalDateTime dateTime = LocalDateTime.parse(data);

        VisitaPK pk = VisitaPK.builder().data(dateTime).visitante(Visitante.builder().id(documento).build()).build();

        visitaService.excluir(pk);

        return ResponseEntity.ok(new ResponseDTO());
    }
}
