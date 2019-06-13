package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Ong;
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

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String senha;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private String doacoes;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataFundacao;

    private List<String> areas;

    private String telefone;

    private String email;

    private String urlFacebook;

    private String urlWebsite;

    private EnderecoDto endereco;

    public NovaOngDto(Ong ong) {
        this.id = ong.getId();
        this.username = ong.getUsuario().getUsername();
        this.senha = null; // Não tem como retornar a senha porque armazenamos apenas a hash.
        this.nome = ong.getNome();
        this.descricao = ong.getDescricao();
        this.doacoes = ong.getDoacoes();
        this.dataFundacao = ong.getDataFundacao();
        this.telefone = ong.getTelefone();
        this.email = ong.getEmail();
        this.urlFacebook = ong.getUrlFacebook();
        this.urlWebsite = ong.getUrlWebsite();
        this.endereco = new EnderecoDto(ong.getEndereco());
    }

}
