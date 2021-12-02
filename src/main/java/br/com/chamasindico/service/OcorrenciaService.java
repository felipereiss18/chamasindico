package br.com.chamasindico.service;

import br.com.chamasindico.repository.dao.SituacaoOcorrenciaRepository;
import br.com.chamasindico.repository.dao.usuario.OcorrenciaRepository;
import br.com.chamasindico.repository.model.Ocorrencia;
import br.com.chamasindico.repository.model.QOcorrencia;
import br.com.chamasindico.repository.model.SituacaoOcorrencia;
import br.com.chamasindico.repository.model.Unidade;
import br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaSituacao;
import br.com.chamasindico.repository.relatorio.EstatisticaOcorrenciaTipo;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OcorrenciaService extends ServiceAbstract<Ocorrencia, OcorrenciaRepository>{

    @Autowired
    private SituacaoOcorrenciaRepository situacaoOcorrenciaRepository;

    @Autowired
    public OcorrenciaService(OcorrenciaRepository repository){
        this.repository = repository;
    }

    public Page<Ocorrencia> pesquisar(Ocorrencia ocorrencia, Pageable pageable) {
        QOcorrencia qOcorrencia = QOcorrencia.ocorrencia;

        BooleanBuilder builder = new BooleanBuilder();

        if (ocorrencia.getData() != null) {
            builder.and(qOcorrencia.data.eq(ocorrencia.getData()));
        }
        if(ocorrencia.getTipo() != null) {
            builder.and(qOcorrencia.tipo.eq(ocorrencia.getTipo()));
        }
        if(ocorrencia.getSituacao() != null && ocorrencia.getSituacao().getId() != null) {
            builder.and(qOcorrencia.situacao.id.eq(ocorrencia.getSituacao().getId()));
        }
        if(ocorrencia.getDescricao() != null && !ocorrencia.getDescricao().isEmpty()) {
            builder.and(qOcorrencia.descricao.contains(ocorrencia.getDescricao()));
        }
        if(ocorrencia.getFuncionario() != null) {
            builder.and(qOcorrencia.funcionario.id.eq(ocorrencia.getFuncionario().getId()));
        } else if(ocorrencia.getProprietario() != null) {
            builder.and(qOcorrencia.proprietario.id.eq(ocorrencia.getProprietario().getId()));
        } else if (ocorrencia.getInquilino() != null) {
            builder.and(qOcorrencia.inquilino.id.eq(ocorrencia.getInquilino().getId()));
        }

        return this.repository.findAll(builder, pageable);
    }

    public List<SituacaoOcorrencia> buscarSituacoes() {
        return situacaoOcorrenciaRepository.findAll(Sort.by("descricao"));
    }

    public List<Ocorrencia> buscarPorDonoSituacao(Ocorrencia ocorrencia, List<SituacaoOcorrencia> situacoes) {
        if (ocorrencia.getProprietario() != null) {
            return this.repository.findAllByUnidadeAndBlocoAndProprietarioAndSituacaoIn(
                    ocorrencia.getUnidade(), ocorrencia.getBloco(), ocorrencia.getProprietario(), situacoes
            );
        } else if (ocorrencia.getInquilino() != null) {
            return this.repository.findAllByUnidadeAndBlocoAndInquilinoAndSituacaoIn(
                    ocorrencia.getUnidade(), ocorrencia.getBloco(), ocorrencia.getInquilino(), situacoes
            );
        } else if (ocorrencia.getFuncionario() != null) {
            return this.repository.findAllByFuncionarioAndSituacaoIn(ocorrencia.getFuncionario(), situacoes);
        }

        return null;
    }

    public List<Ocorrencia> buscarPorSituacoes(List<SituacaoOcorrencia> situacoes) {
        return this.repository.findAllBySituacaoIn(situacoes);
    }

    public List<Ocorrencia> buscarPorDestinatario(Unidade unidade, List<SituacaoOcorrencia> situacoes) {
        return this.repository.findAllByUnidadeDestinatarioAndSituacaoIn(unidade, situacoes);
    }

    public List<EstatisticaOcorrenciaTipo> buscarEstatisticaTipo(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            return this.repository.findEstatisticaPorTipo();
        }else {
            return this.repository.findEstatisticaPorTipo(inicio, fim);
        }
    }

    public List<EstatisticaOcorrenciaSituacao> buscarEstatisticaSituacao(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            return this.repository.findEstatisticaPorSituacao();
        }else {
            return this.repository.findEstatisticaPorSituacao(inicio, fim);
        }
    }
}
