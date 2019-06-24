package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Imagem;
import com.crossover.ivolunteer.business.service.ImagemService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ImagensController {

    @Autowired
    private ImagemService imagemService;

    @GetMapping(ApiPaths.V1.IMAGENS_PREFIX + "/{id}")
    private Imagem get(@PathVariable("id") long id) {
        Imagem img = imagemService.findById(id);
        if (img == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagem not found");
        return null;
    }

}
