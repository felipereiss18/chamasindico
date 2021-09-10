package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.IEstadoRepository;
import br.com.chamasindico.repository.model.Estado;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class EstadoService extends ServiceAbstract<Estado, IEstadoRepository> {

    @Autowired
    public EstadoService(IEstadoRepository repository){
        this.repository = repository;
    }
}
