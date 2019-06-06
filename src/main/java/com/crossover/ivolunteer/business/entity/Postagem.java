package com.crossover.ivolunteer.business.entity;

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
@Table(name = "postagens")
@ToString(exclude = {"favoritantes"}) // Excluir todos os campos derivados do toString()
public class Postagem {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Ong ong;

    @Size(min = 1, max = 100)
    private String nome;

    @Size(min = 1, max = 500)
    private String texto;

    private LocalDateTime dataCriacao;

    // --- Campos derivados ---

    @ManyToMany(mappedBy = "postagensFavoritadas")
    private List<Voluntario> favoritantes = new ArrayList<>();

}
