package com.crossover.ivolunteer.presentation.dto;

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

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private List<String> areasInteressadas;

}
