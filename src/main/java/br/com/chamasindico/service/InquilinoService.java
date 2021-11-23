package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.InquilinoRepository;
import br.com.chamasindico.repository.model.Inquilino;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InquilinoService extends ServiceAbstract<Inquilino, InquilinoRepository> {

    @Autowired
    public InquilinoService(InquilinoRepository repository) {
        this.repository = repository;
    }

    public Inquilino buscarPorUsuarioAtivo(Long idUsuario) {
        Optional<Inquilino> inquilino = repository.findByUsuario_IdAndUsuario_Situacao(idUsuario, true);

        return inquilino.orElseThrow(() ->
                new ChamaSindicoException("Não foi encontrado o inquilino com usuário ativo", HttpStatus.BAD_REQUEST));
    }

    public List<Inquilino> buscarPorUnidadeUsuarioAtivo(Unidade unidade) {
        return repository.findAllByAluguel_UnidadeAndUsuario_Situacao(unidade, true);
    }
}
