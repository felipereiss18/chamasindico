package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.service.CondominioService;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("condominios")
public class CondominioRest {

    @Autowired
    private CondominioService service;


    @RoleAdmin
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody CondominioDTO dto){

        service.salvar(ConverterUtil.converterToDTO(dto, Condominio.class));

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable Long id, @RequestBody CondominioDTO dto){

        Condominio condominio = ConverterUtil.converterToDTO(dto, Condominio.class);
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
                .map(condominio -> ConverterUtil.converterToDTO(condominio, CondominioDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable String id){
        CondominioDTO dto = ConverterUtil.converterToDTO(service.buscarPorId(id), CondominioDTO.class);

        return ResponseEntity.ok(new ResponseDTO(dto));
    }
}
