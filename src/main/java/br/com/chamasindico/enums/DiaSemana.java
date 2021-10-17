package br.com.chamasindico.enums;

public enum DiaSemana {

    DOMINGO(1),
    SEGUNDA(2),
    TERCA(3),
    QUARTA(4),
    QUINTA(5),
    SEXTA(6),
    SABADO(7);

    private final int dia;

    DiaSemana(int valorDia){
        dia = valorDia;
    }

    private int getDia(){
        return dia;
    }
}
