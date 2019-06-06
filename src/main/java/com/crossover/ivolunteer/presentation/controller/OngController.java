package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Ong;
import com.crossover.ivolunteer.business.service.OngService;
import com.crossover.ivolunteer.business.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
public class OngController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OngService ongService;

    @GetMapping("/ongs")
    private Collection<Ong> getAll() {
        // TODO: Add pagination to this
        return ongService.findAll();
    }

    @GetMapping(path = "/ongs/{id}")
    private Ong get(@PathVariable("id") long id) {
        Ong ong = ongService.findById(id);
        if (ong == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ong;
    }

//    @PostMapping(path = "/ongs", params = {"googleIdToken"})
//    private Ong save(@RequestBody Ong ong,
//                     @RequestParam("googleIdToken") String googleIdToken,
//                     HttpServletRequest request) {
//
//        Usuario usuario = googleAuthService.getOrCreateUserFromIdToken(googleIdToken);
//        if (usuario == null)
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google ID Token invalid");
//
//        Ong currentOng = usuario.getOng();
//        if (currentOng == null) {
//            ong.setId(null);
//            ong.setUsuario(usuario);
//            currentOng = ongService.save(ong);
//            usuario.setOng(currentOng);
//            usuario = usuarioService.save(usuario);
//        } else {
//            if (ong.getId() == null)
//                ong.setId(currentOng.getId());
//            else if (!Objects.equals(currentOng.getId(), ong.getId()))
//                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Usuario already has another Ong - Is " + ong.getId() + " but should be " + currentOng.getId());
//            ong.setUsuario(usuario);
//            currentOng = ongService.save(ong);
//        }
//        return currentOng;
//    }
}
