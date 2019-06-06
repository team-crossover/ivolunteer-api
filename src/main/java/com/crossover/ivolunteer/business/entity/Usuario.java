package com.crossover.ivolunteer.business.entity;

import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
@ToString(exclude = {"sessoes"})
public class Usuario {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    private String senha;

    /**
     * Se true, o usuário é/tem uma ong.
     * Se false, é/tem um voluntário.
     */
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipo;

    @OneToOne
    private Ong ong;

    @OneToOne
    private Voluntario voluntario;

    // --- Campos derivados ---

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Collection<Sessao> sessoes = new ArrayList<>();
}
