package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {

}