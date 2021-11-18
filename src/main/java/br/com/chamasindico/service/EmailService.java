package br.com.chamasindico.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private static String remetente = "chamasindico.contato@gmail.com";

    public void sendEmail(String destinatario, String titulo, String corpo) throws MessagingException {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setContent(corpo, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setTo(destinatario);
        helper.setFrom(remetente);
        helper.setSubject(titulo);

        emailSender.send(mimeMessage);
    }

    public void sendEmailVariosDestinatario(List<String> destinatarios, String titulo, String corpo) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setContent(corpo, "text/html");
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        helper.setBcc(destinatarios.toArray(String[]::new));
        helper.setFrom(remetente);
        helper.setSubject(titulo);

        emailSender.send(mimeMessage);
    }
}
