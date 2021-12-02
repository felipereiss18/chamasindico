package br.com.chamasindico.rest;

import br.com.chamasindico.dto.arquitetura.ResponseDTO;
import br.com.chamasindico.dto.model.AgendaDTO;
import br.com.chamasindico.dto.model.AreaComumDTO;
import br.com.chamasindico.dto.pesquisa.AgendaRespDTO;
import br.com.chamasindico.dto.relatorio.EstatisticaOcorrenciaRelReqDTO;
import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.enums.TipoConfirmacao;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.model.Agenda;
import br.com.chamasindico.repository.model.AreaComum;
import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.relatorio.EstatisticaAgendaAreaComum;
import br.com.chamasindico.security.UserPrincipal;
import br.com.chamasindico.security.annotation.RoleGlobal;
import br.com.chamasindico.security.annotation.RoleInquilino;
import br.com.chamasindico.security.annotation.RoleProprietario;
import br.com.chamasindico.security.annotation.RoleSindico;
import br.com.chamasindico.service.AgendaService;
import br.com.chamasindico.service.InquilinoService;
import br.com.chamasindico.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("agenda")
public class AgendaRest {

    @Autowired
    private AgendaService service;

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private InquilinoService inquilinoService;

    @RoleGlobal
    @GetMapping("{data}/agendamentos")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorData(@PathVariable("data") String data) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        LocalDateTime localDateTime = LocalDateTime.parse(data);

        List<AgendaRespDTO> lista = service.buscarPorData(localDateTime.toLocalDate(), principal.getCondominio()).stream().map(
                agenda -> AgendaRespDTO.builder()
                        .id(agenda.getId())
                        .data(agenda.getData())
                        .areaComum(agenda.getAreaComum().getNome())
                        .bloco(agenda.getUnidade().getId().getBloco().getId().getId())
                        .unidade(agenda.getUnidade().getId().getId())
                        .inicio(agenda.getInicio())
                        .termino(agenda.getTermino())
                        .dono(verificarDono(agenda.getUnidade(), principal))
                        .confirmado(agenda.isConfirmacao())
                        .necessariaConfirmacao(
                                agenda.getAreaComum().getTipoConfirmacao()
                                        .equals(TipoConfirmacao.CONFIRMACAO.getTipo())
                        )
                    .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    private boolean verificarDono(Unidade unidade, UserPrincipal principal) {
        Unidade unidadeUsu = verificarUnidade(principal);

        return unidadeUsu != null && unidadeUsu.getId().getId().equals(unidade.getId().getId()) &&
            unidadeUsu.getId().getBloco().getId().getId().equals(unidade.getId().getBloco().getId().getId()) &&
            unidadeUsu.getId().getBloco().getId().getCondominio().getId()
                    .equals(unidade.getId().getBloco().getId().getCondominio().getId());
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @PostMapping
    public ResponseEntity<ResponseDTO> salvar(@RequestBody AgendaDTO dto) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Agenda agenda = Agenda.builder()
                .id(null)
                .areaComum(AreaComum.builder().id(dto.getAreaComum().getId()).build())
                .unidade(verificarUnidade(principal))
                .data(dto.getData())
                .inicio(dto.getInicio())
                .termino(dto.getTermino())
                .observacao(dto.getObservacao())
                .build();

        service.salvar(agenda);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @PutMapping("{id}")
    public ResponseEntity<ResponseDTO> alterar(@PathVariable("id") Long id, @RequestBody AgendaDTO dto) {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Agenda agenda = Agenda.builder()
                .id(id)
                .areaComum(AreaComum.builder().id(dto.getAreaComum().getId()).build())
                .unidade(verificarUnidade(principal))
                .data(dto.getData())
                .inicio(dto.getInicio())
                .termino(dto.getTermino())
                .observacao(dto.getObservacao())
                .build();

        service.editar(agenda);

        return ResponseEntity.ok(new ResponseDTO());
    }

    private Unidade verificarUnidade(UserPrincipal principal) {
        if (principal.getPerfil().getRole().equals(Roles.INQUILINO.getRole())) {
           return inquilinoService.buscarPorId(principal.getId()).getAluguel().getUnidade();
        } else if (principal.getPerfil().getRole().equals(Roles.SINDICO.getRole()) ||
                principal.getPerfil().getRole().equals(Roles.PROPRIETARIO.getRole())) {
            return proprietarioService.buscarPorId(principal.getId()).getUnidade();
        } else if (!principal.getPerfil().getRole().equals(Roles.FUNCIONARIO.getRole())){
            throw new ChamaSindicoException("Não foi possível encontrar a Unidade atual do usuário.");
        }

        return null;
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO> excluir(@PathVariable("id") Long id) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Agenda agenda = service.buscarPorId(id);

        if (!verificarDono(agenda.getUnidade(), principal)) {
            throw new ChamaSindicoException("O usuário logado não é o dono do agendamento.");
        }

        service.excluir(id);

        return ResponseEntity.ok(new ResponseDTO());
    }

    @RoleGlobal
    @GetMapping("{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarPorId(@PathVariable("id") Long id) {

        Agenda agenda = service.buscarPorId(id);

        AgendaDTO dto = AgendaDTO.builder()
                .id(agenda.getId())
                .areaComum(AreaComumDTO.builder()
                        .id(agenda.getAreaComum().getId())
                        .situacao(agenda.getAreaComum().getSituacao())
                        .nome(agenda.getAreaComum().getNome())
                        .locacao(agenda.getAreaComum().getLocacao())
                        .tipoReserva(agenda.getAreaComum().getTipoReserva())
                        .build()
                )
                .bloco(agenda.getUnidade().getId().getBloco().getId().getId())
                .unidade(agenda.getUnidade().getId().getId())
                .confirmacao(agenda.isConfirmacao())
                .data(agenda.getData())
                .inicio(agenda.getInicio())
                .termino(agenda.getTermino())
                .observacao(agenda.getObservacao())
                .build();

        return ResponseEntity.ok(new ResponseDTO(dto));
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @GetMapping("confirmacao-pendente")
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDTO> buscarConfirmacaoPendente() {

        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Unidade unidade = this.verificarUnidade(principal);

        List<AgendaRespDTO> lista = service.listarAgendamentoParaConfirmacao(LocalDate.now(), unidade).stream()
                .map(agenda -> AgendaRespDTO.builder()
                        .id(agenda.getId())
                        .data(agenda.getData())
                        .inicio(agenda.getInicio())
                        .termino(agenda.getTermino())
                        .areaComum(agenda.getAreaComum().getNome())
                        .build()
                ).collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }

    @RoleSindico
    @RoleProprietario
    @RoleInquilino
    @PatchMapping("{id}/confirmar")
    public ResponseEntity<ResponseDTO> confirmar(@PathVariable("id") Long id) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        Agenda agenda = service.buscarPorId(id);

        if (this.verificarDono(agenda.getUnidade(), principal)) {
            agenda.setConfirmacao(true);
            service.confirmar(agenda);
        }else {
            throw new ChamaSindicoException("Usuário não é dono do agendamento.");
        }

        return ResponseEntity.ok(new ResponseDTO());
    }


    @RoleSindico
    @PostMapping("relatorio/area-comum")
    public ResponseEntity<ResponseDTO> gerarRelatorioPorTipo(@RequestBody EstatisticaOcorrenciaRelReqDTO dto) {

        List<EstatisticaAgendaAreaComum> lista = service.buscarEstatisticaAreaComum(dto.getInicio(),dto.getFim());

        return ResponseEntity.ok(new ResponseDTO(lista));
    }
}
