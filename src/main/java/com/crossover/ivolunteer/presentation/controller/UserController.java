package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UsuarioService ongService;

//    @GetMapping(path = "/users")
//    private UserDto getByGoogleId(@RequestParam(value = "googleIdToken", required = true) String googleIdToken,
//                                  @RequestParam(value = "asOng", required = false) Boolean asOng) {
//        if (googleIdToken == null)
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Google ID Token not found");
//
//        Usuario usuario = googleAuthService.getOrCreateUserFromIdToken(googleIdToken, asOng);
//        if (usuario == null)
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario couldn't be found/created");
//
//        UserDto userDto = new UserDto(usuario);
//        userDto.setGoogleIdToken(googleIdToken);
//        return userDto;
//    }

}
