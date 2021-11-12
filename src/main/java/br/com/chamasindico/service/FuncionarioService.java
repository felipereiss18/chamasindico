package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.FuncionarioRepository;
import br.com.chamasindico.repository.model.Funcionario;
import br.com.chamasindico.repository.model.QFuncionario;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService extends ServiceAbstract<Funcionario, FuncionarioRepository>{

    @Autowired
    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public Page<Funcionario> pesquisar(String nome, String usuario, Pageable page) {
        QFuncionario qc = QFuncionario.funcionario;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (nome != null && !nome.isEmpty()) {
            booleanBuilder.and(qc.nome.contains(nome));
        }

        if (usuario != null && !usuario.isEmpty()) {
            booleanBuilder.and(qc.usuario.nome.contains(usuario));
        }

        return repository.findAll(booleanBuilder, page);
    }
}
