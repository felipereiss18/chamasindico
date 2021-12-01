package br.com.chamasindico.service;

import br.com.chamasindico.enums.TipoConfirmacao;
import br.com.chamasindico.enums.TipoReserva;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.AgendaRepository;
import br.com.chamasindico.repository.dao.AreaComumRepository;
import br.com.chamasindico.repository.dao.IProprietarioRepository;
import br.com.chamasindico.repository.dao.InquilinoRepository;
import br.com.chamasindico.repository.model.*;
import br.com.chamasindico.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaService extends ServiceAbstract<Agenda, AgendaRepository> {

    @Autowired
    private AreaComumRepository areaComumRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InquilinoRepository inquilinoRepository;

    @Autowired
    private IProprietarioRepository proprietarioRepository;

    @Autowired
    public AgendaService(AgendaRepository repository) {
        this.repository = repository;
    }

    public List<Agenda> buscarPorData(LocalDate data, Long idCondominio) {

        Condominio condominio = Condominio.builder().id(idCondominio).build();

        return this.repository.findAllByDataAndCondominio(data, condominio);
    }

    @Override
    public Agenda salvar(Agenda agenda) {

        AreaComum areaComum = areaComumRepository.findById(agenda.getAreaComum().getId())
                .orElseThrow(() -> new ChamaSindicoException("Não foi encontrada a área comum."));

        Integer diaSemana = agenda.getData().getDayOfWeek().getValue();

        if(areaComum.getDiasFuncionamento().stream().noneMatch(ac -> ac.getId().getDia().equals(diaSemana))) {
            throw new ChamaSindicoException("Área Comum não permite agendamento no dia da semana selecionado.");
        }

        if (!areaComum.getSituacao()) {
            throw new ChamaSindicoException("Área Comum se encontra inativa para agendamento.");
        }

        if (!areaComum.getLocacao()) {
            throw new ChamaSindicoException("Área Comum não é permitida alocação.");
        }

        if (areaComum.getTipoReserva().equals(TipoReserva.POR_DIA.getTipo())) {
            this.repository.findByDataAndAreaComum(agenda.getData(), areaComum).ifPresent(a -> {
                throw new ChamaSindicoException("Há agendamento para área comum para o dia solicitado.");
            });

            agenda.setInicio(areaComum.getInicial());
            agenda.setTermino(areaComum.getFim());

        }else if (areaComum.getTipoReserva().equals(TipoReserva.POR_HORA.getTipo())) {

            if (areaComum.getInicial().compareTo(agenda.getInicio()) > 0 ||
                    areaComum.getFim().compareTo(agenda.getTermino()) < 0) {
                throw new ChamaSindicoException("Horário não permitido para Área Comum.");
            }

            this.repository.findByDataAndInicioAndTerminoAndAreaComum(
                    agenda.getData(), agenda.getInicio(), agenda.getTermino(), areaComum).ifPresent(a -> {
                        throw new ChamaSindicoException("Há agendamento para área comum para o dia e hora solicitado.");
            });
        }

        agenda = super.salvar(agenda);

        this.enviarEmail(agenda);

        return agenda;
    }

    private void enviarEmail(Agenda agenda) {

        AreaComum areaComum = areaComumRepository.findById(agenda.getAreaComum().getId()).get();

        String email = this.buscarEmail(agenda.getUnidade());
        String titulo = "Reserva agendada - " + areaComum.getNome();
        StringBuilder corpo = new StringBuilder();
        corpo.append("Hummm!! Acabamos de receber um pedido de agendamento para ")
                .append(areaComum.getNome())
                .append(" na data ")
                .append(Utils.localDateToString(agenda.getData())).append("!!<br><br>");

        if (areaComum.getAnotacao() != null && !areaComum.getAnotacao().isEmpty()) {
            corpo.append("Lembrando que essa área tem a seguinte anotação: <br>")
                    .append(areaComum.getAnotacao())
                    .append("<br><br>");
        }

        if (areaComum.getLimpeza() != null && !areaComum.getLimpeza().isEmpty()) {
            corpo.append("#Ficaadica!!! Temos as seguintes anotação de limpeza: <br>")
                    .append(areaComum.getLimpeza());
        }

        try {
            this.emailService.sendEmail(email, titulo, corpo.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private String buscarEmail(Unidade unidade) {
        Optional<Proprietario> optProprietario =
                proprietarioRepository.findByUnidadeAndUsuario_Situacao(unidade, true);

        if (optProprietario.isPresent()) {
            return optProprietario.get().getEmail();
        } else {
            Optional<Inquilino> optInquilino =
                    inquilinoRepository.findFirstByAluguel_UnidadeAndUsuario_Situacao(unidade, true);

            if (optInquilino.isPresent()) {
                return optInquilino.get().getEmail();
            }
        }

        return "";
    }


    @Override
    public Agenda editar(Agenda agenda) {
        super.buscarPorId(agenda.getId());

        return this.salvar(agenda);
    }

    public void confirmar(Agenda agenda) {
        Agenda editar = repository.save(agenda);

        this.enviarEmailConfirmacao(editar);
    }

    private void enviarEmailConfirmacao(Agenda editar) {

        String email = this.buscarEmail(editar.getUnidade());
        String titulo = "Confirmação de reserva - " + editar.getAreaComum().getNome();
        StringBuilder corpo = new StringBuilder();
        corpo.append("Opa, muito bom!!! Acabamos de receber sua confirmação do agendamento para  ")
                .append(editar.getAreaComum().getNome())
                .append(" na data ")
                .append(Utils.localDateToString(editar.getData())).append("!!<br><br>");

        if (editar.getAreaComum().getAnotacao() != null && !editar.getAreaComum().getAnotacao().isEmpty()) {
            corpo.append("Lembrando que essa área tem a seguinte anotação: <br>")
                    .append(editar.getAreaComum().getAnotacao())
                    .append("<br><br>");
        }

        if (editar.getAreaComum().getLimpeza() != null && !editar.getAreaComum().getLimpeza().isEmpty()) {
            corpo.append("#Ficaadica!!! Temos as seguintes anotação de limpeza: <br>")
                    .append(editar.getAreaComum().getLimpeza());
        }

        try {
            this.emailService.sendEmail(email, titulo, corpo.toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public List<Agenda> listarAgendamentoParaConfirmacao(LocalDate data, Unidade unidade) {
        return this.repository.findAllByConfirmacaoIsFalseAndUnidadeAndDataGreaterThanEqualAndAreaComum_TipoConfirmacao
                (unidade, data, TipoConfirmacao.CONFIRMACAO.getTipo());
    }

}
