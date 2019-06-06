package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncaoRepository extends JpaRepository<Funcao, Long> {

//    Optional<Ong> findByUserId(Long idOwner);

}