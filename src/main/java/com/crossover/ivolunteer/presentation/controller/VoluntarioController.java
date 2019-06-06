package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class VoluntarioController {

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/voluntarios")
    private Collection<Voluntario> getAll() {
        // TODO: Add pagination to this
        return voluntarioService.findAll();
    }

    @GetMapping(path = "/voluntarios/{id}")
    private Voluntario get(@PathVariable("id") long id) {
        Voluntario voluntario = voluntarioService.findById(id);
        if (voluntario == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voluntario not found");
        return voluntario;
    }

//    @PostMapping(path = "/voluntarios", params = {"googleIdToken"})
//    private Voluntario save(@RequestBody Voluntario voluntario,
//                     @RequestParam("googleIdToken") String googleIdToken) {
//
//        Usuario usuario = googleAuthService.getOrCreateUserFromIdToken(googleIdToken);
//        if (usuario == null)
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google ID Token invalid");
//
//        Voluntario currentVolunt = usuario.getVoluntario();
//        if (currentVolunt == null) {
//            voluntario.setId(null);
//            voluntario.setUsuario(usuario);
//            currentVolunt = voluntarioService.save(voluntario);
//            usuario.setVoluntario(currentVolunt);
//            usuario = usuarioService.save(usuario);
//        } else {
//            if (voluntario.getId() == null)
//                voluntario.setId(currentVolunt.getId());
//            else if (!Objects.equals(currentVolunt.getId(), voluntario.getId()))
//                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Usuario already has another Voluntario - Is " + voluntario.getId() + " but should be " + currentVolunt.getId());
//            voluntario.setUsuario(usuario);
//            currentVolunt = voluntarioService.save(voluntario);
//        }
//        return currentVolunt;
//    }

}
