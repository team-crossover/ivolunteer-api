package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

}