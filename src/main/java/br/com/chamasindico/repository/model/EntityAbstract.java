package br.com.chamasindico.repository.model;

import java.io.Serializable;

public abstract class EntityAbstract<T> implements Serializable {

    private T id;

    public T getId(){
        return id;
    }

    public EntityAbstract(){}
}