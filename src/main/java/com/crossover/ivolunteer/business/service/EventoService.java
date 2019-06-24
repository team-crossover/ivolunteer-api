package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.data.repository.EventoRepository;
import com.crossover.ivolunteer.presentation.dto.EventoDto;
import com.crossover.ivolunteer.util.ArrayUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Log4j2
@Service
@Transactional
public class EventoService extends EntityServiceBase<Evento, Long, EventoRepository> {

    static class EventoComparatorByConfirmados implements Comparator<Evento> {
        public int compare(Evento c1, Evento c2) {
            return c1.getConfirmados().size() - c2.getConfirmados().size();
        }
    }

    public List<EventoDto> getAllDtoFiltered(Long idOng,
                                             String nome,
                                             String nomeOng,
                                             String[] areas,
                                             Boolean realizados) {
        // TODO: Add pagination to this
        Stream<Evento> eventos = findAll().stream();
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
        eventos = eventos.sorted(new EventoService.EventoComparatorByConfirmados());
        return eventos.map(EventoDto::new).collect(Collectors.toList());
    }

}
