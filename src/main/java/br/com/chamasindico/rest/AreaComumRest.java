package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.AreaComumDTO;
import br.com.chamasindico.dto.pesquisa.AreaComumPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.AreaComumPesqRespDTO;
import br.com.chamasindico.repository.model.AreaComum;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.AreaComumService;
import br.com.chamasindico.utils.Converter;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("area-comum")
public class AreaComumRest {

    @Autowired
    private AreaComumService service;

    @RoleAdmin
    @RoleSindico
    @PostMapping("pesquisa")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(@RequestBody AreaComumPesqReqDTO dto, @PageableDefault Pageable page) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AreaComum areaComum = ConverterUtil.converterToDTO(dto, AreaComum.class, "diasFuncionamento");

        areaComum.setCondominio(new Condominio());
        if(principal.getCondominio() != null) {
            areaComum.getCondominio().setId(principal.getCondominio());
        } else {
            areaComum.getCondominio().setId(12L);
        }

        Page<AreaComumPesqRespDTO> lista = service.pesquisar(areaComum, page).map(Converter::areaComumToPesqDTO);

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleAdmin
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable Long id){
        AreaComumDTO dto = Converter.areaComumToDto(service.buscarPorId(id));

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleAdmin
    @RoleSindico
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody AreaComumDTO dto) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AreaComum areaComum = Converter.dtoToAreaComum(dto);
        areaComum.setCondominio(new Condominio(12L));
        service.salvar(areaComum);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @RoleSindico
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable Long id, @RequestBody AreaComumDTO dto){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AreaComum areaComum = Converter.dtoToAreaComum(dto);
        areaComum.setId(id);
        areaComum.setCondominio(new Condominio(12L));

        service.editar(areaComum);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @RoleSindico
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @RoleSindico
    @PatchMapping("{id}/situacao")
    public ResponseEntity<ResponseDTO> alterarSituacao(@PathVariable Long id, @RequestBody boolean situacao) {

        service.alterarSituacao(id, situacao);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @GetMapping("locacao")
    public ResponseEntity<ResponseDTO> buscarPermiteLocacao(){
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<AreaComumDTO> lista = service.buscarPorLocacao(principal.getCondominio()).stream()
                .map(Converter::areaComumToDto).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }
}
