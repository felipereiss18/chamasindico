package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.dto.model.EstadoDTO;
import br.com.chamasindico.dto.pesquisa.CondominioPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.CondominioPesqRespDTO;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.Estado;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.service.CondominioService;
import br.com.chamasindico.utils.Converter;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("condominio")
public class CondominioRest {

    @Autowired
    private CondominioService service;


    @RoleAdmin
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody CondominioDTO dto){

        service.salvar(Converter.dtoToCondominio(dto));

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable Long id, @RequestBody CondominioDTO dto){

        Condominio condominio = Converter.dtoToCondominio(dto);
        condominio.setId(id);

        service.editar(condominio);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscar(){
        List<CondominioDTO> list = service.listar()
                .stream()
                .map(Converter::condominioToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable Long id){
        CondominioDTO dto = Converter.condominioToDto(service.buscarPorId(id));

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleAdmin
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(@RequestBody CondominioPesqReqDTO dto, @PageableDefault Pageable page) {
        Condominio condominio = new Condominio();
        condominio.setNome(dto.getNome());
        condominio.setCidade(dto.getCidade());
        condominio.setEstado(ConverterUtil.converterToDTO(dto.getEstado(), Estado.class));
        condominio.setSituacao(dto.getSituacao());

        Page<CondominioPesqRespDTO> pages = service.pesquisar(condominio, page).map(cond ->
                CondominioPesqRespDTO.builder()
                    .id(cond.getId())
                    .nome(cond.getNome())
                    .cnpj(cond.getCnpj())
                    .cidade(cond.getCidade())
                    .estado(ConverterUtil.converterToDTO(cond.getEstado(), EstadoDTO.class))
                    .situacao(cond.getSituacao())
                    .build()
        );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleAdmin
    @PatchMapping("{id}/situacao")
    public ResponseEntity<ResponseDTO> alterarSituacao(@PathVariable Long id, @RequestBody boolean situacao) {

        service.alterarSituacao(id, situacao);

        return ResponseEntity.ok(new ResponseDTO());
    }
}
