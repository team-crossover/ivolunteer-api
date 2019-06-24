package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoluntarioDto {

    private Long id;

    @NotBlank
    private String nome;

    private String email;

    @JsonFormat(pattern = ("MM/dd/yyyy HH:mm"))
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataNascimento;

    private List<String> areasInteressadas;

    private Long idImgPerfil;

    private List<Long> idsOngsSeguidas;

    private List<Long> idsEventosConfirmados;

    public VoluntarioDto(Voluntario voluntario) {
        this.id = voluntario.getId();
        this.nome = voluntario.getNome();
        this.email = voluntario.getEmail();
        this.dataCriacao = voluntario.getDataCriacao();
        this.dataNascimento = voluntario.getDataNascimento();
        this.areasInteressadas = voluntario.getAreasInteressadas();
        this.idImgPerfil = voluntario.getIdImgPerfil();
        this.idsOngsSeguidas = voluntario.getOngsSeguidas().stream().map(Ong::getId).collect(Collectors.toList());
        this.idsEventosConfirmados = voluntario.getEventosConfirmados().stream().map(Evento::getId).collect(Collectors.toList());
    }
}
