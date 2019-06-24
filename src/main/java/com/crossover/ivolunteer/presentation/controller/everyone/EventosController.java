package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.business.service.EventoService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.EventoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class EventosController {

    @Autowired
    private EventoService eventoService;

    @GetMapping(ApiPaths.V1.EVENTOS_PREFIX + "/{id}")
    private EventoDto get(@PathVariable("id") long id) {
        Evento evento = eventoService.findById(id);
        if (evento == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento not found");
        return new EventoDto(evento);
    }

    @GetMapping(ApiPaths.V1.EVENTOS_PREFIX)
    private Collection<EventoDto> getAll(@RequestParam(name = "idOng", required = false) Long idOng,
                                         @RequestParam(name = "nome", required = false) String nome,
                                         @RequestParam(name = "nomeOng", required = false) String nomeOng,
                                         @RequestParam(name = "areas", required = false) String[] areas,
                                         @RequestParam(name = "realizados", required = false) Boolean realizados) {
        return eventoService.getAllDtoFiltered(idOng, nome, nomeOng, areas, realizados);
    }

}
