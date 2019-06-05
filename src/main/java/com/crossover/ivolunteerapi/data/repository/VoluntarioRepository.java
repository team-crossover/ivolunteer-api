package com.crossover.ivolunteerapi.data.repository;

import com.crossover.ivolunteerapi.business.entity.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {

    Optional<Voluntario> findByUserId(Long idOwner);

}