package br.com.chamasindico.enums;

public enum Roles {

    ADMIN("ADMIN"),
    SINDICO("SINDICO"),
    INQUILINO("INQUI"),
    PROPRIETARIO("PROPRIE"),
    FUNCIONARIO("FUNCIO");

    private final String role;

    Roles(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
