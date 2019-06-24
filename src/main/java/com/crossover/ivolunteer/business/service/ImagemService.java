package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Imagem;
import com.crossover.ivolunteer.data.repository.ImagemRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
public class ImagemService extends EntityServiceBase<Imagem, Long, ImagemRepository> {

}
