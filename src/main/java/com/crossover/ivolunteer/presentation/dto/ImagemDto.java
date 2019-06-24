package com.crossover.ivolunteer.presentation.dto;

import com.crossover.ivolunteer.business.entity.Imagem;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImagemDto {
    private Long id;
    private String src;

    public ImagemDto(Imagem imagem) {
        id = imagem.getId();
        src = imagem.getSrc();
    }
}
