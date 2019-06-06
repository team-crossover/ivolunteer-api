package com.crossover.ivolunteer.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcoes")
@ToString(exclude = {"participacoes"}) // Excluir todos os campos derivados do toString()
public class Funcao {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Evento evento;

    @Size(min = 1, max = 50)
    private String nome;

    @Size(min = 1, max = 200)
    private String descricao;

    @NotNull
    @PositiveOrZero
    private Integer maximoVagas;

    // --- Campos derivados ---

    @OneToMany(mappedBy = "funcao")
    @JsonIgnore
    private List<Participacao> participacoes = new ArrayList<>();

}
