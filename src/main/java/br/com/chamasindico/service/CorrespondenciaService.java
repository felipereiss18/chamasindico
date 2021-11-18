package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.CorrespondenciaRepository;
import br.com.chamasindico.repository.dao.IProprietarioRepository;
import br.com.chamasindico.repository.dao.InquilinoRepository;
import br.com.chamasindico.repository.model.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

@Service
public class CorrespondenciaService extends ServiceAbstract<Correspondencia, CorrespondenciaRepository> {

    @Autowired
    private EmailService emailService;

    @Autowired
    private InquilinoRepository inquilinoRepository;

    @Autowired
    private IProprietarioRepository proprietarioRepository;

    @Autowired
    public CorrespondenciaService(CorrespondenciaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(rollbackFor = ChamaSindicoException.class)
    public Correspondencia salvar(Correspondencia correspondencia) {

        boolean enviarEmail = correspondencia.getId() == null;

        Correspondencia corresp = super.salvar(correspondencia);

        if (enviarEmail) {
            this.enviarEmail(corresp);
        }

        return corresp;
    }

    @Override
    public void excluir(Object id) {

        Correspondencia correspondencia = super.buscarPorId(id);

        super.excluir(id);

        if (correspondencia != null) {
            this.enviarEmailExcluir(correspondencia);
        }
    }

    private void enviarEmailExcluir(Correspondencia corresp) {
        String destinatario = buscarDestinatario(corresp.getUnidade());
        String titulo = "Atualização de Correspondência";
        StringBuilder corpo = new StringBuilder();

        corpo.append("Olá, parece que houve um engano!<br><br>")
                .append("A correspondência ");
        if(corresp.getGenero().equals('F')) {
            corpo.append("da ");
        }else {
            corpo.append("do ");
        }

        corpo.append(corresp.getRemetente()).append(" foi informada incorretamente! <br><br>")
                .append("Pedimos desculpa pelo transtorno.");

        if(destinatario != null) {
            try {
                emailService.sendEmail(destinatario, titulo, corpo.toString());
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new ChamaSindicoException("Erro ao enviar e-mail.");
            }
        }
    }

    private void enviarEmail(Correspondencia corresp) {

        String destinatario = buscarDestinatario(corresp.getUnidade());
        String titulo = "Nova Correspondência";
        StringBuilder corpo = new StringBuilder();

        corpo.append("Temos uma ótima notícia!<br><br>")
                .append("Chegou uma correspondência ");
        if(corresp.getGenero().equals('F')) {
            corpo.append("da ");
        }else {
            corpo.append("do ");
        }

        corpo.append(corresp.getRemetente()).append("! <br><br>")
                .append("Aguardamos a sua presença na portaria para buscá-la.");

        if(destinatario != null) {
            try {
                emailService.sendEmail(destinatario, titulo, corpo.toString());
            } catch (MessagingException e) {
                e.printStackTrace();
                throw new ChamaSindicoException("Erro ao enviar e-mail.");
            }
        }
    }

    private String buscarDestinatario(Unidade unidade) {
        String destinatario = null;

        List<Inquilino> inquilinos =
                inquilinoRepository.findAllByAluguel_UnidadeAndUsuario_Situacao(unidade, true);

        if (inquilinos != null && !inquilinos.isEmpty()) {
            destinatario = inquilinos.get(0).getEmail();
        } else {
            Optional<Proprietario> proprietario =
                    proprietarioRepository.findByUnidadeAndUsuario_Situacao(unidade, true);
            if (proprietario.isPresent()) {
                destinatario = proprietario.get().getEmail();
            }
        }

        return destinatario;
    }

    public Page<Correspondencia> pesquisar(Correspondencia correspondencia, Pageable pageable) {
        QCorrespondencia qCorrespondencia = QCorrespondencia.correspondencia;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (correspondencia.getUnidade() != null
                && correspondencia.getUnidade().getId() != null
                && correspondencia.getUnidade().getId().getId() != null) {
            booleanBuilder.and(qCorrespondencia.unidade.id.id.eq(correspondencia.getUnidade().getId().getId()));
            booleanBuilder.and(qCorrespondencia.unidade.id.bloco.id.id.eq(
                    correspondencia.getUnidade().getId().getBloco().getId().getId())
            );
            booleanBuilder.and(qCorrespondencia.unidade.id.bloco.id.condominio.id.eq(
                    correspondencia.getUnidade().getId().getBloco().getId().getCondominio().getId())
            );
        } else if (correspondencia.getBloco() != null
                && correspondencia.getBloco().getId() != null
                && !correspondencia.getBloco().getId().getId().isEmpty()) {
            booleanBuilder.and(qCorrespondencia.bloco.id.id.eq(correspondencia.getBloco().getId().getId()));
            booleanBuilder.and(qCorrespondencia.bloco.id.condominio.id.eq(correspondencia.getCondominio().getId()));
        } else if (correspondencia.getCondominio() != null && correspondencia.getCondominio().getId() != null) {
            booleanBuilder.and(qCorrespondencia.condominio.id.eq(correspondencia.getCondominio().getId()));
        }

        if (correspondencia.getData() != null) {
            booleanBuilder.and(qCorrespondencia.data.eq(correspondencia.getData()));
        }

        if (correspondencia.getEntrega() != null) {
            booleanBuilder.and(qCorrespondencia.entrega.eq(correspondencia.getEntrega()));
        }

        if (correspondencia.getRemetente() != null && !correspondencia.getRemetente().isEmpty()) {
            booleanBuilder.and(qCorrespondencia.remetente.contains(correspondencia.getRemetente()));
        }

        return repository.findAll(booleanBuilder, pageable);
    }

    public List<Correspondencia> buscarAtivaPorUnidade(Unidade unidade) {
        return repository.findAllByUnidadeAndEntregaIsNull(unidade);
    }
}
