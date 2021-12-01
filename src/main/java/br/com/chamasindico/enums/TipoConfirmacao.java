package br.com.chamasindico.enums;

public enum TipoConfirmacao {

    ALERTA_EMAIL(1),
    CONFIRMACAO(2),
    NAO_NECESSARIO(3);

    private final int tipo;

    TipoConfirmacao(int tipoConfirmacao){
        tipo = tipoConfirmacao;
    }

    public int getTipo(){
        return tipo;
    }
}
