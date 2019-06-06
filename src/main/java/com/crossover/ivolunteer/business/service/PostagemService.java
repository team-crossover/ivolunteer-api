package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Postagem;
import com.crossover.ivolunteer.data.repository.PostagemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
public class PostagemService extends EntityServiceBase<Postagem, Long, PostagemRepository> {

//    public Usuario findByGoogleId(String googleId) {
//        return repository.findByGoogleId(googleId).orElse(null);
//    }

}
