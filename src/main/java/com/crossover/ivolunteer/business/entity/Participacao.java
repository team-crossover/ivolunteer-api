package com.crossover.ivolunteer.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Representa a participação de um voluntário em um evento.
 * Inclui tanto o voluntário marcar como 'confirmar presença' tanto
 * quanto a validação que a ONG dá de que o usuário realmente compareceu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "participacoes")
public class Participacao {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Voluntario voluntario;

    @NotNull
    @ManyToOne
    private Evento evento;

    @NotNull
    @ManyToOne
    private Funcao funcao;

    /**
     * Se o voluntário marcou 'confirmar presença' para aquela função, naquele evento.
     */
    @NotNull
    private Boolean confirmouPresenca;

    /**
     * Se a ONG marcou 'compareceu' para o voluntário, inficando que ele(a) realmente compareceu.
     */
    @NotNull
    private Boolean validadoPelaOng;

}
