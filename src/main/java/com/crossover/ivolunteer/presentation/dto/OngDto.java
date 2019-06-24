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
public class OngDto {

    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    private String doacoes;

    @JsonFormat(pattern = ("MM/dd/yyyy HH:mm"))
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = ("MM/dd/yyyy"))
    private LocalDate dataFundacao;

    private List<String> areas;

    private String telefone;

    private String email;

    private String urlFacebook;

    private String urlWebsite;

    private EnderecoDto endereco;

    private List<Long> idsEventos;

    private List<Long> idsSeguidores;

    private Long idImgPerfil;

    public OngDto(Ong ong) {
        this.id = ong.getId();
        this.nome = ong.getNome();
        this.descricao = ong.getDescricao();
        this.doacoes = ong.getDoacoes();
        this.dataCriacao = ong.getDataCriacao();
        this.dataFundacao = ong.getDataFundacao();
        this.areas = ong.getAreas();
        this.telefone = ong.getTelefone();
        this.email = ong.getEmail();
        this.urlFacebook = ong.getUrlFacebook();
        this.urlWebsite = ong.getUrlWebsite();
        this.endereco = new EnderecoDto(ong.getEndereco());
        this.idsEventos = ong.getEventos().stream().map(Evento::getId).collect(Collectors.toList());
        this.idsSeguidores = ong.getSeguidores().stream().map(Voluntario::getId).collect(Collectors.toList());
        this.idImgPerfil = ong.getImgPerfil() == null ? null : ong.getImgPerfil().getId();
    }

}
