package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Endereco;
import com.crossover.ivolunteer.data.repository.EnderecoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
public class EnderecoService extends EntityServiceBase<Endereco, Long, EnderecoRepository> {

//    public Usuario findByGoogleId(String googleId) {
//        return repository.findByGoogleId(googleId).orElse(null);
//    }

}
