package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.ICondominioRepository;
import br.com.chamasindico.repository.model.Condominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CondominioService extends ServiceAbstract<Condominio, ICondominioRepository> {

    @Autowired
    public CondominioService(ICondominioRepository repository){
        this.repository = repository;
    }

}
