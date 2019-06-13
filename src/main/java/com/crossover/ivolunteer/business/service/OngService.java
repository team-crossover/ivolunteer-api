package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.data.repository.OngRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Service
@Transactional
public class OngService extends EntityServiceBase<Ong, Long, OngRepository> {


}
