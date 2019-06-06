package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.data.repository.OngRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class OngService extends EntityServiceBase<Ong, Long, OngRepository> {

//    public Ong findByUserId(Long idOwner) {
//        return repository.findByUserId(idOwner).orElse(null);
//    }

}
