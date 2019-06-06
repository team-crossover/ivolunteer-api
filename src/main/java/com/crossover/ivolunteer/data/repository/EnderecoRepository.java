package com.crossover.ivolunteer.data.repository;

import com.crossover.ivolunteer.business.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

//    Optional<Ong> findByUserId(Long idOwner);

}