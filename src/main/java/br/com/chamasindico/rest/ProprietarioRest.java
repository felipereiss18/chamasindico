package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.*;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.ProprietarioService;
import br.com.chamasindico.service.UsuarioService;
import br.com.chamasindico.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("proprietario")
public class ProprietarioRest {

    @Autowired
    private ProprietarioService service;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RoleAdmin
    @RoleSindico
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody ProprietarioDTO dto) {

        Proprietario proprietario =
                ConverterUtil.converterToDTO(dto, Proprietario.class, "usuario", "bloco", "unidade", "estado");

        proprietario.setUsuario(
                new Usuario(
                        null,
                        dto.getUsuario().getNome(),
                        passwordEncoder.encode("123456"),
                        true,
                        proprietario
                )
        );

        proprietario.setUnidade(new Unidade(
                new UnidadePK(
                        dto.getUnidade().getId(),
                        new Bloco(
                                new BlocoPK(dto.getBloco(), new Condominio(dto.getCondominio().getId()))
                        )
                )
            )
        );

        if (!dto.getSindico()) {
            proprietario.setEstado(Estado.builder().id(dto.getEstado().getId()).build());
        }

        Long id = service.salvar(proprietario).getId();

        return ResponseEntity.ok(new ResponseDTO(id));
    }

    @RoleAdmin
    @RoleSindico
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> editar(@PathVariable("id") Long id, @RequestBody ProprietarioDTO dto) {
        Proprietario proprietario =
                ConverterUtil.converterToDTO(dto, Proprietario.class, "usuario", "bloco", "unidade", "estado");

        proprietario.setId(id);

        proprietario.setUsuario(usuarioService.buscarPorId(id));
        proprietario.getUsuario().setNome(dto.getUsuario().getNome());

        proprietario.setUnidade(
                new Unidade(
                        new UnidadePK(
                                dto.getUnidade().getId(),
                                new Bloco(
                                        new BlocoPK(
                                                dto.getBloco(),
                                                Condominio.builder().id(dto.getCondominio().getId()).build()
                                        )
                                )
                        )
                )
        );

        if (!dto.getMorador()) {
            proprietario.setEstado(Estado.builder().id(dto.getEstado().getId()).build());
        }
        service.editar(proprietario);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleAdmin
    @RoleSindico
    @GetMapping("{condominio}/{bloco}/{unidade}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade
    ){
        Proprietario proprietario = service.buscarPorUnidade(new Unidade(
                        new UnidadePK(unidade, new Bloco(new BlocoPK(bloco, new Condominio(idCondominio))))
                )
        );

        ProprietarioDTO dto =
                ConverterUtil.converterToDTO(
                        proprietario, ProprietarioDTO.class, "usuario", "bloco", "unidade", "estado"
                );

        dto.setUsuario(UsuarioDTO.builder()
                .id(proprietario.getId())
                .nome(proprietario.getUsuario().getNome())
                .situacao(proprietario.getUsuario().getSituacao())
                .build()
        );
        dto.setUnidade(UnidadeDTO.builder()
                .id(proprietario.getUnidade().getId().getId())
                .build()
        );
        dto.setBloco(proprietario.getUnidade().getId().getBloco().getId().getId());
        dto.setCondominio(CondominioDTO.builder()
                .id(proprietario.getUnidade().getId().getBloco().getId().getCondominio().getId())
                .nome(proprietario.getUnidade().getId().getBloco().getId().getCondominio().getNome())
                .build()
        );

        if (!proprietario.isMorador()) {
            dto.setEstado(EstadoDTO.builder().id(proprietario.getEstado().getId()).build());
        }

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

}
