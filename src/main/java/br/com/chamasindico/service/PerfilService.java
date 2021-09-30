package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.IPerfilRepository;
import br.com.chamasindico.repository.model.Perfil;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class PerfilService extends ServiceAbstract<Perfil, IPerfilRepository> {

    @Autowired
    public PerfilService(IPerfilRepository repository){
        this.repository = repository;
    }


    public Perfil buscarPorRole(String role) {
        Optional<Perfil> optional = repository.findByRole(role);

        if(optional.isPresent()){
            return optional.get();
        }else {
            throw new ChamaSindicoException("erro.naoencontrado:Perfil");
        }
    }
}
