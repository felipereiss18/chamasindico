package br.com.chamasindico.rest;

import br.com.chamasindico.dto.SenhaDTO;
import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.PerfilDTO;
import br.com.chamasindico.dto.model.UsuarioDTO;
import br.com.chamasindico.dto.pesquisa.UsuarioPesqReqDTO;
import br.com.chamasindico.dto.pesquisa.UsuarioPesqRespDTO;
import br.com.chamasindico.repository.model.Perfil;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.UsuarioService;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("usuario")
public class UsuarioRest {

    @Autowired
    private UsuarioService service;

    @RoleAdmin
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@Valid @RequestBody UsuarioDTO dto){

        Usuario usuario = ConverterUtil.deepConvertToDTO(dto, Usuario.class);

        UsuarioDTO usuarioDTO = ConverterUtil.deepConvertToDTO(service.salvar(usuario), UsuarioDTO.class);

        return ResponseEntity.ok(new ResponseDTO(usuarioDTO.getId(), "sucesso.salvo:Administrador"));
    }

    @RoleAdmin
    @RoleSindico
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscar(){
        List<UsuarioDTO> list = service.listar()
                .stream()
                .map(usu -> ConverterUtil.deepConvertToDTO(usu, UsuarioDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @RoleSindico
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(@RequestBody UsuarioPesqReqDTO dto, @PageableDefault Pageable page){
        Page<UsuarioPesqRespDTO> list =
                service.pesquisar(
                        dto.getUsuario(),
                        dto.getSituacao(),
                        ConverterUtil.converterToDTO(dto.getPerfil(), Perfil.class),
                        page
                )
                .map(usu -> UsuarioPesqRespDTO.builder()
                        .id(usu.getId())
                        .usuario(usu.getNome())
                        .situacao(usu.getSituacao())
                        .perfil(ConverterUtil.converterToDTO(usu.getPerfil(), PerfilDTO.class))
                        .build()
                );

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable Long id){
        UsuarioDTO usuarioDTO = ConverterUtil.deepConvertToDTO(service.buscarPorId(id), UsuarioDTO.class);
        usuarioDTO.setSenha(null);

        return ResponseEntity.ok(new ResponseDTO(usuarioDTO));
    }

    @RoleAdmin
    @RoleSindico
    @GetMapping("paginacao")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorPaginacao(@PageableDefault Pageable pageable){
        List<UsuarioDTO> list = service.listar(pageable)
                .stream()
                .map(usu -> ConverterUtil.deepConvertToDTO(usu, UsuarioDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(list));
    }

    @RoleAdmin
    @RoleSindico
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> alterar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO){

        Usuario usuario = ConverterUtil.deepConvertToDTO(usuarioDTO, Usuario.class);
        usuario.setId(id);

        service.editar(usuario);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable Long id) {
        service.excluir(id);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @PatchMapping("{id}/trocar-senha")
    public ResponseEntity<ResponseDTO> alterarSenha(@PathVariable Long id, @RequestBody SenhaDTO senhaDTO){
        service.trocarSenha(id, senhaDTO.getAtual(), senhaDTO.getNova());

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @PatchMapping("{id}/situacao")
    public ResponseEntity<ResponseDTO> alterarSituacao(@PathVariable Long id, @RequestBody boolean situacao) {

        service.alterarSituacao(id, situacao);

        return ResponseEntity.ok(new ResponseDTO());
    }
}
