package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.VisitaRepository;
import br.com.chamasindico.repository.model.Visita;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitaService extends ServiceAbstract<Visita, VisitaRepository> {

    @Autowired
    public VisitaService(VisitaRepository repository) {
        this.repository = repository;
    }
}
