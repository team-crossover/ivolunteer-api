package com.crossover.ivolunteer.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Builder.Default
    @ElementCollection
    private List<String> areas = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    private List<Voluntario> confirmados = new ArrayList<>();

    @Basic(fetch = FetchType.LAZY)
    @Size(max = 10485760)
    private String img;

}
