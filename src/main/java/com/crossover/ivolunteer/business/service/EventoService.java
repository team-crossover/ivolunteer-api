package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Evento;
import com.crossover.ivolunteer.data.repository.EventoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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

    public Stream<Evento> sort(Stream<Evento> eventos) {
        return eventos.sorted(new EventoService.EventoComparatorByConfirmados());
    }

}
