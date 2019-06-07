package com.crossover.ivolunteer.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoDto {

    @Size(min = 2, max = 2)
    @Column(nullable = false)
    private String uf;

    @Size(max = 50)
    @Column(nullable = false)
    private String cidade;

    @Size(min = 8, max = 8)
    @Column(nullable = false)
    private String cep;

    @Size(max = 50)
    @Column(nullable = false)
    private String bairro;

    @Size(max = 50)
    private String complemento1;

    @Size(max = 50)
    private String complemento2;


}
