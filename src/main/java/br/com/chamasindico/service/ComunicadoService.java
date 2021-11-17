package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.ComunicadoRepository;
import br.com.chamasindico.repository.dao.InquilinoRepository;
import br.com.chamasindico.repository.model.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComunicadoService extends ServiceAbstract<Comunicado, ComunicadoRepository> {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private InquilinoRepository inquilinoRepository;

    @Autowired
    public ComunicadoService(ComunicadoRepository repository) {
        this.repository = repository;
    }

    public Page<Comunicado> pesquisar(Comunicado comunicado, Pageable page) {

        QComunicado qComunicado = QComunicado.comunicado;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qComunicado.condominio.id.eq(comunicado.getCondominio().getId()));

        if (comunicado.getTitulo() != null && !comunicado.getTitulo().isEmpty()) {
            booleanBuilder.and(qComunicado.titulo.contains(comunicado.getTitulo()));
        }

        if (comunicado.getData() != null) {
            booleanBuilder.and(qComunicado.data.eq(comunicado.getData()));
        }

        if (comunicado.getVencimento() != null) {
            booleanBuilder.and(qComunicado.vencimento.eq(comunicado.getVencimento()));
        }

        return repository.findAll(booleanBuilder, page);
    }

    public List<Comunicado> buscarAtivos(Long condominio) {

        return repository.findAllByCondominio_IdAndVencimentoGreaterThanEqual(condominio, LocalDate.now());
    }

    public Comunicado buscarPorId(Long id, Long idCondominio) throws ChamaSindicoException {
        Optional<Comunicado> optional = repository.findByIdAndCondominio_Id(id, idCondominio);

        return optional
            .orElseThrow(() -> new ChamaSindicoException("erro.naoencontrada:Comunicado", HttpStatus.BAD_GATEWAY));
    }

    public void enviarComunicado(Long id, Long idCondominio) {
        Comunicado comunicado = this.buscarPorId(id, idCondominio);

        List<String> destinatarios = new ArrayList<>();
        destinatarios.addAll(
                proprietarioService.listarPorCondominio(comunicado.getCondominio().getId())
                        .stream().map(Proprietario::getEmail).collect(Collectors.toList())
        );
        destinatarios.addAll(inquilinoRepository.findAllByAluguel_CondominioAndUsuario_Situacao(
                Condominio.builder().id(idCondominio).build(),
                true
            ).stream().map(Inquilino::getEmail).collect(Collectors.toList())
        );

        try {
            emailService.sendEmailVariosDestinatario(destinatarios, comunicado.getTitulo(), comunicado.getCorpo());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new ChamaSindicoException("Erro ao enviar e-mail.");
        }
    }
}
