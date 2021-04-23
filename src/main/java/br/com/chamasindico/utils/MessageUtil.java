package br.com.chamasindico.utils;

import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil
{
    private static MessageSource messageSource;

    private MessageUtil(){}

    public static String tratarMensagem(String mensagem) {
        Pattern p = Pattern.compile("[a-z]+\\.{1}[a-z]+[:]*.*");
        Matcher m = p.matcher(mensagem);
        if (m.matches()) {
            String[] params = mensagem.split(":");
            if (params.length == 2) {
                mensagem = MessageUtil.getMessage(params[0].trim(), params[1].trim());
            } else {
                mensagem = MessageUtil.getMessage(mensagem.trim());
            }
        }

        return mensagem;
    }

    private static String getMessage(String code){
        return messageSource.getMessage(code, null, Locale.getDefault());
    }

    private static String getMessage(String code, Object...objects){
        return messageSource.getMessage(code, objects, Locale.getDefault());
    }

    public static void injectMessageSource(MessageSource ms) {
        if (messageSource == null)
            messageSource = ms;
    }
}
