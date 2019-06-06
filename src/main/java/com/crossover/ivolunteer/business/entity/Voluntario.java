package com.crossover.ivolunteer.business.entity;

import com.crossover.ivolunteer.business.enums.AreaEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voluntarios")
@ToString(exclude = {"usuario", "participacoes"}) // Excluir todos os campos derivados do toString()
public class Voluntario {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 1, max = 100)
    private String nome;

    @Email
    @Size(max = 100)
    private String email;

    private LocalDate dataNascimento;

    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<AreaEnum> areasInteressadas = new ArrayList<>();

    @ManyToMany
    private List<Ong> ongsSeguidas = new ArrayList<>();

    @ManyToMany
    private List<Evento> eventosFavoritados = new ArrayList<>();

    @ManyToMany
    private List<Postagem> postagensFavoritadas = new ArrayList<>();

//    TODO: Adicionar suporte a imagens
//    private Imagem imgPerfil;

    // --- Campos derivados ---

    @OneToOne(mappedBy = "voluntario")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "voluntario")
    @JsonIgnore
    private List<Participacao> participacoes = new ArrayList<>();

}
