package br.com.chamasindico.service;

import br.com.chamasindico.exception.ChamaSindicoException;
import br.com.chamasindico.repository.model.EntityAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class ServiceAbstract<T extends EntityAbstract, R extends JpaRepository> {

    protected static final Logger LOG = LoggerFactory.getLogger(ServiceAbstract.class);

    R repository;

    @Transactional(rollbackFor = ChamaSindicoException.class)
    public Page<T> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional (rollbackFor = ChamaSindicoException.class)
    public List<T> listar() {
        return repository.findAll();
    }

    @Transactional (rollbackFor = ChamaSindicoException.class)
    public T buscarPorId(Object id) throws ChamaSindicoException {

        Optional<T> optional = repository.findById(id);

        return optional
                .orElseThrow(() -> new ChamaSindicoException("erro.naoencontrada:Informação", HttpStatus.NOT_FOUND));
    }

    @Transactional (rollbackFor = ChamaSindicoException.class)
    public T salvar(T t) {
        try {
            return (T) repository.save(t);
        } catch (DataIntegrityViolationException e){
            if (e.getMostSpecificCause().getMessage().contains("duplicate key")
                    || e.getMostSpecificCause().getMessage().contains("primary key")) {
                LOG.error("Erro ao salvar - ", e);
                throw new ChamaSindicoException("erro.duplicado");
            }

            LOG.error("Erro ao salvar - ", e);
            throw new ChamaSindicoException("erro.violacao");
        } catch (Exception e) {
            LOG.error("Erro ao salvar - ", e);
            throw new ChamaSindicoException("erro.persistir");
        }
    }

    @Transactional (rollbackFor = ChamaSindicoException.class)
    public void excluir(Object id) {
        try {
            repository.deleteById(id);
        } catch (UnexpectedRollbackException ue) {
            if (ue.getMostSpecificCause().getMessage().contains("conflicted with the REFERENCE")) {
                LOG.error("Erro ao excluir - ", ue);
                throw new ChamaSindicoException("erro.violacao");
            }
        } catch (Exception ex) {
            LOG.error("Erro ao excluir - ", ex);
            throw new ChamaSindicoException("erro.excluir");
        }
    }

    @Transactional (rollbackFor = ChamaSindicoException.class)
    public T editar(T t){

        this.buscarPorId(t.getId());

        return this.salvar(t);
    }
}