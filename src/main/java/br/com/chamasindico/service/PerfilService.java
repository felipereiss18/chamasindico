package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.IPerfilRepository;
import br.com.chamasindico.repository.model.Perfil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class PerfilService extends ServiceAbstract<Perfil, IPerfilRepository> {

    @Autowired
    public PerfilService(IPerfilRepository repository){
        this.repository = repository;
    }
}
