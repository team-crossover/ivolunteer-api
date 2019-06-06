package com.crossover.ivolunteer.business.entity;

import com.crossover.ivolunteer.business.enums.AreaEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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
@Table(name = "ongs")
@ToString(exclude = {"usuario", "eventos", "seguidores"}) // Excluir todos os campos derivados do toString()
public class Ong {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 1, max = 50)
    private String nome;

    @Size(min = 1, max = 500)
    private String descricao;

    @Size(min = 1, max = 500)
    private String doacoes;

    @NotNull
    @OneToOne(orphanRemoval = true)
    private Endereco endereco;

    private LocalDate dataFundacao;

    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private List<AreaEnum> areas = new ArrayList<>();

    @Size(max = 50)
    private String telefone;

    @Email
    @Size(max = 100)
    private String email;

    @Size(max = 100)
    private String urlFacebook;

    @Size(max = 100)
    private String urlWebsite;

//    TODO: Adicionar suporte a imagens
//    private Imagem imgPerfil;
//    private List<Imagem> imgsGaleria = new ArrayList<>();

    // --- Campos derivados ---

    @OneToOne(mappedBy = "ong")
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "ong")
    @JsonIgnore
    private List<Evento> eventos = new ArrayList<>();

    @ManyToMany(mappedBy = "ongsSeguidas")
    @JsonIgnore
    private List<Voluntario> seguidores = new ArrayList<>();

}
