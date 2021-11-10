package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.AluguelDTO;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.AluguelService;
import br.com.chamasindico.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("aluguel")
public class AluguelRest {

    @Autowired
    private AluguelService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PerfilService perfilService;

    @RoleSindico
    @RoleAdmin
    @PostMapping("{condominio}/{bloco}/{unidade}")
    public ResponseEntity<ResponseDTO> salvar(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade,
            @RequestBody AluguelDTO dto
    ){
        Aluguel aluguel = Aluguel.builder()
                .id(null)
                .unidade(Unidade.builder()
                        .id(UnidadePK.builder()
                                .id(unidade)
                                .bloco(Bloco.builder()
                                        .id(BlocoPK.builder()
                                                .id(bloco)
                                                .condominio(Condominio.builder().id(idCondominio).build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .inicio(dto.getDataInicio())
                .fim(dto.getDataFim())
                .build();

        Inquilino inquilino = Inquilino.builder()
                .aluguel(aluguel)
                .cpf(dto.getInquilino().getCpf())
                .nome(dto.getInquilino().getNome())
                .email(dto.getInquilino().getEmail())
                .nascimento(dto.getInquilino().getNascimento())
                .telefone(dto.getInquilino().getTelefone())
                .build();


        inquilino.setUsuario(
                new Usuario(
                    dto.getInquilino().getUsuario().getId(),
                    perfilService.buscarPorRole(Roles.INQUILINO.getRole()),
                    dto.getInquilino().getUsuario().getNome(),
                    passwordEncoder.encode("123456"),
                    verificarDataContrato(dto.getDataInicio(), dto.getDataFim()),
                    inquilino
                )
        );

        aluguel.setInquilinos(Set.of(inquilino));

        service.salvar(aluguel);

        return ResponseEntity.ok(new ResponseDTO());

    }

    @RoleAdmin
    @RoleSindico
    @PutMapping("{condominio}/{bloco}/{unidade}/{id}")
    public ResponseEntity<ResponseDTO> editar(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade,
            @PathVariable("id") Long id,
            @RequestBody AluguelDTO dto
    ) {

        Aluguel aluguel = Aluguel.builder()
                .id(id)
                .unidade(Unidade.builder()
                        .id(UnidadePK.builder()
                                .id(unidade)
                                .bloco(Bloco.builder()
                                        .id(BlocoPK.builder()
                                                .id(bloco)
                                                .condominio(Condominio.builder().id(idCondominio).build())
                                                .build())
                                        .build())
                                .build())
                        .build())
                .inicio(dto.getDataInicio())
                .fim(dto.getDataFim())
                .build();

        Inquilino inquilino = Inquilino.builder()
                .aluguel(aluguel)
                .cpf(dto.getInquilino().getCpf())
                .nome(dto.getInquilino().getNome())
                .email(dto.getInquilino().getEmail())
                .nascimento(dto.getInquilino().getNascimento())
                .telefone(dto.getInquilino().getTelefone())
                .build();


        inquilino.setUsuario(
                new Usuario(
                        dto.getInquilino().getUsuario().getId(),
                        perfilService.buscarPorRole(Roles.INQUILINO.getRole()),
                        dto.getInquilino().getUsuario().getNome(),
                        null,
                        verificarDataContrato(dto.getDataInicio(), dto.getDataFim()),
                        inquilino
                )
        );

        aluguel.setInquilinos(Set.of(inquilino));

        service.editar(aluguel);

        return ResponseEntity.ok(new ResponseDTO());
    }

    private Boolean verificarDataContrato(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate data = LocalDate.now();

        return (data.isAfter(dataInicio) || data.isEqual(dataInicio)) &&
                (data.isBefore(dataFim) || data.isEqual(dataFim));
    }
}
