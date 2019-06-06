package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.data.repository.UsuarioRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Log4j2
@Service
@Transactional
public class UsuarioService extends EntityServiceBase<Usuario, Long, UsuarioRepository> {

    public Usuario findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

}
