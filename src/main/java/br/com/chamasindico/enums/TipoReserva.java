package br.com.chamasindico.enums;

public enum TipoReserva {

    POR_DIA(1),
    POR_HORA(2),
    INFORMATIVO(3);

    private final int tipo;

    TipoReserva(int tipoReserva){
        tipo = tipoReserva;
    }

    public int getTipo(){
        return tipo;
    }
}
