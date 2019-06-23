package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoDto {

    private Long id;

    @NotBlank
    private String uf;

    @NotBlank
    private String cidade;

    @NotBlank
    private String cep;

    @NotBlank
    private String bairro;

    @NotBlank
    private String complemento1;

    private String complemento2;

    public EnderecoDto(Endereco endereco) {
        this.id = endereco.getId();
        this.uf = endereco.getUf();
        this.cidade = endereco.getCidade();
        this.cep = endereco.getCep();
        this.bairro = endereco.getBairro();
        this.complemento1 = endereco.getComplemento1();
        this.complemento2 = endereco.getComplemento2();
    }

    public Endereco toEntity() {
        return Endereco.builder()
                .id(id)
                .uf(getUf())
                .cidade(getCidade())
                .cep(getCep())
                .bairro(getBairro())
                .complemento1(getComplemento1())
                .complemento2(getComplemento2())
                .build();
    }

}
