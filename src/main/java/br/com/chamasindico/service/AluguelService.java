package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.AluguelRepository;
import br.com.chamasindico.repository.dao.usuario.UsuarioRepository;
import br.com.chamasindico.repository.model.Aluguel;
import br.com.chamasindico.repository.model.Inquilino;
import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AluguelService extends ServiceAbstract<Aluguel, AluguelRepository>{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public AluguelService(AluguelRepository repository) {
        this.repository = repository;
    }

    public List<Aluguel> buscarPorUnidade(Unidade uni) {
        List<Aluguel> alugueis = repository.findAllByUnidade(uni);

        if (alugueis == null || alugueis.isEmpty()) {
            throw  new ChamaSindicoException("erro.nenhumresultado");
        }

        return alugueis;
    }

    @Override
    public Aluguel salvar(Aluguel aluguel) {

        verificarDatasContrato(aluguel);

        return super.salvar(aluguel);
    }

    @Override
    public Aluguel editar(Aluguel aluguel) {

        Inquilino inquilino = aluguel.getInquilinos().stream().findFirst().get();

        Usuario usuario = usuarioRepository.findById(inquilino.getUsuario().getId()).get();

        inquilino.getUsuario().setSenha(usuario.getSenha());

        inquilino.setId(inquilino.getUsuario().getId());

        aluguel.setInquilinos(Set.of(inquilino));

        return super.salvar(aluguel);
    }

    private void verificarDatasContrato(Aluguel aluguel) {

        Optional<Aluguel> aluguelMax = repository.findFirstByUnidadeOrderByFimDesc(aluguel.getUnidade());

        if (aluguelMax.isPresent()) {
            Aluguel max = aluguelMax.get();
            if (max.getFim().isAfter(aluguel.getInicio())) {
                max.setFim(aluguel.getInicio().minusDays(1));
                max.getInquilinos().forEach(inquilino -> inquilino.getUsuario().setSituacao(false));
                super.salvar(max);
            }
        }

    }
}
