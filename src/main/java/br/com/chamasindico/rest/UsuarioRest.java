package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.UsuarioDTO;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.service.UsuarioService;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("usuarios")
public class UsuarioRest {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@Valid @RequestBody UsuarioDTO dto){

        Usuario usuario = ConverterUtil.deepConvertToDTO(dto, Usuario.class);

        UsuarioDTO usuarioDTO = ConverterUtil.converterToDTO(service.salvar(usuario), UsuarioDTO.class);

        return ResponseEntity.ok(new ResponseDTO(usuarioDTO, "sucesso.salvo:Usuário"));
    }
}
