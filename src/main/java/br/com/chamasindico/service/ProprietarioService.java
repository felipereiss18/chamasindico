package br.com.chamasindico.service;

import br.com.chamasindico.enums.Roles;
import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.dao.IProprietarioRepository;
import br.com.chamasindico.repository.dao.usuario.UsuarioRepository;
import br.com.chamasindico.repository.model.Proprietario;
import br.com.chamasindico.repository.model.Unidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProprietarioService extends ServiceAbstract<Proprietario, IProprietarioRepository> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PerfilService perfilService;

    public ProprietarioService(IProprietarioRepository repository) {
        this.repository = repository;
    }


    public Proprietario buscarPorUnidade(Unidade unidade) {
        Optional<Proprietario> optional = repository.findByUnidadeAndUsuario_Situacao(unidade, true);

        if (optional.isPresent()){
            return optional.get();
        }else {
            throw new ChamaSindicoException("Não foi encontrado nenhum Proprietário.");
        }
    }

    @Override
    public Proprietario salvar(Proprietario proprietario) {

        Optional<Proprietario> propOld =
                repository.findByUnidadeAndUsuario_Situacao(proprietario.getUnidade(), true);

        if (propOld.isPresent()) {
            Proprietario prop = propOld.get();
            prop.getUsuario().setSituacao(false);
            prop.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.PROPRIETARIO.getRole()));

            usuarioRepository.save(prop.getUsuario());
        }


        if (proprietario.isSindico()) {
            proprietario.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.SINDICO.getRole()));
            verificarSindico(proprietario);
        } else {
            proprietario.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.PROPRIETARIO.getRole()));
        }

        return super.salvar(proprietario);
    }

    @Override
    public Proprietario editar(Proprietario proprietario) {

        if (proprietario.isSindico()) {
            proprietario.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.SINDICO.getRole()));
            verificarSindico(proprietario);
        } else {
            proprietario.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.PROPRIETARIO.getRole()));
        }


        return repository.save(proprietario);
    }

    private void verificarSindico(Proprietario prop) {

        Optional<Proprietario> sindico = repository
                .findBySindicoTrueAndCondominio_Id(
                        prop.getUnidade().getId().getBloco().getId().getCondominio().getId()
                );

        if (sindico.isPresent()) {
            Proprietario proprietario = sindico.get();

            proprietario.setSindico(false);
            proprietario.getUsuario().setPerfil(perfilService.buscarPorRole(Roles.PROPRIETARIO.getRole()));

            repository.save(proprietario);
        }
    }

    public Proprietario buscarSindicoPorCondominio(Long idCondominio) {

        Optional<Proprietario> sindico = repository.findBySindicoTrueAndCondominio_Id(idCondominio);

        if (sindico.isPresent()) {
            return sindico.get();
        }else {
            throw new ChamaSindicoException("Não foi encontrado síndico para o Condomínio");
        }
    }
}
