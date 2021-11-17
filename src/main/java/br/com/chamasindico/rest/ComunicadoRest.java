package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.ComunicadoDTO;
import br.com.chamasindico.dto.pesquisa.ComunicadoPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.ComunicadoPesqRespDTO;
import br.com.chamasindico.repository.model.Comunicado;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.ComunicadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("comunicado")
public class ComunicadoRest {

    @Autowired
    private ComunicadoService service;

    @RoleSindico
    @PostMapping("pesquisar")
    public ResponseEntity<ResponseDTO> pesquisar(
            @RequestBody ComunicadoPesqReqDTO dto,
            @PageableDefault Pageable pageable
            ) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comunicado comunicado = Comunicado.builder()
                .titulo(dto.getTitulo())
                .data(dto.getData())
                .vencimento(dto.getVencimento())
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .build();

        Page<ComunicadoPesqRespDTO> pages = service.pesquisar(comunicado, pageable).map(
                comunic -> ComunicadoPesqRespDTO.builder()
                        .id(comunic.getId())
                        .data(comunic.getData())
                        .vencimento(comunic.getVencimento())
                        .titulo(comunic.getTitulo())
                        .build()
        );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleSindico
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody ComunicadoDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comunicado comunicado = Comunicado.builder()
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .usuario(new Usuario(principal.getId()))
                .data(dto.getData())
                .vencimento(dto.getVencimento())
                .titulo(dto.getTitulo())
                .corpo(dto.getCorpo())
                .build();

        Comunicado obj = service.salvar(comunicado);

        return ResponseEntity.ok(new ResponseDTO(obj.getId()));
    }

    @RoleSindico
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> alterar(
            @PathVariable("id") Long id,
            @RequestBody ComunicadoDTO dto
    ){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comunicado comunicado = Comunicado.builder()
                .id(id)
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .usuario(new Usuario(principal.getId()))
                .data(dto.getData())
                .vencimento(dto.getVencimento())
                .titulo(dto.getTitulo())
                .corpo(dto.getCorpo())
                .build();

        service.editar(comunicado);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @GetMapping("ativos")
    public ResponseEntity<ResponseDTO> buscarAtivos() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ComunicadoPesqRespDTO> lista = service.buscarAtivos(principal.getCondominio()).stream()
                .map(comunicado -> ComunicadoPesqRespDTO.builder()
                        .id(comunicado.getId())
                        .data(comunicado.getData())
                        .vencimento(comunicado.getVencimento())
                        .titulo(comunicado.getTitulo())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleGlobal
    @GetMapping("{id}")
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("id") Long id) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Comunicado comunicado = service.buscarPorId(id, principal.getCondominio());

        ComunicadoDTO dto = ComunicadoDTO.builder()
                .id(comunicado.getId())
                .data(comunicado.getData())
                .vencimento(comunicado.getVencimento())
                .titulo(comunicado.getTitulo())
                .corpo(comunicado.getCorpo())
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleSindico
    @PostMapping("{id}/enviar")
    public ResponseEntity<ResponseDTO> enviarComunicado(@PathVariable("id") Long id) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        service.enviarComunicado(id, principal.getCondominio());

        return ResponseEntity.ok(new ResponseDTO());
    }
}
