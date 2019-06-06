package com.crossover.ivolunteer.business.service;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configures Spring to retrieve UserDetails from persisted users and roles.
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByUsername(username);
        if (usuario == null)
            throw new UsernameNotFoundException("Couldn't find usuario with username = " + username);

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getSenha(),
                usuario.getTipo().getAuthorities()
        );
    }

}