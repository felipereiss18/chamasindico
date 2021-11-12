package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.CondominioDTO;
import br.com.chamasindico.dto.model.FuncionarioDTO;
import br.com.chamasindico.dto.model.UsuarioDTO;
import br.com.chamasindico.dto.pesquisa.FuncionarioPesqReq;
import br.com.chamasindico.dto.pesquisa.FuncionarioPesqResp;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.repository.model.Condominio;
import br.com.chamasindico.repository.model.Funcionario;
import br.com.chamasindico.repository.model.Usuario;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.FuncionarioService;
import br.com.chamasindico.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("funcionario")
public class FuncionarioRest {

    @Autowired
    private FuncionarioService service;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RoleSindico
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(
            @RequestBody FuncionarioPesqReq dto,
            @PageableDefault Pageable page
    ) {


        Page<FuncionarioPesqResp> pages = service.pesquisar(dto.getNome(), dto.getUsuario(), page)
                .map(func ->
                    FuncionarioPesqResp.builder()
                            .id(func.getId())
                            .nome(func.getNome())
                            .usuario(func.getUsuario().getNome())
                            .situacao(func.getUsuario().getSituacao())
                            .email(func.getEmail())
                            .nascimento(func.getNascimento())
                            .telefone(func.getTelefone())
                            .build()
                );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleSindico
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody FuncionarioDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Funcionario funcionario = Funcionario.builder()
                .usuario(new Usuario(
                        perfilService.buscarPorRole(Roles.FUNCIONARIO.getRole()),
                        dto.getUsuario().getNome(),
                        passwordEncoder.encode("123456"),
                        true
                ))
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .cpf(dto.getCpf())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .nascimento(dto.getNascimento())
                .telefone(dto.getTelefone())
                .build();

        service.salvar(funcionario);

        return ResponseEntity.ok(new ResponseDTO());
    }


    @RoleSindico
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable("id") Long id, @RequestBody FuncionarioDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Funcionario funcionario = Funcionario.builder()
                .id(id)
                .usuario(new Usuario(
                        id,
                        perfilService.buscarPorRole(Roles.FUNCIONARIO.getRole()),
                        dto.getUsuario().getNome(),
                        passwordEncoder.encode("123456"),
                        true
                ))
                .condominio(Condominio.builder().id(principal.getCondominio()).build())
                .cpf(dto.getCpf())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .nascimento(dto.getNascimento())
                .telefone(dto.getTelefone())
                .build();

        service.editar(funcionario);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleSindico
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("id") Long id) {

        Funcionario funcionario = service.buscarPorId(id);

        FuncionarioDTO dto = FuncionarioDTO.builder()
                .usuario(UsuarioDTO.builder()
                        .id(funcionario.getUsuario().getId())
                        .nome(funcionario.getUsuario().getNome())
                        .situacao(funcionario.getUsuario().getSituacao())
                        .build()
                )
                .condominio(CondominioDTO.builder().id(funcionario.getCondominio().getId()).build())
                .nome(funcionario.getNome())
                .cpf(funcionario.getCpf())
                .email(funcionario.getEmail())
                .nascimento(funcionario.getNascimento())
                .telefone(funcionario.getTelefone())
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }
}
