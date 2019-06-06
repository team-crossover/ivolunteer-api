package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDto {

    private Long id;
    private String username;
    private String tipo;
    private Long idOng;
    private Long idVoluntario;

    public UsuarioDto(Usuario usuario) {
        id = usuario.getId();
        username = usuario.getUsername();
        tipo = usuario.getTipo().name();
        idVoluntario = usuario.getOng() == null
                ? null : usuario.getOng().getId();
        idVoluntario = usuario.getVoluntario() == null
                ? null : usuario.getVoluntario().getId();
    }

}
