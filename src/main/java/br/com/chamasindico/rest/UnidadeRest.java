package br.com.chamasindico.rest;

import br.com.chamasindico.dto.GaragemDTO;
import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.AluguelDTO;
import br.com.chamasindico.dto.model.UnidadeDTO;
import br.com.chamasindico.dto.pesquisa.UnidadePesqReqDTO;
import br.com.chamasindico.dto.pesquisa.UnidadePesqRespDTO;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleAdmin;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.AluguelService;
import br.com.chamasindico.service.UnidadeService;
import br.com.chamasindico.utils.Converter;
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
@RequestMapping("unidade")
public class UnidadeRest {

    @Autowired
    private UnidadeService service;

    @Autowired
    private AluguelService aluguelService;

    @RoleAdmin
    @RoleSindico
    @PostMapping("pesquisar")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> pesquisar(@RequestBody UnidadePesqReqDTO dto, @PageableDefault Pageable page){

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!principal.getPerfil().getRole().equals(Roles.ADMIN.getRole())){
            dto.setIdCondominio(principal.getCondominio());
        }

        Unidade unidade = Unidade
                .builder()
                .id(UnidadePK
                        .builder()
                        .id(dto.getUnidade())
                        .bloco(Bloco
                                .builder()
                                .id(BlocoPK.builder()
                                        .id(dto.getIdBloco())
                                        .condominio(Condominio.builder().id(dto.getIdCondominio()).build())
                                        .build())
                                .build()
                        )
                        .build()
                ).build();

        Page<UnidadePesqRespDTO> pages = service.pesquisar(unidade, page).map(uni ->
                    UnidadePesqRespDTO.builder()
                            .unidade(uni.getId().getId())
                            .bloco(uni.getId().getBloco().getId().getId())
                            .idCondominio(uni.getId().getBloco().getId().getCondominio().getId())
                            .condominio(uni.getId().getBloco().getId().getCondominio().getNome())
                            .banheiros(uni.getBanheiros())
                            .metragem(uni.getMetragem())
                            .quartos(uni.getQuartos())
                            .build()
                );

        return ResponseEntity.ok(new ResponseDTO(pages));
    }

    @RoleGlobal
    @GetMapping("{condominio}/{bloco}/{unidade}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade
    ){
        Unidade uni = service.buscarPorId(
                new UnidadePK(unidade, new Bloco(new BlocoPK(bloco, new Condominio(idCondominio))))
        );

        UnidadeDTO dto = UnidadeDTO.builder()
                .id(uni.getId().getId())
                .banheiros(uni.getBanheiros())
                .quartos(uni.getQuartos())
                .metragem(uni.getMetragem())
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleGlobal
    @GetMapping("{condominio}/{bloco}/{unidade}/alugueis")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarAlugueis(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade
    ) {
        Unidade uni = new Unidade(new UnidadePK(unidade, new Bloco(new BlocoPK(bloco, new Condominio(idCondominio)))));

        List<AluguelDTO> lista = aluguelService.buscarPorUnidade(uni).stream()
                .map(Converter::aluguelToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleSindico
    @RoleAdmin
    @PutMapping("{condominio}/{bloco}/{unidade}/garagem")
    public ResponseEntity<ResponseDTO> alterarGaragem(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade,
            @RequestBody GaragemDTO dto
    ){

        Unidade uni = service.buscarPorId(
                new UnidadePK(unidade, new Bloco(new BlocoPK(bloco, new Condominio(idCondominio))))
        );

        uni.setGaragem(dto.getNumero());
        uni.setPlaca(dto.getPlaca());
        uni.setMarca(dto.getMarca());
        uni.setModelo(dto.getModelo());
        uni.setAnoFabricacao(dto.getAnoFabricacao());
        uni.setAnoModelo(dto.getAnoModelo());

        service.salvar(uni);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleSindico
    @RoleAdmin
    @GetMapping("{condominio}/{bloco}/{unidade}/garagem")
    public ResponseEntity<ResponseDTO> buscarGaragem(
            @PathVariable("condominio") Long idCondominio,
            @PathVariable("bloco") String bloco,
            @PathVariable("unidade") Long unidade
    ){

        Unidade uni = service.buscarPorId(
                new UnidadePK(unidade, new Bloco(new BlocoPK(bloco, new Condominio(idCondominio))))
        );

        GaragemDTO dto = GaragemDTO.builder()
                .numero(uni.getGaragem())
                .placa(uni.getPlaca())
                .marca(uni.getMarca())
                .modelo(uni.getModelo())
                .anoFabricacao(uni.getAnoFabricacao())
                .anoModelo(uni.getAnoModelo())
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }
}
