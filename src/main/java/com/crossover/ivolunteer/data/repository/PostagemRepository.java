package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

//    Optional<Ong> findByUserId(Long idOwner);

}