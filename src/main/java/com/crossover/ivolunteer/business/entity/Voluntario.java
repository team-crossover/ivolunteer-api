package com.crossover.ivolunteer.business.entity;

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
@ToString(exclude = {"usuario", "eventosConfirmados"}) // Excluir todos os campos derivados do toString()
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

    @ElementCollection
    @Builder.Default
    private List<String> areasInteressadas = new ArrayList<>();

    @ManyToMany
    @Builder.Default
    private List<Ong> ongsSeguidas = new ArrayList<>();

    @OneToOne(orphanRemoval = true)
    private Imagem imgPerfil;

    private Long idImgPerfil;

    // --- Campos derivados ---

    @OneToOne(mappedBy = "voluntario")
    @JsonIgnore
    private Usuario usuario;

    @Builder.Default
    @ManyToMany(mappedBy = "confirmados")
    @JsonIgnore
    private List<Evento> eventosConfirmados = new ArrayList<>();

}
