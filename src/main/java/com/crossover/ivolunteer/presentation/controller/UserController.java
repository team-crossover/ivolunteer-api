package com.crossover.ivolunteer.presentation.controller;

import com.crossover.ivolunteer.business.entity.Usuario;
import com.crossover.ivolunteer.business.entity.Voluntario;
import com.crossover.ivolunteer.business.enums.TipoUsuarioEnum;
import com.crossover.ivolunteer.business.service.UsuarioService;
import com.crossover.ivolunteer.business.service.VoluntarioService;
import com.crossover.ivolunteer.presentation.constants.ApiPaths;
import com.crossover.ivolunteer.presentation.dto.NovoVoluntarioDto;
import com.crossover.ivolunteer.presentation.dto.UsuarioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VoluntarioService voluntarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(ApiPaths.V1.USERS_PREFIX + "/create/voluntario")
    private UsuarioDto createVoluntario(@Valid @RequestBody NovoVoluntarioDto novoVoluntarioDto) {

        Usuario usuario = usuarioService.findByUsername(novoVoluntarioDto.getUsername());
        if (usuario != null)
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Username already exists");

        Voluntario voluntario = Voluntario.builder()
                .nome(novoVoluntarioDto.getNome())
                .email(novoVoluntarioDto.getEmail())
                .areasInteressadas(novoVoluntarioDto.getAreasInteressadas())
                .dataCriacao(LocalDateTime.now())
                .dataNascimento(novoVoluntarioDto.getDataNascimento())
                .build();
        voluntario = voluntarioService.save(voluntario);

        usuario = Usuario.builder()
                .username(novoVoluntarioDto.getUsername())
                .senha(passwordEncoder.encode(novoVoluntarioDto.getSenha()))
                .tipo(TipoUsuarioEnum.VOLUNTARIO)
                .voluntario(voluntario)
                .build();
        usuario = usuarioService.save(usuario);

        voluntario.setUsuario(usuario);
        voluntario = voluntarioService.save(voluntario);
        return new UsuarioDto(usuario);
    }

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
