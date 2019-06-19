package com.crossover.ivolunteer.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "postagens")
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
}
