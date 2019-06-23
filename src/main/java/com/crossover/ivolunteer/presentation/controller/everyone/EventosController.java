package com.crossover.ivolunteer.presentation.controller.everyone;

import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.business.service.EventoService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.EventoDto;
import com.crossover.ivolunteer.util.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        // TODO: Add pagination to this
        Stream<Evento> eventos = eventoService.findAll().stream();
        if (idOng != null) {
            eventos = eventos.filter(e -> Objects.equals(e.getOng().getId(), idOng));
        }
        if (nome != null && nome.length() > 0) {
            String finalNome = nome.toLowerCase();
            eventos = eventos.filter(e -> e.getNome().toLowerCase().contains(finalNome));
        }
        if (nomeOng != null && nomeOng.length() > 0) {
            String finalNome = nomeOng.toLowerCase();
            eventos = eventos.filter(e -> e.getOng().getNome().toLowerCase().contains(finalNome));
        }
        if (areas != null && areas.length > 0) {
            eventos = eventos.filter(e -> ArrayUtils.containsAny(e.getAreas().toArray(), areas));
        }
        if (realizados != null) {
            final LocalDateTime now = LocalDateTime.now();
            if (realizados) {
                eventos = eventos.filter(e -> e.getDataRealizacao().isBefore(now));
            } else {
                eventos = eventos.filter(e -> !e.getDataRealizacao().isBefore(now));
            }
        }
        eventos = eventoService.sort(eventos);
        return eventos.map(EventoDto::new).collect(Collectors.toList());
    }

}
