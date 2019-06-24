package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {

}