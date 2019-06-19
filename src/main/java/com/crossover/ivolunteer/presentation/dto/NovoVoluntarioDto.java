package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Voluntario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NovoVoluntarioDto {

    private Long id;

    @NotBlank
    private String username;

    private String senha;

    @NotBlank
    private String nome;

    private String email;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataNascimento;

    private List<String> areasInteressadas;

    public NovoVoluntarioDto(Voluntario voluntario) {
        this.id = voluntario.getId();
        this.username = voluntario.getUsuario().getUsername();
        this.senha = null; // Não tem como retornar a senha porque armazenamos apenas a hash.
        this.nome = voluntario.getNome();
        this.email = voluntario.getEmail();
        this.dataNascimento = voluntario.getDataNascimento();
        this.areasInteressadas = voluntario.getAreasInteressadas();
    }
}
