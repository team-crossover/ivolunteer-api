package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Ong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

//    Optional<Ong> findByUserId(Long idOwner);

}