package br.com.chamasindico.repository.model;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitaPK implements Serializable {

    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_visitante")
    private Visitante visitante;
}
