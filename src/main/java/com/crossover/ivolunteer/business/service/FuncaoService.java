package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Funcao;
import com.crossover.ivolunteer.data.repository.FuncaoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
public class FuncaoService extends EntityServiceBase<Funcao, Long, FuncaoRepository> {

//    public Usuario findByGoogleId(String googleId) {
//        return repository.findByGoogleId(googleId).orElse(null);
//    }

}
