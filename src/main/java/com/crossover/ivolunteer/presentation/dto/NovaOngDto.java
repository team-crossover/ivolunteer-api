package com.crossover.ivolunteer.presentation.dto;

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
public class NovaOngDto {

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private String doacao;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataFundacao;

    private List<String> areas;

    private String telefone;

    private String email;

    private String urlFacebook;

    private String urlWebsite;

    private EnderecoDto endereco;

}
