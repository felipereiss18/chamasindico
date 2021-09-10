package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.EstadoDTO;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.service.EstadoService;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("estados")
public class EstadoRest {

    @Autowired
    private EstadoService service;

    @RoleGlobal
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscar(){
        List<EstadoDTO> list = service.listar()
                .stream()
                .map(estado -> ConverterUtil.converterToDTO(estado, EstadoDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleGlobal
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable String id){
        EstadoDTO estadoDTO = ConverterUtil.converterToDTO(service.buscarPorId(id), EstadoDTO.class);

        return ResponseEntity.ok(new ResponseDTO(estadoDTO));
    }
}
