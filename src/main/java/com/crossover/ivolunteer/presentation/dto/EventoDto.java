package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDto {

    private Long id;

    private Long idOng;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    private EnderecoDto local;

    @JsonFormat(pattern = ("MM/dd/yyyy HH:mm"))
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = ("MM/dd/yyyy HH:mm"))
    private LocalDateTime dataRealizacao;

    private List<String> areas = new ArrayList<>();

    private String img;

    private List<Long> idsVoluntariosConfirmados = new ArrayList<>();

    public EventoDto(Evento evento) {
        this.id = evento.getId();
        this.idOng = evento.getOng() == null ? null : evento.getOng().getId();
        this.nome = evento.getNome();
        this.descricao = evento.getDescricao();
        this.local = new EnderecoDto(evento.getLocal());
        this.dataCriacao = evento.getDataCriacao();
        this.dataRealizacao = evento.getDataRealizacao();
        this.areas = evento.getAreas();
        this.img = evento.getImg();
        this.idsVoluntariosConfirmados = evento.getConfirmados().stream().map(Voluntario::getId).collect(Collectors.toList());
    }

    public Evento toEntity(Ong ong, Endereco endereco) {
        return Evento.builder()
                .id(id)
                .ong(ong)
                .nome(getNome())
                .descricao(getDescricao())
                .local(endereco)
                .dataCriacao(LocalDateTime.now())
                .dataRealizacao(getDataRealizacao())
                .areas(getAreas())
                .img(getImg())
                .build();
    }

}
