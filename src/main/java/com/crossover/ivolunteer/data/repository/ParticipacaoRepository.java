package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Participacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, Long> {

//    Optional<Ong> findByUserId(Long idOwner);

}