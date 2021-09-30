package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.PerfilDTO;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.PerfilService;
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
@RequestMapping("perfil")
public class PerfilRest {

    @Autowired
    private PerfilService service;

    @RoleAdmin
    @RoleSindico
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscar(){
        List<PerfilDTO> list = service.listar()
                .stream()
                .map(perfil -> ConverterUtil.converterToDTO(perfil, PerfilDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @RoleSindico
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable Long id){
        PerfilDTO perfilDTO = ConverterUtil.converterToDTO(service.buscarPorId(id), PerfilDTO.class);
        return ResponseEntity.ok(new ResponseDTO(perfilDTO));
    }

    @RoleGlobal
    @GetMapping("role/{role}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorRole(@PathVariable String role) {
        PerfilDTO dto = ConverterUtil.converterToDTO(service.buscarPorRole(role), PerfilDTO.class);

        return ResponseEntity.ok(new ResponseDTO(dto));
    }
}
