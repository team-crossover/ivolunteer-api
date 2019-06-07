package com.crossover.ivolunteer.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
@ToString(exclude = {"favoritantes", "funcoes", "participacoes"}) // Excluir todos os campos derivados do toString()
public class Evento {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Ong ong;

    @Size(min = 1, max = 100)
    private String nome;

    @Size(min = 1, max = 500)
    private String descricao;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco local;

    @NotNull
    private LocalDateTime dataRealizacao;

    private LocalDateTime dataCriacao;

    @ElementCollection
    private List<String> areas = new ArrayList<>();

//    TODO: Adicionar suporte a imagens
//    private Imagem img;

    // --- Campos derivados ---

    @ManyToMany(mappedBy = "eventosFavoritados")
    @JsonIgnore
    private List<Voluntario> favoritantes = new ArrayList<>();

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private List<Funcao> funcoes = new ArrayList<>();

    @OneToMany(mappedBy = "evento")
    @JsonIgnore
    private List<Participacao> participacoes = new ArrayList<>();

}
